package cn.leomc.multiblockmachine.common.api;

import com.google.common.collect.Lists;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public class PositionBlock {

    private List<Block> ALLOWED_BLOCKS;

    public PositionBlock(Block... block){
        this();
        add(block);
    }

    public PositionBlock(Tag<Block> tag){
        this();
        add(tag);
    }

    public PositionBlock(){
        this.ALLOWED_BLOCKS = new ArrayList<>();
    }

    public void add(Tag<Block> tag){
        ALLOWED_BLOCKS.addAll(tag.getValues());
    }

    public void add(Block... block){
        ALLOWED_BLOCKS.addAll(Lists.newArrayList(block));
    }

    public List<Block> getAllowedBlocks() {
        return ALLOWED_BLOCKS;
    }

    public boolean isBlockAllowed(Block block){
        return ALLOWED_BLOCKS.contains(block);
    }

}
