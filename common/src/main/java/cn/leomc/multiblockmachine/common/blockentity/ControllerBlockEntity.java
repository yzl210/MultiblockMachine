package cn.leomc.multiblockmachine.common.blockentity;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.common.api.*;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructure;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructures;
import cn.leomc.multiblockmachine.common.api.recipe.MachineRecipe;
import cn.leomc.multiblockmachine.common.api.recipe.MachineRecipeType;
import cn.leomc.multiblockmachine.common.api.recipe.RecipeResult;
import cn.leomc.multiblockmachine.common.block.ControllerBlock;
import cn.leomc.multiblockmachine.common.menu.ControllerMenu;
import cn.leomc.multiblockmachine.common.registry.BlockEntityRegistry;
import me.shedaniel.architectury.extensions.BlockEntityExtension;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ControllerBlockEntity extends BlockEntity implements TickableBlockEntity, MenuProvider, BlockEntityExtension {

    protected boolean formed = false;

    protected MultiblockStructure structure;

    protected MachineRecipe recipe;

    protected boolean working;

    protected double progress;

    protected MachineStatus status;

    protected DoubleLong counter;

    protected List<IItemSlot> itemSlots;
    protected MultipleContainer itemInputSlots;
    protected MultipleContainer itemOutputSlots;

    protected List<IEnergySlot> energySlots;
    protected MultipleEnergyHandler energyInputSlots;
    protected MultipleEnergyHandler energyOutputSlots;

    protected List<IFluidSlot> fluidSlots;
    protected MultipleFluidHandler fluidInputSlots;
    protected MultipleFluidHandler fluidOutputSlots;


    public ControllerBlockEntity() {
        super(BlockEntityRegistry.CONTROLLER.get());
        itemSlots = new ArrayList<>();
        energySlots = new ArrayList<>();
        working = false;
        counter = DoubleLong.of(0);
        status = MachineStatus.NOT_FORMED;
        progress = 0;
    }

    @Override
    public void tick() {
        if (level.isClientSide)
            return;

        Direction direction = getBlockState().getValue(ControllerBlock.FACING);

        MultiblockStructure newStructure = MultiblockStructures.getFormedStructure(level, worldPosition, direction);

        if (!formed && newStructure != null) {
            formed = true;
            status = MachineStatus.NO_INPUT;
            structure = newStructure;
            itemSlots.addAll(structure.getItemSlots());
            energySlots.addAll(structure.getEnergySlots());
            itemInputSlots = new MultipleContainer(itemSlots.stream()
                    .filter(itemSlot -> itemSlot.getSlotType() == SlotType.INPUT)
                    .map(IItemSlot::getContainer)
                    .collect(Collectors.toList()));
            itemOutputSlots = new MultipleContainer(itemSlots.stream()
                    .filter(itemSlot -> itemSlot.getSlotType() == SlotType.OUTPUT)
                    .map(IItemSlot::getContainer)
                    .collect(Collectors.toList()));
            energyInputSlots = new MultipleEnergyHandler(energySlots.stream()
                    .filter(energySlot -> energySlot.getSlotType() == SlotType.INPUT)
                    .map(IEnergySlot::getEnergyHandler)
                    .collect(Collectors.toList()));
            energyOutputSlots = new MultipleEnergyHandler(energySlots.stream()
                    .filter(energySlot -> energySlot.getSlotType() == SlotType.OUTPUT)
                    .map(IEnergySlot::getEnergyHandler)
                    .collect(Collectors.toList()));

        }

        if (formed && newStructure != structure) {
            formed = false;
            structure = null;
            itemSlots.clear();
            itemInputSlots = null;
            itemOutputSlots = null;
            energySlots.clear();
            energyInputSlots = null;
            energyOutputSlots = null;
            working = false;
            recipe = null;
            status = MachineStatus.NOT_FORMED;
        }


        if (formed && structure != null && itemInputSlots != null && itemOutputSlots != null) {

            boolean canAdd = true;


            if (!working && canAdd) {
                Optional<MachineRecipe> recipeOptional = level.getRecipeManager().getRecipeFor(MachineRecipeType.INSTANCE, itemInputSlots, level);
                if (!recipeOptional.isPresent())
                    return;
                this.recipe = recipeOptional.get();
                for (ItemStack itemStack : recipe.getInputs()) {
                    ItemStack itemStack1 = itemInputSlots.hasItem(itemStack);
                    if (itemStack1.isEmpty()) {
                        recipe = null;
                        return;
                    }
                }
                for (ItemStack itemStack : recipe.getInputs()) {
                    itemInputSlots.removeItemType(itemStack.getItem(), itemStack.getCount());
                    status = MachineStatus.WORKING;
                    working = true;
                }
            }
            if (recipe.requireEnergy()) {
                counter.add(energyInputSlots.extractEnergy(recipe.getEnergyMaxInput(), false));
                progress = new BigDecimal(counter.doubleValue / recipe.getEnergy().doubleValue * 100).setScale(1, RoundingMode.HALF_UP).doubleValue();
            } else {
                counter.add(1);
                progress = new BigDecimal(counter.doubleValue / recipe.getTime() * 100).setScale(1, RoundingMode.HALF_UP).doubleValue();
            }
            if (recipe.requireEnergy() ? counter.doubleValue > recipe.getEnergy().doubleValue : counter.doubleValue > recipe.getTime()) {
                recipe.getResults().stream()
                        .filter(result -> result.getType() == RecipeResult.ResultType.ITEM)
                        .forEach(result -> itemOutputSlots.addItem(result.getItem()));
                recipe.getResults().stream()
                        .filter(result -> result.getType() == RecipeResult.ResultType.ENERGY)
                        .forEach(result -> energyOutputSlots.receiveEnergy(result.getEnergy(), false));
                recipe.getResults().stream()
                        .filter(result -> result.getType() == RecipeResult.ResultType.FLUID)
                        .forEach(result -> fluidOutputSlots);
                recipe = null;
                working = false;
                counter = DoubleLong.of(0);
                progress = 0;
                if (status == MachineStatus.WORKING)
                    status = MachineStatus.NO_INPUT;
            }
        }
        syncData();
    }

    public boolean isFormed() {
        return formed;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        //tag.putString("structure", structure == null ? "" : structure.getId().toString());
        tag.putString("recipe", recipe == null ? "" : recipe.getId().toString());
        tag.putDouble("counter", counter.doubleValue);
        tag.putBoolean("working", working);
        tag.putString("status", status.toString());
        return super.save(tag);
    }

    @Override
    public void load(BlockState state, CompoundTag tag) {
        super.load(state, tag);
       // String id = tag.getString("structure");
        //if (!id.isEmpty()) {
         //   MultiblockStructure structure = MultiblockStructures.getStructure(new ResourceLocation(id));
           // if (structure != null) {
                //this.structure = structure;
                //this.formed = true;
        this.counter = DoubleLong.of(tag.getDouble("counter"));
        this.working = tag.getBoolean("working");
        String recipe = tag.getString("recipe");
        List<MachineRecipe> list = level.getRecipeManager().getAllRecipesFor(MachineRecipeType.INSTANCE).stream()
                .filter(machineRecipe -> machineRecipe.getId().equals(new ResourceLocation(recipe)))
                .collect(Collectors.toList());

        this.recipe = list.size() < 1 ? null : list.get(0);
            //}
       // }
        status = MachineStatus.valueOf(tag.getString("status"));
    }

    @Nullable
    public MultiblockStructure getStructure() {
        return structure;
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("multiblockmachine." + structure.getId().getNamespace() + "." + structure.getId().getPath());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new ControllerMenu(this, player, inventory, id);
    }

    @Override
    public void loadClientData(BlockState state, CompoundTag tag) {
        progress = tag.getDouble("progress");
    }

    @Override
    public CompoundTag saveClientData(CompoundTag tag) {
        tag.putDouble("progress", progress);
        return tag;
    }

    public double getProgress() {
        return progress;
    }
}
