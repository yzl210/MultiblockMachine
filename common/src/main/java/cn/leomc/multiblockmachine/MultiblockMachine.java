package cn.leomc.multiblockmachine;

import cn.leomc.multiblockmachine.client.renderer.InstructionBlockEntityRenderer;
import cn.leomc.multiblockmachine.client.screen.ControllerScreen;
import cn.leomc.multiblockmachine.client.screen.energyslot.EnergySlotScreen;
import cn.leomc.multiblockmachine.client.screen.fluidslot.FluidSlotScreen;
import cn.leomc.multiblockmachine.client.screen.itemslot.ItemSlotScreen;
import cn.leomc.multiblockmachine.client.utils.Textures;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructure;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructures;
import cn.leomc.multiblockmachine.common.api.recipe.MachineRecipe;
import cn.leomc.multiblockmachine.common.api.recipe.MachineRecipeType;
import cn.leomc.multiblockmachine.common.config.ModConfig;
import cn.leomc.multiblockmachine.common.network.NetworkHandler;
import cn.leomc.multiblockmachine.common.registry.BlockEntityRegistry;
import cn.leomc.multiblockmachine.common.registry.ContainerMenuRegistry;
import cn.leomc.multiblockmachine.common.registry.ModRegistry;
import me.shedaniel.architectury.event.events.RecipeUpdateEvent;
import me.shedaniel.architectury.event.events.TextureStitchEvent;
import me.shedaniel.architectury.event.events.client.ClientLifecycleEvent;
import me.shedaniel.architectury.platform.Platform;
import me.shedaniel.architectury.registry.BlockEntityRenderers;
import me.shedaniel.architectury.registry.MenuRegistry;
import me.shedaniel.architectury.registry.ReloadListeners;
import me.shedaniel.architectury.utils.Env;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.crafting.RecipeManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

/*
    TODO:
     1. textures
     2. upgrades for slots(configs)
     3. Structure display button
 */

public class MultiblockMachine {

    public static final String MODID = "multiblockmachine";

    public static final Logger LOGGER = LogManager.getLogger();

    public static ModConfig CONFIG;

    public MultiblockMachine() {
        CONFIG = AutoConfig.register(ModConfig.class, PartitioningSerializer.wrap(Platform.isForge() ? Toml4jConfigSerializer::new : JanksonConfigSerializer::new)).getConfig();
        ModRegistry.register();
        NetworkHandler.register();
        if (Platform.getEnvironment() == Env.CLIENT) {
            ClientLifecycleEvent.CLIENT_SETUP.register(this::onClientSetup);
            TextureStitchEvent.PRE.register(this::onPreTextureStitch);
            TextureStitchEvent.POST.register(this::onPostTextureStitch);
            RecipeUpdateEvent.EVENT.register(this::onRecipeUpdate);
        }
        ReloadListeners.registerReloadListener(PackType.SERVER_DATA, MultiblockStructures.INSTANCE);
    }

    private void onRecipeUpdate(RecipeManager manager) {
        for (MachineRecipe recipe : manager.getAllRecipesFor(MachineRecipeType.INSTANCE)) {
            MultiblockStructure structure = MultiblockStructures.getStructure(recipe.getMachineID());
            if (structure == null)
                continue;
            structure.addRecipe(recipe.getId(), recipe);
        }
    }


    @Environment(EnvType.CLIENT)
    private void onPreTextureStitch(TextureAtlas atlasTexture, Consumer<ResourceLocation> spriteAdder) {
        if (atlasTexture.location() == InventoryMenu.BLOCK_ATLAS) {
            Textures.REGISTRIES.forEach(spriteAdder);
        }
    }

    @Environment(EnvType.CLIENT)
    private void onPostTextureStitch(TextureAtlas atlasTexture) {
        if (atlasTexture.location() == InventoryMenu.BLOCK_ATLAS) {
            Textures.TEXTURE_MAP.clear();
            Textures.REGISTRIES.forEach(rl -> Textures.TEXTURE_MAP.put(rl, atlasTexture.getSprite(rl)));
        }
    }

    @Environment(EnvType.CLIENT)
    private void onClientSetup(Minecraft minecraft) {
        MenuRegistry.registerScreenFactory(ContainerMenuRegistry.CONTROLLER.get(), ControllerScreen::new);
        MenuRegistry.registerScreenFactory(ContainerMenuRegistry.ITEM_SLOT.get(), ItemSlotScreen::new);
        MenuRegistry.registerScreenFactory(ContainerMenuRegistry.ENERGY_SLOT.get(), EnergySlotScreen::new);
        MenuRegistry.registerScreenFactory(ContainerMenuRegistry.FLUID_SLOT.get(), FluidSlotScreen::new);
        BlockEntityRenderers.registerRenderer(BlockEntityRegistry.INSTRUCTION_BLOCK.get(), InstructionBlockEntityRenderer::new);
    }

}
