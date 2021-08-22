package cn.leomc.multiblockmachine.client.screen;

import cn.leomc.multiblockmachine.common.menu.ControllerMenu;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ControllerScreen extends BaseScreen<ControllerMenu> {
    public ControllerScreen(ControllerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int x, int y) {
        super.renderLabels(poseStack, x, y);
        String progress = menu.getBlockEntity().getProgress() + "%";
        Minecraft.getInstance().font.draw(poseStack, progress, getCenteredOffset(progress), 20, 0x404040);
    }
}
