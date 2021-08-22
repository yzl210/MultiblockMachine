package cn.leomc.multiblockmachine.forge;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.common.api.recipe.MachineRecipe;
import cn.leomc.multiblockmachine.common.api.recipe.MachineRecipeType;
import cn.leomc.multiblockmachine.common.config.forge.ForgeConfig;
import me.shedaniel.architectury.platform.forge.EventBuses;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MultiblockMachine.MODID)
public class MultiblockMachineForge {
    public MultiblockMachineForge() {
        ForgeConfig.register();
        EventBuses.registerModEventBus(MultiblockMachine.MODID, FMLJavaModLoadingContext.get().getModEventBus());
        new MultiblockMachine();
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(RecipeSerializer.class, this::onRegisterRecipe);
    }

    public void onRegisterRecipe(RegistryEvent.Register<RecipeSerializer<?>> event){
        Registry.register(Registry.RECIPE_TYPE, MachineRecipeType.ID, MachineRecipeType.INSTANCE);
        event.getRegistry().register(MachineRecipe.Serializer.INSTANCE);
    }

}
