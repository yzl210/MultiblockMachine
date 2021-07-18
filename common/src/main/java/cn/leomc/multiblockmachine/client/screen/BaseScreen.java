package cn.leomc.multiblockmachine.client.screen;

import cn.leomc.multiblockmachine.client.utils.Textures;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseScreen<C extends AbstractContainerMenu> extends AbstractContainerScreen<C> {

    protected List<Pair<Integer, Integer>> slots = new ArrayList<>();

    public BaseScreen(C menu, Inventory inv, Component titleIn) {
        super(menu, inv, titleIn);
    }


    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
        renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int x, int y) {
        Minecraft.getInstance().font.draw(poseStack, title.getString(), getCenteredOffset(title.getString()), 6, 0x404040);
        Minecraft.getInstance().font.draw(poseStack, inventory.getDisplayName().getString(), 8, imageHeight - 96 + 3, 0x404040);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bind(InventoryMenu.BLOCK_ATLAS);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        blit(poseStack, relX, relY, 0, this.imageWidth, this.imageWidth, Textures.GENERIC_GUI.get());
        if (!slots.isEmpty()) {
            TextureAtlasSprite slot = Textures.SLOT_SMALL.get();
            for (Pair<Integer, Integer> pair : slots) {
                blit(poseStack, leftPos + pair.getFirst(), topPos + pair.getSecond(), 1, slot.getWidth(), slot.getHeight(), slot);
            }
        }
    }

    protected void addSlot(int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            slots.add(Pair.of(x - 1, y - 1));
            x += dx;
        }
    }


    protected void addSlot(int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            addSlot(x, y, horAmount, dx);
            y += dy;
        }
    }

    public int getCenteredOffset(String string) {
        return this.getCenteredOffset(string, imageWidth / 2);
    }

    public int getCenteredOffset(String string, int xPos) {
        return ((xPos * 2) - font.width(string)) / 2;
    }

    public Font getFont() {
        return font;
    }


}
