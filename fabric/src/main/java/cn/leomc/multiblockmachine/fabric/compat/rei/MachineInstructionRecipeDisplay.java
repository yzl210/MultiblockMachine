package cn.leomc.multiblockmachine.fabric.compat.rei;

import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructure;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructures;
import cn.leomc.multiblockmachine.common.api.recipe.MachineInstructionRecipe;
import cn.leomc.multiblockmachine.common.registry.ItemRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.List;

public class MachineInstructionRecipeDisplay implements Display {

    private ResourceLocation machine;
    private MultiblockStructure structure;
    private MachineInstructionRecipe recipe;

    public MachineInstructionRecipeDisplay(MachineInstructionRecipe recipe, ResourceLocation machine){
        this.machine = machine;
        this.recipe = recipe;
        this.structure = MultiblockStructures.getStructure(machine);
    }


    @Override
    public List<EntryIngredient> getInputEntries() {
        return List.of(EntryIngredients.of(structure.getItem()), EntryIngredients.of(ItemRegistry.CONTROLLER.get()));
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return Collections.singletonList(EntryIngredients.of(structure.getItem()));
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return CategoryIdentifier.of(ReiClientPlugin.getMachineCategory(machine));
    }

    public MachineInstructionRecipe getRecipe() {
        return recipe;
    }
}
