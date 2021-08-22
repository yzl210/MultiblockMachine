package cn.leomc.multiblockmachine.common.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagCollection;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class TagsRegistry {

    private static final TagCollection<Block> BLOCKS = BlockTags.getAllTags();
    private static final TagCollection<Item> ITEMS = ItemTags.getAllTags();


    public static final Tag<Block> ITEM_SLOT_BLOCKS = BLOCKS.getTag(new ResourceLocation("multiblockmachine", "item_slot"));
    public static final Tag<Block> ENERGY_SLOT_BLOCKS = BLOCKS.getTag(new ResourceLocation("multiblockmachine", "energy_slot"));


}
