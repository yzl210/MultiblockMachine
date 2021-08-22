package cn.leomc.multiblockmachine.client.screen.energyslot;

import cn.leomc.multiblockmachine.client.screen.BaseScreen;
import cn.leomc.multiblockmachine.common.menu.energyslot.EnergySlotMenu;
import com.mojang.blaze3d.vertex.PoseStack;
import me.shedaniel.architectury.platform.Platform;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class EnergySlotScreen extends BaseScreen<EnergySlotMenu> {
    public EnergySlotScreen(EnergySlotMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void renderTooltip(PoseStack poses, int x, int y) {
        super.renderTooltip(poses, x, y);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int x, int y) {
        super.renderLabels(poseStack, x, y);
        Minecraft.getInstance().font.draw(poseStack, String.valueOf(menu.getBlockEntity().getEnergyClient().doubleValue).replace(Platform.isForge() ? ".0" : "", "") + (Platform.isForge() ? " FE" : " E"), 8, 30, 0x404040);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int x, int y) {
        super.renderBg(poseStack, partialTicks, x, y);
    }
}
