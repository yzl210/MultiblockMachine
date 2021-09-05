package cn.leomc.multiblockmachine.forge.client.jei;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructure;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructures;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(MultiblockMachine.MODID, "jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        for(ResourceLocation machine : MultiblockStructures.INSTANCE.MACHINES.keySet()){
            registration.addRecipeCategories(new MachineRecipeCategory(registration.getJeiHelpers().getGuiHelper(), machine));
        }
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        for (MultiblockStructure machine : MultiblockStructures.INSTANCE.MACHINES.values())
            registration.addRecipes(machine.getRecipes(), new ResourceLocation(MultiblockMachine.MODID, machine.getId().getNamespace() + "_" + machine.getId().getPath()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        for(ResourceLocation machine : MultiblockStructures.INSTANCE.MACHINES.keySet()) {
            MultiblockStructure structure = MultiblockStructures.getStructure(machine);
            registration.addRecipeCatalyst(structure.getItem(), new ResourceLocation(MultiblockMachine.MODID, machine.getNamespace() + "_" + machine.getPath()));
        }
    }
}
