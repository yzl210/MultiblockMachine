package cn.leomc.multiblockmachine.common.api.multiblock;

import cn.leomc.multiblockmachine.common.api.IEnergySlot;
import cn.leomc.multiblockmachine.common.api.IItemSlot;
import cn.leomc.multiblockmachine.common.api.recipe.MachineRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiblockStructure {

    private ResourceLocation id;

    private Map<BlockPos, PositionBlock> BLOCKS;
    private Map<ResourceLocation, MachineRecipe> RECIPES;

    private List<IItemSlot> itemSlots;
    private List<IEnergySlot> energySlots;

    public MultiblockStructure(ResourceLocation id, Map<BlockPos, PositionBlock> map) {
        this.id = id;
        this.BLOCKS = map;
        this.RECIPES = new HashMap<>();
    }

    public boolean isFormed(Level level, BlockPos origin, Direction facing) {
        List<IItemSlot> itemSlots = new ArrayList<>();
        List<IEnergySlot> energySlots = new ArrayList<>();

        for (Map.Entry<BlockPos, PositionBlock> entry : BLOCKS.entrySet()) {

            BlockPos pos = entry.getKey();

            int offsetX;
            int offsetZ;

            switch (facing) {
                case EAST:
                    offsetX = -pos.getZ();
                    offsetZ = -pos.getX();
                    break;
                case WEST:
                    offsetX = pos.getZ();
                    offsetZ = pos.getX();
                    break;
                case SOUTH:
                    offsetX = pos.getX();
                    offsetZ = -pos.getZ();
                    break;
                case NORTH:
                    offsetX = -pos.getX();
                    offsetZ = pos.getZ();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + facing);
            }


            if (!entry.getValue().isBlockAllowed(level.getBlockState(origin.offset(offsetX, pos.getY(), offsetZ)).getBlock()))
                return false;

            BlockEntity entity = level.getBlockEntity(origin.offset(offsetX, pos.getY(), offsetZ));
            if (entity instanceof IItemSlot)
                itemSlots.add((IItemSlot) entity);
            if (entity instanceof IEnergySlot)
                energySlots.add((IEnergySlot) entity);

        }

        this.itemSlots = itemSlots;
        this.energySlots = energySlots;
        return true;
    }


    public MultiblockStructure addBlock(BlockPos pos, PositionBlock positionBlock) {
        BLOCKS.put(pos, positionBlock);
        return this;
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

    public List<IEnergySlot> getEnergySlots() {
        return energySlots;
    }

    public List<IItemSlot> getItemSlots() {
        return itemSlots;
    }
}
