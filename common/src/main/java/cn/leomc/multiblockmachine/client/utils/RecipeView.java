package cn.leomc.multiblockmachine.client.utils;

import cn.leomc.multiblockmachine.common.api.recipe.MachineRecipe;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class RecipeView {

    public static void apply(IGuiAccessor accessor, MachineRecipe recipe) {

        int x = recipe.requireEnergy() ? 30 : 10;
        int y = 5;
        List<Pair<Ingredient, Integer>> inputs = recipe.getInputs();
        for (int i = 0; i < (recipe.requireEnergy() ? 21 : 24); i++) {
            accessor.addInputSlot(inputs.size() > i ? inputs.get(i).getFirst() : Ingredient.EMPTY, x, y);
            x += 18;
            if (x >= 144) {
                y += 18;
                x = recipe.requireEnergy() ? 30 : 10;
            }
        }

        x = recipe.outputEnergy() ? 30 : 10;
        y = 100;
        List<ItemStack> itemResults = recipe.getItemResults();
        for (int i = 0; i < (recipe.outputEnergy() ? 14 : 16); i++) {
            accessor.addOutputSlot(itemResults.size() > i ? itemResults.get(i) : ItemStack.EMPTY, x, y);
            x += 18;
            if (x >= 144) {
                y += 18;
                x = recipe.outputEnergy() ? 30 : 10;
            }
        }

        TextureAtlasSprite arrow = Textures.ARROW_DOWN.get();
        accessor.addCustom(74, 64, arrow.getWidth(), arrow.getHeight(), arrow, Lists.newArrayList(new TextComponent(recipe.getProcessTime() + " ticks")));

        if (recipe.requireEnergy()) {
            TextureAtlasSprite energyBar = Textures.ENERGY_BAR_FULL.get();
            accessor.addCustom(6, 8, energyBar.getWidth(), energyBar.getHeight(), energyBar, Lists.newArrayList(
                    new TextComponent(recipe.getEnergy().longValue + " FE"),
                    new TextComponent(recipe.getEnergyMaxInput().longValue + " FE/t").withStyle(ChatFormatting.GRAY)
            ));
        }
        if (recipe.outputEnergy()) {
            TextureAtlasSprite energyBar = Textures.ENERGY_BAR_FULL.get();
            accessor.addCustom(6, 95, energyBar.getWidth(), energyBar.getHeight(), energyBar, Lists.newArrayList(
                    new TextComponent(recipe.getTotalOutputEnergy().longValue + " FE")
            ));
        }
    }


    public interface IGuiAccessor {

        void addInputSlot(Ingredient ingredient, int x, int y);

        void addOutputSlot(ItemStack itemStack, int x, int y);

        void addCustom(int x, int y, int width, int height, TextureAtlasSprite sprite, List<Component> tooltips);

    }

}
