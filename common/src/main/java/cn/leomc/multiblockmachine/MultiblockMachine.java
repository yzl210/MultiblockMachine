package cn.leomc.multiblockmachine;

import cn.leomc.multiblockmachine.client.screen.ControllerScreen;
import cn.leomc.multiblockmachine.client.screen.itemslot.ItemSlotScreen;
import cn.leomc.multiblockmachine.client.utils.Textures;
import cn.leomc.multiblockmachine.common.registry.ContainerMenuRegistry;
import cn.leomc.multiblockmachine.common.registry.ModRegistry;
import me.shedaniel.architectury.event.events.TextureStitchEvent;
import me.shedaniel.architectury.event.events.client.ClientLifecycleEvent;
import me.shedaniel.architectury.platform.Platform;
import me.shedaniel.architectury.registry.MenuRegistry;
import me.shedaniel.architectury.utils.Env;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

public class MultiblockMachine {

    public static final String MODID = "multiblockmachine";

    public static final Logger LOGGER = LogManager.getLogger();

    public MultiblockMachine() {
        ModRegistry.register();
        if (Platform.getEnvironment() == Env.CLIENT) {
            ClientLifecycleEvent.CLIENT_SETUP.register(this::onClientSetup);
            TextureStitchEvent.PRE.register(this::onPreTextureStitch);
            TextureStitchEvent.POST.register(this::onPostTextureStitch);
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
    }


}
