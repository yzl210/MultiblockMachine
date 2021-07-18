package cn.leomc.multiblockmachine.common.api;

import cn.leomc.multiblockmachine.common.registry.TagsRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;

public class MultiblockStructures {

    public static MultiblockStructure TESTSTRUCTURE = new MultiblockStructure(new ResourceLocation("multiblockmachine", "test"), new HashMap<>())
            .addBlock(new BlockPos(1, 0, 0), new PositionBlock(Blocks.IRON_BLOCK))
            .addBlock(new BlockPos(0, 0, 1), new PositionBlock(Blocks.DIAMOND_BLOCK))
            .addBlock(new BlockPos(0, 1, 0), new PositionBlock(BlockTags.WOOL))
            .addBlock(new BlockPos(-1,0,0), new PositionBlock(TagsRegistry.ITEM_SLOT_BLOCKS));

}
