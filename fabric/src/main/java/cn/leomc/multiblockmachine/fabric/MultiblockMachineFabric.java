package cn.leomc.multiblockmachine.fabric;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.common.api.recipe.MachineRecipe;
import cn.leomc.multiblockmachine.common.api.recipe.MachineRecipeType;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;

public class MultiblockMachineFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        new MultiblockMachine();
        Registry.register(Registry.RECIPE_TYPE, MachineRecipeType.ID, MachineRecipeType.INSTANCE);
        Registry.register(Registry.RECIPE_SERIALIZER, MachineRecipeType.ID, MachineRecipe.Serializer.INSTANCE);
    }
}
