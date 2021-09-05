package cn.leomc.multiblockmachine.fabric.client.compat.rei;

import com.mojang.blaze3d.vertex.PoseStack;
import me.shedaniel.math.Dimension;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.widgets.BaseWidget;
import me.shedaniel.rei.api.widgets.Tooltip;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.InventoryMenu;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomWidget extends BaseWidget<CustomWidget> {

    private Rectangle bounds;
    private TextureAtlasSprite sprite;
    private List<Component> tooltips;

    public CustomWidget(int x, int y, int width, int height, TextureAtlasSprite sprite){
        this.bounds = new Rectangle(new Point(x, y), new Dimension(width, height));
        this.sprite = sprite;
        this.tooltips = new ArrayList<>();
    }

            @Override
    public @NotNull Rectangle getBounds() {
        return bounds;
    }

    public CustomWidget tooltip(Component component){
        tooltips.add(component);
        return this;
    }

    public CustomWidget tooltips(List<Component> components){
        tooltips.addAll(components);
        return this;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        Minecraft.getInstance().getTextureManager().bind(InventoryMenu.BLOCK_ATLAS);
        GuiComponent.blit(poseStack, bounds.x, bounds.y, 0, bounds.width, bounds.height, sprite);
        if(!tooltips.isEmpty() && containsMouse(mouseX, mouseY))
            Tooltip.create(new Point(mouseX, mouseY), tooltips).queue();
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return Collections.emptyList();
    }
}
