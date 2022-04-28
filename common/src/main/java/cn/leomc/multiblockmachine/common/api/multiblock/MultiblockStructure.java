package cn.leomc.multiblockmachine.common.api.multiblock;

import cn.leomc.multiblockmachine.common.api.IEnergySlot;
import cn.leomc.multiblockmachine.common.api.IFluidSlot;
import cn.leomc.multiblockmachine.common.api.IItemSlot;
import cn.leomc.multiblockmachine.common.api.recipe.MachineRecipe;
import cn.leomc.multiblockmachine.common.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiblockStructure {

    private ResourceLocation id;

    private ItemStack item;

    private Map<BlockPos, PositionBlock> BLOCKS;
    private Map<ResourceLocation, MachineRecipe> RECIPES;

    private List<IItemSlot> itemSlots;
    private List<IEnergySlot> energySlots;
    private List<IFluidSlot> fluidSlots;


    public MultiblockStructure(ResourceLocation id, Map<BlockPos, PositionBlock> map) {
        this.id = id;
        this.BLOCKS = map;
        this.RECIPES = new HashMap<>();
        this.item = new ItemStack(ItemRegistry.MACHINE_ITEM.get());
        item.getOrCreateTag().putString("machine", id.toString());
    }

    public static MultiblockStructure of(CompoundTag tag) {

        ResourceLocation id = new ResourceLocation(tag.getString("id"));

        Map<BlockPos, PositionBlock> blocks = new HashMap<>();

        for (Tag t : tag.getList("blocks", 10)) {
            CompoundTag compound = (CompoundTag) t;
            blocks.put(new BlockPos(compound.getInt("x"), compound.getInt("y"), compound.getInt("z")),
                    PositionBlock.of(compound.getCompound("block")));
        }

        return new MultiblockStructure(id, blocks);
    }

    public boolean isFormed(Level level, BlockPos origin, Direction facing) {
        List<IItemSlot> itemSlots = new ArrayList<>();
        List<IEnergySlot> energySlots = new ArrayList<>();
        List<IFluidSlot> fluidSlots = new ArrayList<>();


        for (Map.Entry<BlockPos, PositionBlock> entry : BLOCKS.entrySet()) {

            BlockPos pos = origin.offset(MultiblockStructures.getOffset(entry.getKey(), facing));


            if (!entry.getValue().test(level.getBlockState(pos).getBlock()))
                return false;

            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof IItemSlot itemSlot)
                itemSlots.add(itemSlot);
            if (entity instanceof IEnergySlot energySlot)
                energySlots.add(energySlot);
            if (entity instanceof IFluidSlot fluidSlot)
                fluidSlots.add(fluidSlot);

        }

        this.itemSlots = itemSlots;
        this.energySlots = energySlots;
        this.fluidSlots = fluidSlots;
        return true;
    }

    public MultiblockStructure addBlock(BlockPos pos, PositionBlock positionBlock) {
        if(BLOCKS.containsKey(pos))
            throw new IllegalArgumentException("Pos " + pos + " has already defined before");
        BLOCKS.put(pos, positionBlock);
        return this;
    }

    public Map<BlockPos, PositionBlock> getBlocks() {
        return BLOCKS;
    }

    public ResourceLocation getId() {
        return id;
    }

    public void addRecipe(ResourceLocation id, MachineRecipe recipe) {
        RECIPES.put(id, recipe);
    }

    public MachineRecipe getRecipe(ResourceLocation id) {
        return RECIPES.get(id);
    }

    public Collection<MachineRecipe> getRecipes() {
        return RECIPES.values();
    }

    public List<IEnergySlot> getEnergySlots() {
        return energySlots;
    }

    public List<IItemSlot> getItemSlots() {
        return itemSlots;
    }

    public List<IFluidSlot> getFluidSlots() {
        return fluidSlots;
    }

    public ItemStack getItem() {
        return item.copy();
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", id.toString());
        ListTag list = new ListTag();
        for (Map.Entry<BlockPos, PositionBlock> entry : BLOCKS.entrySet()) {
            CompoundTag block = new CompoundTag();
            block.putInt("x", entry.getKey().getX());
            block.putInt("y", entry.getKey().getY());
            block.putInt("z", entry.getKey().getZ());
            block.put("block", entry.getValue().save());
            list.add(block);
        }
        tag.put("blocks", list);
        return tag;
    }

}
