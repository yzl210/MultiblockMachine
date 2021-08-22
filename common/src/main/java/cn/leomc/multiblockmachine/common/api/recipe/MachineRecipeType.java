package cn.leomc.multiblockmachine.common.api.recipe;

import cn.leomc.multiblockmachine.MultiblockMachine;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

public enum MachineRecipeType implements RecipeType<MachineRecipe> {
    INSTANCE;

    public static final ResourceLocation ID = new ResourceLocation(MultiblockMachine.MODID, "machine");

    @Override
    public String toString() {
        return ID.toString();
    }
}
