package cn.leomc.multiblockmachine.client.screen.energyslot;

import cn.leomc.multiblockmachine.client.screen.BaseScreen;
import cn.leomc.multiblockmachine.client.utils.Textures;
import cn.leomc.multiblockmachine.common.menu.energyslot.EnergySlotMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.architectury.platform.Platform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import org.lwjgl.opengl.GL11;

public class EnergySlotScreen extends BaseScreen<EnergySlotMenu> {
    public EnergySlotScreen(EnergySlotMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void renderTooltip(PoseStack poseStack, int x, int y) {
        super.renderTooltip(poseStack, x, y);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int x, int y) {
        super.renderLabels(poseStack, x, y);
        Minecraft.getInstance().font.draw(poseStack, menu.getBlockEntity().getEnergyHandler().getEnergy() + (Platform.isForge() ? " FE" : " E"), 8, 30, 0x404040);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int x, int y) {
        super.renderBg(poseStack, partialTicks, x, y);

    }
}
