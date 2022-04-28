package cn.leomc.multiblockmachine.fabric.compat.rei;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.client.utils.RecipeView;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructure;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructures;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class MachineRecipeDisplayCategory implements DisplayCategory<MachineRecipeDisplay> {

    private ResourceLocation machine;
    private MultiblockStructure structure;

    public MachineRecipeDisplayCategory(ResourceLocation machine){
        this.machine = machine;
        this.structure = MultiblockStructures.getStructure(machine);
    }

    @Override
    public Component getTitle() {
        return MultiblockStructures.getStructureName(machine);
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(structure.getItem());
    }

    @Override
    public List<Widget> setupDisplay(MachineRecipeDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        Point start = new Point(bounds.getX(), bounds.getY());
        widgets.add(Widgets.createRecipeBase(bounds));
        RecipeView.recipe(ReiClientPlugin.getAccessor(widgets, start), display.getRecipe());
        return widgets;
    }


    @Override
    public int getDisplayHeight() {
        return 150;
    }

    @Override
    public int getDisplayWidth(MachineRecipeDisplay display) {
        return 160;
    }

    @Override
    public CategoryIdentifier<? extends MachineRecipeDisplay> getCategoryIdentifier() {
        return CategoryIdentifier.of(ReiClientPlugin.getMachineCategory(machine));
    }
}
