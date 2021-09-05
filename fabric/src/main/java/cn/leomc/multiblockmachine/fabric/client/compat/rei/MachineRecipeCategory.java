package cn.leomc.multiblockmachine.fabric.client.compat.rei;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.client.utils.RecipeView;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructure;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructures;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MachineRecipeCategory implements RecipeCategory<MachineRecipeDisplay> {

    private ResourceLocation machine;
    private MultiblockStructure structure;

    public MachineRecipeCategory(ResourceLocation machine){
        this.machine = machine;
        this.structure = MultiblockStructures.getStructure(machine);
    }

    @Override
    public @NotNull ResourceLocation getIdentifier() {
        return REIPlugin.getMachineCategory(machine);
    }

    @Override
    public @NotNull String getCategoryName() {
        return MultiblockStructures.getStructureName(machine).getString();
    }

    @Override
    public @NotNull EntryStack getLogo() {
        return EntryStack.create(structure.getItem());
    }

    @Override
    public @NotNull List<Widget> setupDisplay(MachineRecipeDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        Point start = new Point(bounds.getX(), bounds.getY());
        widgets.add(Widgets.createRecipeBase(bounds));
        RecipeView.apply(new RecipeView.IGuiAccessor() {
            @Override
            public void addInputSlot(Ingredient ingredient, int x, int y) {
               widgets.add(Widgets
                       .createSlot(new Point(start.x + x, start.y + y))
                       .markInput()
                       .entries(EntryStack.ofIngredient(ingredient)));
            }

            @Override
            public void addOutputSlot(ItemStack itemStack, int x, int y) {
                widgets.add(Widgets
                        .createSlot(new Point(start.x + x, start.y + y))
                        .markOutput()
                        .entry(EntryStack.create(itemStack)));
            }

            @Override
            public void addCustom(int x, int y, int width, int height, TextureAtlasSprite sprite, List<Component> tooltips) {
                widgets.add(new CustomWidget(start.x + x, start.y + y, width, height, sprite)
                        .tooltips(tooltips));
            }

        }, display.getRecipe());

        return widgets;
       /* MachineRecipe recipe = display.getRecipe();
        Point startPoint = new Point(bounds.getX(), bounds.getY());

        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createCategoryBase(bounds));

        int x = recipe.requireEnergy() ? 30 : 10;
        int y = 5;
        List<Pair<Ingredient, Integer>> inputs = recipe.getInputs();
        for (int i = 0;i < (recipe.requireEnergy() ? 21 : 24);i++) {
            Slot slot = Widgets
                    .createSlot(new Point(startPoint.x + x, startPoint.y + y))
                    .markInput();
            if(inputs.size() > i) {
                int finalI = i;
                slot.entries(EntryStack.ofIngredient(inputs.get(i).getFirst()).stream().peek(entry -> entry.setAmount(inputs.get(finalI).getSecond())).collect(Collectors.toList()));
            }
            widgets.add(slot);
            x += 18;
            if(x >= 144){
                y += 18;
                x = recipe.requireEnergy() ? 30 : 10;
            }
        }

        x = recipe.outputEnergy() ? 30 : 10;
        y = 100;
        List<ItemStack> itemResults = recipe.getItemResults();
        for (int i = 0;i < (recipe.outputEnergy() ? 14 : 16);i++) {
            Slot slot = Widgets
                    .createSlot(new Point(startPoint.x + x, startPoint.y + y))
                    .markInput();
            if(itemResults.size() > i)
                slot.entry(EntryStack.create(itemResults.get(i)));

            widgets.add(slot);
            x += 18;
            if(x >= 144){
                y += 18;
                x = recipe.outputEnergy() ? 30 : 10;
            }
        }

        return widgets;
        */
    }


    @Override
    public int getDisplayHeight() {
        return 150;
    }

    @Override
    public int getDisplayWidth(MachineRecipeDisplay display) {
        return 160;
    }
}
