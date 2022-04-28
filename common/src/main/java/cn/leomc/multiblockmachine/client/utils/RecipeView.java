package cn.leomc.multiblockmachine.client.utils;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructures;
import cn.leomc.multiblockmachine.common.api.recipe.*;
import cn.leomc.multiblockmachine.common.network.NetworkHandler;
import cn.leomc.multiblockmachine.common.network.packet.ShowInstructionMessage;
import cn.leomc.multiblockmachine.common.utils.PlatformSpecific;
import com.google.common.collect.Lists;
import dev.architectury.fluid.FluidStack;
import dev.architectury.platform.Platform;
import me.shedaniel.math.Dimension;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class RecipeView {

    public static void recipe(IGuiAccessor accessor, MachineRecipe recipe) {
        int x = recipe.requireEnergy() ? 30 : 10;
        int y = 5;
        int i = 0;
        int maxI = (recipe.requireEnergy() ? 21 : 24);
        List<RecipeIngredient> itemInputs = recipe.getItemInputs();
        for (; i < maxI && i < itemInputs.size(); i++) {
            accessor.addInputSlot(itemInputs.get(i), x, y);
            x += 18;
            if (x >= 144) {
                y += 18;
                x = recipe.requireEnergy() ? 30 : 10;
            }
        }
        int ai = i;
        List<RecipeIngredient> fluidInputs = recipe.getFluidInputs();
        for (; i < maxI; i++) {
            accessor.addFluidInputSlot(fluidInputs.size() > i - ai ? fluidInputs.get(i - ai) : RecipeIngredient.EMPTY, x, y);
            x += 18;
            if (x >= 144) {
                y += 18;
                x = recipe.requireEnergy() ? 30 : 10;
            }
        }


        //output
        x = recipe.outputEnergy() ? 30 : 10;
        y = 100;
        maxI  = recipe.outputEnergy() ? 14 : 16;
        List<RecipeResult> itemResults = recipe.getItemResults();
        for (i = 0; i < maxI && i < itemResults.size(); i++) {
            accessor.addOutputSlot(itemResults.get(i), x, y);
            x += 18;
            if (x >= 144) {
                y += 18;
                x = recipe.outputEnergy() ? 30 : 10;
            }
        }

        ai = i;
        List<RecipeResult> fluidResults = recipe.getFluidResults();
        for (; i < maxI; i++) {
            accessor.addFluidOutputSlot(fluidResults.size() > i - ai ? fluidResults.get(i - ai) : RecipeResult.EMPTY, x, y);
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
                    new TranslatableComponent("text.multiblockmachine.additional_inputs"),
                    new TextComponent("")
                            .append(IngredientType.getDescription(RecipeResult.of(recipe.getEnergy())))
                            .append(" ")
                            .append(new TranslatableComponent("text.multiblockmachine.ingredient.energy_max_input", recipe.getEnergyMaxInput(), PlatformSpecific.getEnergy()).withStyle(ChatFormatting.GRAY))
            ));
        }
        if (recipe.outputEnergy()) {
            TextureAtlasSprite energyBar = Textures.ENERGY_BAR_FULL.get();
            accessor.addCustom(6, 95, energyBar.getWidth(), energyBar.getHeight(), energyBar, Lists.newArrayList(
                    new TranslatableComponent("text.multiblockmachine.additional_outputs"),
                    IngredientType.getDescription(RecipeResult.of(recipe.getEnergy()))
            ));
        }
    }

    public static void instruction(IGuiAccessor accessor, MachineInstructionRecipe recipe, Dimension bounds) {
        Component name = MultiblockStructures.getStructureName(recipe.getMachine());
        accessor.addText(
                Platform.isForge() ? (bounds.getWidth() - Minecraft.getInstance().font.width(name.getString())) / 2 : bounds.getWidth() / 2,
                15,
                0,
                name,
                false
        );

        accessor.addOutputSlot(
                RecipeResult.of(recipe.getStructure().getItem()),
                (bounds.getWidth() - 18) / 2,
                45);

        Component text = new TranslatableComponent("text.multiblockmachine.show_structure");
        int width = Minecraft.getInstance().font.width(text.getString()) + 15;
        accessor.addButton(
                (bounds.getWidth() - width) / 2,
                75,
                width,
                Minecraft.getInstance().font.lineHeight + 10,
                text,
                () -> NetworkHandler.INSTANCE.sendToServer(new ShowInstructionMessage(recipe.getMachine()))
        );
    }

    public interface IGuiAccessor {

        void addInputSlot(RecipeIngredient ingredient, int x, int y);

        void addOutputSlot(RecipeResult itemStack, int x, int y);

        void addFluidInputSlot(RecipeIngredient ingredient, int x, int y);

        void addFluidOutputSlot(RecipeResult fluidStack, int x, int y);

        void addCustom(int x, int y, int width, int height, TextureAtlasSprite sprite, List<Component> tooltips);

        void addButton(int x, int y, int width, int height, Component text, Runnable onClick);

        void addText(int x, int y, int color, Component text, boolean shadow);

    }

}
