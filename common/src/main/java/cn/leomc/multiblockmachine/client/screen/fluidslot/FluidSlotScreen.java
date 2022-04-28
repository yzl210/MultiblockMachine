package cn.leomc.multiblockmachine.client.screen.fluidslot;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.client.screen.BaseScreen;
import cn.leomc.multiblockmachine.common.menu.fluidslot.FluidSlotMenu;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;

public class FluidSlotScreen extends BaseScreen<FluidSlotMenu> {
    public FluidSlotScreen(FluidSlotMenu menu, Inventory inv, Component titleIn) {
        super(menu, inv, titleIn);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int x, int y) {
        super.renderLabels(poseStack, x, y);

        Component fluid = new TranslatableComponent("text." + MultiblockMachine.MODID + ".fluid_tank",
                new TranslatableComponent(menu.getBlockEntity().getFluidHandler().getFluid(0).getTranslationKey()),
                menu.getBlockEntity().getFluidHandler().getFluid(0).getAmount());
        Minecraft.getInstance().font.draw(poseStack, fluid.getString(), getCenteredOffset(title.getString()), 30, 0x404040);
    }
}
