package cn.leomc.multiblockmachine.fabric.client.compat.rei;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructure;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructures;
import cn.leomc.multiblockmachine.common.api.recipe.MachineRecipe;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;
import me.shedaniel.rei.utils.CollectionUtils;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class MachineRecipeDisplay implements RecipeDisplay {

    private ResourceLocation machine;
    private MultiblockStructure structure;
    private MachineRecipe recipe;

    public MachineRecipeDisplay(MachineRecipe recipe, ResourceLocation machine){
        this.machine = machine;
        this.recipe = recipe;
        this.structure = MultiblockStructures.getStructure(machine);
    }

    @Override
    public @NotNull List<List<EntryStack>> getInputEntries() {
        return EntryStack.ofIngredients(recipe.getIngredients());
    }


    @Override
    public @NotNull List<List<EntryStack>> getResultingEntries() {
        return CollectionUtils.map(EntryStack.ofItemStacks(recipe.getItemResults()), Collections::singletonList);
    }

    @Override
    public @NotNull ResourceLocation getRecipeCategory() {
        return REIPlugin.getMachineCategory(machine);
    }

    public MachineRecipe getRecipe() {
        return recipe;
    }
}
