package cn.leomc.multiblockmachine.fabric.compat.rei;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.client.utils.RecipeView;
import cn.leomc.multiblockmachine.common.registry.ItemRegistry;
import me.shedaniel.math.Dimension;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class MachineInstructionRecipeDisplayCategory implements DisplayCategory<MachineInstructionRecipeDisplay> {

    public static final ResourceLocation ID = new ResourceLocation(MultiblockMachine.MODID, "instruction");


    @Override
    public CategoryIdentifier<? extends MachineInstructionRecipeDisplay> getCategoryIdentifier() {
        return CategoryIdentifier.of(ID);
    }

    @Override
    public Component getTitle() {
        return new TranslatableComponent("text.multiblockmachine.build_instruction_category");
    }

    @Override
    public int getDisplayHeight() {
        return 150;
    }

    @Override
    public int getDisplayWidth(MachineInstructionRecipeDisplay display) {
        return 160;
    }

   /* @Override
    public int getMaximumDisplaysPerPage() {
        return 1;
    }

    */

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ItemRegistry.MACHINE_ITEM.get());
    }

    @Override
    public List<Widget> setupDisplay(MachineInstructionRecipeDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        Point start = new Point(bounds.getX(), bounds.getY());
        widgets.add(Widgets.createRecipeBase(bounds));
        RecipeView.instruction(ReiClientPlugin.getAccessor(widgets, start), display.getRecipe(), new Dimension(bounds.getWidth(), bounds.getHeight()));
        return widgets;
    }
}
