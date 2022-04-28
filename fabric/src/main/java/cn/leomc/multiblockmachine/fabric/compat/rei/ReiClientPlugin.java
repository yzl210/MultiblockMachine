package cn.leomc.multiblockmachine.fabric.compat.rei;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.client.utils.RecipeView;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructure;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructures;
import cn.leomc.multiblockmachine.common.api.recipe.*;
import cn.leomc.multiblockmachine.common.item.MachineItem;
import dev.architectury.fluid.FluidStack;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Slot;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.display.DynamicDisplayGenerator;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class ReiClientPlugin implements REIClientPlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new MachineInstructionRecipeDisplayCategory());
        for(ResourceLocation machine : MultiblockStructures.INSTANCE.MACHINES.keySet()) {
            registry.add(new MachineRecipeDisplayCategory(machine));
            registry.addWorkstations(CategoryIdentifier.of(getMachineCategory(machine)), EntryStacks.of(MultiblockStructures.getStructure(machine).getItem()));
            registry.removePlusButton(CategoryIdentifier.of(getMachineCategory(machine)));
        }
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {

        registry.registerRecipeFiller(MachineRecipe.class, MachineRecipeType.INSTANCE, recipe -> new MachineRecipeDisplay(recipe, recipe.getMachineID()));

        registry.registerDisplayGenerator(CategoryIdentifier.of(MachineInstructionRecipeDisplayCategory.ID), new DynamicDisplayGenerator<MachineInstructionRecipeDisplay>() {
            @Override
            public Optional<List<MachineInstructionRecipeDisplay>> getRecipeFor(EntryStack<?> entry) {
                if(entry.getType() == VanillaEntryTypes.ITEM && entry.getValue() instanceof ItemStack itemStack && MachineItem.valid(itemStack)){
                    ResourceLocation machine = ResourceLocation.tryParse(itemStack.getTag().getString("machine"));
                    return Optional.of(Collections.singletonList(new MachineInstructionRecipeDisplay(new MachineInstructionRecipe(machine, machine), machine)));
                }
                return Optional.empty();
            }
        });
    }



    public static ResourceLocation getMachineCategory(ResourceLocation machine){
        return new ResourceLocation(MultiblockMachine.MODID, machine.getNamespace() + "_" + machine.getPath());
    }


    public static RecipeView.IGuiAccessor getAccessor(List<Widget> widgets, Point start){
        return new RecipeView.IGuiAccessor() {
            @Override
            public void addInputSlot(RecipeIngredient ingredient, int x, int y) {
                widgets.add(Widgets
                        .createSlot(new Point(start.x + x, start.y + y))
                        .markInput()
                        .entries(Arrays.stream(ingredient.getItem().getItems()).map(EntryStacks::of).toList()));
            }

            @Override
            public void addOutputSlot(RecipeResult result, int x, int y) {
                widgets.add(Widgets
                        .createSlot(new Point(start.x + x, start.y + y))
                        .markOutput()
                        .entry(EntryStacks.of(result.getItem())));
            }

            @Override
            public void addFluidInputSlot(RecipeIngredient ingredient, int x, int y) {
                    widgets.add(Widgets
                            .createSlot(new Point(start.x + x, start.y + y))
                            .markOutput()
                            .entries(ingredient
                                    .getFluid()
                                    .getFluids()
                                    .stream()
                                    .map(fluid -> FluidStack.create(fluid, ingredient.getAmount()))
                                    .map(EntryStacks::of)
                                    .toList()));
            }

            @Override
            public void addFluidOutputSlot(RecipeResult result, int x, int y) {
                widgets.add(Widgets
                        .createSlot(new Point(start.x + x, start.y + y))
                        .markOutput()
                        .entry(EntryStacks.of(result.getFluid())));
            }

            @Override
            public void addCustom(int x, int y, int width, int height, TextureAtlasSprite sprite, List<Component> tooltips) {
                widgets.add(new CustomWidget(start.x + x, start.y + y, width, height, sprite)
                        .tooltips(tooltips));
            }

            @Override
            public void addButton(int x, int y, int width, int height, Component text, Runnable onClick) {
                widgets.add(
                        Widgets
                                .createButton(new Rectangle(start.x + x, start.y + y, width, height), text)
                                .onClick(button -> onClick.run())

                );
            }

            @Override
            public void addText(int x, int y, int color, Component text, boolean shadow) {
                widgets.add(
                        Widgets
                                .createLabel(new Point(start.x + x, start.y + y), text)
                                .color(color)
                                .shadow(shadow)
                );
            }
        };
    }

}
