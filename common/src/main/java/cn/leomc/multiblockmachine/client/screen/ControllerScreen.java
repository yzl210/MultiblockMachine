package cn.leomc.multiblockmachine.client.screen;

import cn.leomc.multiblockmachine.common.menu.ControllerMenu;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import java.util.Locale;

public class ControllerScreen extends BaseScreen<ControllerMenu> {
    public ControllerScreen(ControllerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int x, int y) {
        super.renderLabels(poseStack, x, y);
        String progress = I18n.get("text.multiblockmachine.controller.progress", menu.getBlockEntity().getProgress() + "%");
        Minecraft.getInstance().font.draw(poseStack, progress, getCenteredOffset(progress), 20, 0x404040);
        String status = I18n.get("text.multiblockmachine.controller.status",
                I18n.get("text.multiblockmachine.controller.status." + menu.getBlockEntity().getStatus().name().toLowerCase(Locale.ROOT)));
        Minecraft.getInstance().font.draw(poseStack, status, getCenteredOffset(status), 40, 0x404040);
    }
}
