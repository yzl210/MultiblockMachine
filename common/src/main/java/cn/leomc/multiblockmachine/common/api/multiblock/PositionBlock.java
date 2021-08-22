package cn.leomc.multiblockmachine.common.api.multiblock;

import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;

public class PositionBlock {

    private final HashMap<Block, BlockState> ALLOWED_BLOCKS;

    public PositionBlock(Block... block){
        this();
        add(block);
    }

    public PositionBlock(Tag<Block> tag){
        this();
        add(tag);
    }

    public PositionBlock() {
        this.ALLOWED_BLOCKS = new HashMap<>();
    }

    public void add(Tag<Block> tag) {
        tag.getValues().forEach(block -> ALLOWED_BLOCKS.put(block, null));
    }

    public void add(Block... blocks) {
        for (Block block : blocks)
            ALLOWED_BLOCKS.put(block, null);
    }

    public HashMap<Block, BlockState> getAllowedBlocks() {
        return ALLOWED_BLOCKS;
    }

    public boolean isBlockAllowed(Block block) {
        return ALLOWED_BLOCKS.containsKey(block);
    }
/*
    public boolean isBlockAllowed(BlockState state){
        return ALLOWED_BLOCKS.containsKey(state.getBlock()) ?  : false;
    }

    private boolean blockStateMatch(BlockState state, BlockState anotherState){
        return state.get
    }

 */

}
