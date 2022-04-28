package cn.leomc.multiblockmachine.fabric.compat.rei;

import cn.leomc.multiblockmachine.common.api.recipe.FluidIngredient;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.fluid.FluidStack;
import me.shedaniel.clothconfig2.api.animator.NumberAnimator;
import me.shedaniel.math.Dimension;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.BaseWidget;
import me.shedaniel.rei.api.client.gui.widgets.Panel;
import me.shedaniel.rei.api.client.gui.widgets.Slot;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.common.entry.EntryStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.Fluid;

import java.text.NumberFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class FluidSlot extends BaseWidget<FluidSlot> {

    private static final NumberFormat FORMAT = NumberFormat.getInstance();

    private final Rectangle bounds;
    private final FluidIngredient fluid;
    private final long amount;
    private final Component tooltip;

    public FluidSlot(int x, int y, int width, int height, FluidIngredient fluid, long amount){
        this.bounds = new Rectangle(new Point(x, y), new Dimension(width, height));
        this.fluid = fluid;
        this.amount = amount;
        this.tooltip = new TextComponent(FORMAT.format(amount) + "").withStyle(ChatFormatting.GRAY);
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }


    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        if(containsMouse(mouseX, mouseY))
            Tooltip.create(new Point(mouseX, mouseY), tooltip).queue();
    }



    @Override
    public List<? extends GuiEventListener> children() {
        return Collections.emptyList();
    }
}
