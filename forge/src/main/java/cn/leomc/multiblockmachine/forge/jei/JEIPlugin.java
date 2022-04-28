package cn.leomc.multiblockmachine.forge.jei;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructure;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructures;
import cn.leomc.multiblockmachine.common.api.recipe.MachineInstructionRecipe;
import cn.leomc.multiblockmachine.common.api.recipe.MachineRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(MultiblockMachine.MODID, "jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new MachineInstructionCategory(registration.getJeiHelpers().getGuiHelper()));
        for(ResourceLocation machine : MultiblockStructures.INSTANCE.MACHINES.keySet())
            registration.addRecipeCategories(new MachineRecipeCategory(registration.getJeiHelpers().getGuiHelper(), machine));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        for (MultiblockStructure machine : MultiblockStructures.INSTANCE.MACHINES.values()) {
            registration.addRecipes(RecipeType.create(MultiblockMachine.MODID, machine.getId().getNamespace() + "_" + machine.getId().getPath(), MachineRecipe.class), new ArrayList<>(machine.getRecipes()));
            registration.addRecipes(RecipeType.create(MachineInstructionCategory.ID.getNamespace(), MachineInstructionCategory.ID.getPath(), MachineInstructionRecipe.class), Collections.singletonList(new MachineInstructionRecipe(machine.getId(), machine.getId())));
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        for(ResourceLocation machine : MultiblockStructures.INSTANCE.MACHINES.keySet()) {
            MultiblockStructure structure = MultiblockStructures.getStructure(machine);
            registration.addRecipeCatalyst(structure.getItem(), RecipeType.create(MultiblockMachine.MODID, machine.getNamespace() + "_" + machine.getPath(), MachineRecipe.class));
        }
    }
}
