package cn.leomc.multiblockmachine.common.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.Map;

public class MultiblockStructure {

    private ResourceLocation id;

    private Map<BlockPos, PositionBlock> BLOCKS;

    public MultiblockStructure(ResourceLocation id, Map<BlockPos, PositionBlock> map){
        this.id = id;
        this.BLOCKS = map;
    }

    public boolean isFormed(Level level, BlockPos origin, Direction facing){
        for (Map.Entry<BlockPos, PositionBlock> entry : BLOCKS.entrySet()) {

            BlockPos pos = entry.getKey();

            int offsetX;
            int offsetZ;

            switch (facing){
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


            if(!entry.getValue().isBlockAllowed(level.getBlockState(origin.offset(offsetX, pos.getY(), offsetZ)).getBlock()))
                return false;
        }
        return true;
    }

    public MultiblockStructure addBlock(BlockPos pos, PositionBlock positionBlock){
        BLOCKS.put(pos, positionBlock);
        return this;
    }

    public ResourceLocation getId() {
        return id;
    }
}
