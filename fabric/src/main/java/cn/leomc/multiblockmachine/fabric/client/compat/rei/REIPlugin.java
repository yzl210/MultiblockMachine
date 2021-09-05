package cn.leomc.multiblockmachine.fabric.client.compat.rei;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructure;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructures;
import cn.leomc.multiblockmachine.common.api.recipe.MachineRecipe;
import me.shedaniel.rei.api.EntryRegistry;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;

import java.util.function.Predicate;

public class REIPlugin implements REIPluginV0 {

    public static ResourceLocation ID = new ResourceLocation(MultiblockMachine.MODID, "rei");

    @Override
    public ResourceLocation getPluginIdentifier() {
        return ID;
    }

    @Override
    public void registerPluginCategories(RecipeHelper helper) {
        MultiblockMachine.LOGGER.info("adding category");
        for(ResourceLocation machine : MultiblockStructures.INSTANCE.MACHINES.keySet()) {
            MultiblockMachine.LOGGER.info(machine.toString());
            helper.registerCategory(new MachineRecipeCategory(machine));
        }
    }

    @Override
    public void registerRecipeDisplays(RecipeHelper helper) {
        for(ResourceLocation machine : MultiblockStructures.INSTANCE.MACHINES.keySet()) {
            Predicate<Recipe> filter = recipe -> recipe instanceof MachineRecipe && ((MachineRecipe) recipe).getMachineID().equals(machine);
            helper.registerRecipes(getMachineCategory(machine), filter, recipe -> new MachineRecipeDisplay((MachineRecipe) recipe, machine));
        }
    }

    @Override
    public void registerOthers(RecipeHelper helper) {
        for(ResourceLocation machine : MultiblockStructures.INSTANCE.MACHINES.keySet()) {
            MultiblockStructure structure = MultiblockStructures.getStructure(machine);
            helper.registerWorkingStations(getMachineCategory(machine), EntryStack.create(structure.getItem()));
            helper.removeAutoCraftButton(getMachineCategory(machine));
        }
    }

    public static ResourceLocation getMachineCategory(ResourceLocation machine){
        return new ResourceLocation(MultiblockMachine.MODID, machine.getNamespace() + "_" + machine.getPath());
    }

}
