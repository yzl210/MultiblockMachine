package cn.leomc.multiblockmachine.common.blockentity;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.common.api.*;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructure;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructures;
import cn.leomc.multiblockmachine.common.api.recipe.MachineRecipe;
import cn.leomc.multiblockmachine.common.api.recipe.MachineRecipeType;
import cn.leomc.multiblockmachine.common.api.recipe.RecipeIngredient;
import cn.leomc.multiblockmachine.common.api.recipe.RecipeResult;
import cn.leomc.multiblockmachine.common.block.ControllerBlock;
import cn.leomc.multiblockmachine.common.menu.ControllerMenu;
import cn.leomc.multiblockmachine.common.registry.BlockEntityRegistry;
import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.block.BlockEntityHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ControllerBlockEntity extends BlockEntity implements MenuProvider, ITickableBlockEntity {

    protected int interval = -100;

    protected boolean formed = false;

    protected MultiblockStructure structure;

    protected MachineRecipe recipe;

    protected boolean working;

    protected String loadRecipe;

    protected double progress;

    protected MachineStatus status;

    protected long counter;

    protected List<IItemSlot> itemSlots;
    protected MultipleContainer itemInputSlots;
    protected MultipleContainer itemOutputSlots;

    protected List<IEnergySlot> energySlots;
    protected MultipleEnergyHandler energyInputSlots;
    protected MultipleEnergyHandler energyOutputSlots;

    protected List<IFluidSlot> fluidSlots;
    protected MultipleFluidHandler fluidInputSlots;
    protected MultipleFluidHandler fluidOutputSlots;


    public ControllerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.CONTROLLER.get(), pos, state);
        itemSlots = new ArrayList<>();
        energySlots = new ArrayList<>();
        fluidSlots = new ArrayList<>();
        working = false;
        counter = 0;
        status = MachineStatus.NOT_FORMED;
        progress = 0;
    }


    @Override
    public void serverTick(ServerLevel level, BlockPos pos, BlockState state) {
        if(interval <= 0 && interval != -100)
            interval = MultiblockMachine.CONFIG.common.controller.interval;
        else
            interval--;

        if(interval <= 0) {
            Direction direction = state.getValue(ControllerBlock.FACING);

            MultiblockStructure newStructure = MultiblockStructures.getFormedStructure(level, pos, direction);

            if (!formed && newStructure != null) {
                formed = true;
                status = MachineStatus.NO_INPUT;
                structure = newStructure;
                itemSlots.addAll(structure.getItemSlots());
                energySlots.addAll(structure.getEnergySlots());
                fluidSlots.addAll(structure.getFluidSlots());
                level.setBlockAndUpdate(pos, state.setValue(ControllerBlock.FORMED, true));
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
                fluidInputSlots = new MultipleFluidHandler(fluidSlots.stream()
                        .filter(energySlot -> energySlot.getSlotType() == SlotType.INPUT)
                        .map(IFluidSlot::getFluidHandler)
                        .collect(Collectors.toList()));
                fluidOutputSlots = new MultipleFluidHandler(fluidSlots.stream()
                        .filter(energySlot -> energySlot.getSlotType() == SlotType.OUTPUT)
                        .map(IFluidSlot::getFluidHandler)
                        .collect(Collectors.toList()));
                BlockEntityHooks.syncData(this);
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
                fluidSlots.clear();
                fluidInputSlots = null;
                fluidOutputSlots = null;
                working = false;
                recipe = null;
                loadRecipe = null;
                status = MachineStatus.NOT_FORMED;
                level.setBlockAndUpdate(pos, state.setValue(ControllerBlock.FORMED, false));
                BlockEntityHooks.syncData(this);
            }


        }

        if (loadRecipe != null) {
            List<MachineRecipe> list = level.getRecipeManager().getAllRecipesFor(MachineRecipeType.INSTANCE).stream()
                    .filter(machineRecipe -> machineRecipe.getId().equals(new ResourceLocation(loadRecipe))).toList();
            recipe = list.size() < 1 ? null : list.get(0);
            loadRecipe = null;
        }

        if (formed && structure != null) {

            if (!working && interval <= 0) {
                Optional<MachineRecipe> recipeOptional = level.getRecipeManager().getRecipeFor(MachineRecipeType.INSTANCE, new FakeContainer(itemInputSlots.copy(), fluidInputSlots.copy()), level);
                if (recipeOptional.isEmpty()) {
                    status = MachineStatus.NO_INPUT;
                    return;
                }
                recipe = recipeOptional.get();

                /* Move to when it is about to output
                if (!itemOutputSlots.canAddItems(recipe.getItemResults().stream().map(RecipeResult::getItem).toList())) {
                    recipe = null;
                    status = MachineStatus.OUTPUT_FULL;
                    return;
                }

                 */
                outer:
                for (RecipeIngredient ingredient : recipe.getItemInputs()) {
                    int removed = 0;
                    for (ItemStack item : ingredient.getItem().getItems()) {
                        removed += itemInputSlots.removeItemType(item.getItem(), ingredient.getIntAmount() - removed).getCount();
                        if (removed >= ingredient.getIntAmount())
                            continue outer;
                    }
                    status = MachineStatus.NO_INPUT;
                    recipe = null;
                    return;
                }


                outer:
                for (RecipeIngredient ingredient : recipe.getFluidInputs()) {
                    long removed = 0;
                    for (Fluid fluid : ingredient.getFluid().getFluids()) {
                        for(int i = 0;i < fluidInputSlots.getSize();i++){
                            long extracted = fluidInputSlots.extractFluid(i, FluidStack.create(fluid, ingredient.getIntAmount() - removed), false, false);
                            removed += extracted;
                            if (removed >= ingredient.getAmount())
                                continue outer;
                        }
                    }
                    status = MachineStatus.NO_INPUT;
                    recipe = null;
                    return;
                }


                status = MachineStatus.WORKING;
                working = true;
            }
            if (recipe == null)
                return;
            if (recipe.requireEnergy()) {
                counter += energyInputSlots.extractEnergy(recipe.getEnergyMaxInput(), false, true);
                progress = (double) counter / recipe.getEnergy() * 100;
            } else {
                counter += 1;
                progress = (double) counter / recipe.getTime() * 100;
            }
            if (recipe.requireEnergy() ? counter >= recipe.getEnergy() : counter >= recipe.getTime()) {
                for (RecipeResult result : recipe.getItemResults())
                    itemOutputSlots.addItem(result.getItem());

                for (RecipeResult result : recipe.getFluidResults())
                    fluidOutputSlots.receiveFluid(0, result.getFluid().copy(), false, true);

                energyOutputSlots.receiveEnergy(recipe.getTotalOutputEnergy(), false, true);
              /*  recipe.getResults().stream()
                        .filter(result -> result.getType() == RecipeResult.ResultType.FLUID)
                        .forEach(result -> fluidOutputSlots);

               */
                recipe = null;
                working = false;
                counter = 0;
                progress = 0;
                if (status == MachineStatus.WORKING)
                    status = MachineStatus.NO_INPUT;
            }
            BlockEntityHooks.syncData(this);
        }
    }
    

    public boolean isFormed() {
        return formed;
    }


    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.putString("recipe", recipe == null ? "" : recipe.getId().toString());
        tag.putLong("counter", counter);
        tag.putBoolean("working", working);
        tag.putString("status", status.toString());
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        if(tag.getBoolean("isUpdatePacket")){
            progress = tag.getDouble("progress");
            status = MachineStatus.valueOf(tag.getString("status"));
            return;
        }
        // String id = tag.getString("structure");
        //if (!id.isEmpty()) {
        //   MultiblockStructure structure = MultiblockStructures.getStructure(new ResourceLocation(id));
        // if (structure != null) {
        //this.structure = structure;
        //this.formed = true;
        this.counter = tag.getLong("counter");
        this.working = tag.getBoolean("working");
        if (tag.contains("recipe"))
            this.loadRecipe = tag.getString("recipe");
        //}
        // }
        status = MachineStatus.valueOf(tag.getString("status"));
    }

    @Nullable
    public MultiblockStructure getStructure() {
        return structure;
    }

    public MachineStatus getStatus() {
        return status;
    }

    @Override
    public Component getDisplayName() {
        return MultiblockStructures.getStructureName(structure.getId());
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new ControllerMenu(this, player, inventory, id);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    //@Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("isUpdatePacket", true);
        tag.putDouble("progress", progress);
        tag.putString("status", status.name());
        return tag;
    }

    public double getProgress() {
        return progress;
    }
}
