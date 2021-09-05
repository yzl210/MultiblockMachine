package cn.leomc.multiblockmachine.forge;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.common.api.recipe.MachineRecipe;
import cn.leomc.multiblockmachine.common.api.recipe.MachineRecipeType;
//import cn.leomc.multiblockmachine.common.config.forge.ForgeConfig;
import cn.leomc.multiblockmachine.common.config.ModConfig;
import me.shedaniel.architectury.platform.forge.EventBuses;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.BiFunction;
import java.util.function.Supplier;

@Mod(MultiblockMachine.MODID)
public class MultiblockMachineForge {

    private MultiblockMachine INSTANCE;

    public MultiblockMachineForge() {
        EventBuses.registerModEventBus(MultiblockMachine.MODID, FMLJavaModLoadingContext.get().getModEventBus());
        INSTANCE = new MultiblockMachine();
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () ->
                ((minecraft, screen) -> AutoConfig.getConfigScreen(ModConfig.class, screen).get()));
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(RecipeSerializer.class, this::onRegisterRecipe);
    }

    public void onRegisterRecipe(RegistryEvent.Register<RecipeSerializer<?>> event){
        Registry.register(Registry.RECIPE_TYPE, MachineRecipeType.ID, MachineRecipeType.INSTANCE);
        event.getRegistry().register(MachineRecipe.Serializer.INSTANCE);
    }

}
