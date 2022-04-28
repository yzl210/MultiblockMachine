package cn.leomc.multiblockmachine.fabric.compat.rei;

import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructure;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructures;
import cn.leomc.multiblockmachine.common.api.recipe.MachineRecipe;
import dev.architectury.fluid.FluidStack;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class MachineRecipeDisplay implements Display {

    private ResourceLocation machine;
    private MultiblockStructure structure;
    private MachineRecipe recipe;

    public MachineRecipeDisplay(MachineRecipe recipe, ResourceLocation machine){
        this.machine = machine;
        this.recipe = recipe;
        this.structure = MultiblockStructures.getStructure(machine);
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        List<EntryIngredient> ingredients = new ArrayList<>();
        recipe.getInputs().stream().filter(ingredient -> ingredient.getType().isIngredient()).forEach(ingredient -> {
            switch (ingredient.getType()){
                case ITEM -> ingredients.add(EntryIngredients.ofIngredient(ingredient.getItem()));
                case FLUID -> ingredients.add(EntryIngredients.of(VanillaEntryTypes.FLUID,
                        ingredient.getFluid().getFluids().stream().map(fluid -> FluidStack.create(fluid, ingredient.getAmount())).toList()));
            }
        });
        return ingredients;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        List<EntryIngredient> ingredients = new ArrayList<>();
        recipe.getResults().stream().filter(ingredient -> ingredient.getType().isIngredient()).forEach(result -> {
            switch (result.getType()){
                case ITEM -> ingredients.add(EntryIngredients.of(result.getItem()));
                case FLUID -> ingredients.add(EntryIngredients.of(result.getFluid()));
            }
        });
        return ingredients;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return CategoryIdentifier.of(ReiClientPlugin.getMachineCategory(machine));
    }

    public MachineRecipe getRecipe() {
        return recipe;
    }
}
