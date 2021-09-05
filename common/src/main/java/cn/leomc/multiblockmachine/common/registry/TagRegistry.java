package cn.leomc.multiblockmachine.common.registry;

import me.shedaniel.architectury.hooks.TagHooks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.Block;

public class TagRegistry {

    public static final Tag.Named<Block> ITEM_SLOT_BLOCKS = TagHooks.getBlockOptional(new ResourceLocation("multiblockmachine", "item_slot"));
    public static final Tag.Named<Block> ENERGY_SLOT_BLOCKS = TagHooks.getBlockOptional(new ResourceLocation("multiblockmachine", "energy_slot"));


}
