package cn.leomc.multiblockmachine.forge.jei;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.client.utils.RecipeView;
import cn.leomc.multiblockmachine.client.utils.Textures;
import cn.leomc.multiblockmachine.common.api.recipe.FluidIngredient;
import cn.leomc.multiblockmachine.common.api.recipe.RecipeIngredient;
import cn.leomc.multiblockmachine.common.api.recipe.RecipeResult;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Matrix4f;
import dev.architectury.hooks.fluid.forge.FluidStackHooksForge;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Triple;

import java.util.*;

public abstract class BaseCategory<T extends Recipe> implements IRecipeCategory<T> {


    protected IGuiHelper helper;


    protected HashMap<ResourceLocation, List<Pair<Rectangle, TextureAtlasSprite>>> draws;
    protected HashMap<ResourceLocation, List<Pair<Rectangle, List<Component>>>> tooltips;
    protected HashMap<ResourceLocation, List<Pair<Rectangle, Pair<Component, Runnable>>>> buttons;
    protected HashMap<ResourceLocation, List<Pair<Point, Triple<Component, Integer, Boolean>>>> texts;



    public BaseCategory(IGuiHelper helper){
        this.helper = helper;
        this.draws = new HashMap<>();
        this.tooltips = new HashMap<>();
        this.buttons = new HashMap<>();
        this.texts = new HashMap<>();
    }

    @Override
    public boolean handleInput(T recipe, double mouseX, double mouseY, InputConstants.Key input) {
        if(input.getType() == InputConstants.Type.MOUSE && buttons.containsKey(recipe.getId()))
            for (Pair<Rectangle, Pair<Component, Runnable>> pair : buttons.get(recipe.getId())) {
                if(pair.getFirst().contains(mouseX, mouseY)){
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    pair.getSecond().getSecond().run();
                    return true;
                }
            }
        return false;
    }


    @Override
    public List<Component> getTooltipStrings(T recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        for (Pair<Rectangle, List<Component>> pair : tooltips.get(recipe.getId()))
            if(pair.getFirst().contains(mouseX, mouseY))
                return pair.getSecond();
        return Collections.emptyList();
    }

    @Override
    public void draw(T recipe, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        if(draws.containsKey(recipe.getId()))
            for (Pair<Rectangle, TextureAtlasSprite> pair : draws.get(recipe.getId()))
                GuiComponent.blit(poseStack, pair.getFirst().getX(), pair.getFirst().getY(), 0, pair.getFirst().getWidth(), pair.getFirst().getHeight(), pair.getSecond());
        if(buttons.containsKey(recipe.getId()))
            for (Pair<Rectangle, Pair<Component, Runnable>> pair : buttons.get(recipe.getId())) {
                boolean mouse =  pair.getFirst().contains(new Point(mouseX, mouseY));
                drawButton(poseStack, pair.getFirst().getX(), pair.getFirst().getY(), pair.getFirst().getWidth(), pair.getFirst().getHeight(), mouse ? 4 : 1);
                GuiComponent.drawCenteredString(poseStack, Minecraft.getInstance().font, pair.getSecond().getFirst(), pair.getFirst().getX() + pair.getFirst().getWidth() / 2,
                        pair.getFirst().getY() + (pair.getFirst().getHeight() - 8) / 2, mouse ? 16777120 : 14737632);
            }
        if(texts.containsKey(recipe.getId()))
            for (Pair<Point, Triple<Component, Integer, Boolean>> pair : texts.get(recipe.getId())) {
                if (pair.getSecond().getRight())
                    Minecraft.getInstance().font.drawShadow(poseStack, pair.getSecond().getLeft(), pair.getFirst().getX(), pair.getFirst().getY(), pair.getSecond().getMiddle());
                else
                    Minecraft.getInstance().font.draw(poseStack, pair.getSecond().getLeft(), pair.getFirst().getX(), pair.getFirst().getY(), pair.getSecond().getMiddle());
            }
    }

    private static void drawButton(PoseStack poseStack, int x, int y, int width, int height, int textureOffset) {
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(770, 771, 1, 0);
        RenderSystem.blendFunc(770, 771);

        Matrix4f matrix = poseStack.last().pose();

        TextureAtlasSprite button = Textures.BUTTON.get();

        float uScale = (float)button.getWidth() / (button.getU1() - button.getU0());
        float vScale = (float)button.getHeight() / (button.getV1() - button.getV0());

        int uFloor = (int) Math.floor(uScale);
        int vFloor = (int) Math.floor(vScale);


        blit(matrix, x, y, 0, 0, textureOffset * 80, 8, 8, vFloor, uFloor, button);
        blit(matrix, x + width - 8, y, 0, 248, textureOffset * 80, 8, 8, vFloor, uFloor, button);
        blit(matrix, x, y + height - 8, 0, 0, textureOffset * 80 + 72, 8, 8, vFloor, uFloor, button);
        blit(matrix, x + width - 8, y + height - 8, 0, 248, textureOffset * 80 + 72, 8, 8, vFloor, uFloor, button);


        blit1(matrix, x + 8, x + width - 8, y, y + 8, 0, (8) / uScale, (width - 8) / uScale, (textureOffset * 80)  / vScale, (textureOffset * 80 + 8)  / vScale, button);
        blit1(matrix, x + 8, x + width - 8, y + height - 8, y + height, 0, (8) / uScale, (width - 8) / uScale, (textureOffset * 80 + 72)  / vScale, (textureOffset * 80 + 80)  / vScale, button);
        blit1(matrix, x, x + 8, y + 8, y + height - 8, 0, 0, (8) / uScale, (textureOffset * 80 + 8)  / vScale, (textureOffset * 80 + height - 8)  / vScale, button);
        blit1(matrix, x + width - 8, x + width, y + 8, y + height - 8, 0, (248) / uScale, (256) / uScale, (textureOffset * 80 + 8)  / vScale, (textureOffset * 80 + height - 8)  / vScale, button);
        blit1(matrix, x + 8, x + width - 8, y + 8, y + height - 8, 0, (8) / uScale, (248) / uScale, (textureOffset * 80 + 8)  / vScale, (textureOffset * 80 + 72)  / vScale, button);
        RenderSystem.disableBlend();
    }

    public static void blit(Matrix4f matrix4f, int x, int y, int blitOffset, float uOffset, float vOffset, int uWidth, int vHeight, int textureHeight, int textureWidth, TextureAtlasSprite sprite) {
        blit(matrix4f, x, x + uWidth, y, y + vHeight, blitOffset, uWidth, vHeight, uOffset, vOffset, textureWidth, textureHeight, sprite);
    }

    private static void blit(Matrix4f matrix4f, int x1, int x2, int y1, int y2, int blitOffset, int uWidth, int vHeight, float uOffset, float vOffset, int textureWidth, int textureHeight, TextureAtlasSprite sprite) {
        blit(matrix4f, x1, x2, y1, y2, blitOffset, sprite.getU0() + ((uOffset + 0.0F) / (float)textureWidth), sprite.getU0() + ((uOffset + (float)uWidth) / (float)textureWidth),
                sprite.getV0() + ((vOffset + 0.0F) / (float)textureHeight), sprite.getV0() + ((vOffset + (float)vHeight) / (float)textureHeight));
    }
    private static void blit1(Matrix4f matrix4f, int x0, int x1, int y1, int y0, int z, float minU, float maxU, float minV, float maxV, TextureAtlasSprite sprite) {
        blit(matrix4f, x0, x1, y1, y0, z, sprite.getU0() + minU, sprite.getU0() + maxU, sprite.getV0() + minV, sprite.getV0() + maxV);
    }

    private static void blit(Matrix4f matrix4f, int x0, int x1, int y1, int y0, int z, float minU, float maxU, float minV, float maxV) {
        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.vertex(matrix4f, x0, y0, z).uv(minU, maxV).endVertex();
        bufferBuilder.vertex(matrix4f, x1, y0, z).uv(maxU, maxV).endVertex();
        bufferBuilder.vertex(matrix4f, x1, y1, z).uv(maxU, minV).endVertex();
        bufferBuilder.vertex(matrix4f, x0, y1, z).uv(minU, minV).endVertex();
        bufferBuilder.end();
        BufferUploader.end(bufferBuilder);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, T recipe, IFocusGroup focuses) {

        RecipeView.IGuiAccessor accessor = new RecipeView.IGuiAccessor() {
            @Override
            public void addInputSlot(RecipeIngredient ingredient, int x, int y) {
                addSlot(x, y);
                if(ingredient.getType().isEmpty())
                    return;
                builder
                        .addSlot(RecipeIngredientRole.INPUT, x + 1, y + 1)
                        .addItemStacks(Arrays.stream(ingredient.getItem().getItems()).peek(itemStack -> itemStack.setCount(ingredient.getIntAmount())).toList());
            }

            @Override
            public void addOutputSlot(RecipeResult result, int x, int y) {
                addSlot(x, y);
                if(result.getType().isEmpty())
                    return;
                builder
                        .addSlot(RecipeIngredientRole.OUTPUT, x + 1, y + 1)
                        .addItemStack(result.getItem());
            }

            @Override
            public void addFluidInputSlot(RecipeIngredient ingredient, int x, int y) {
                addSlot(x, y);
                if(ingredient.getType().isEmpty())
                    return;
                builder
                        .addSlot(RecipeIngredientRole.INPUT, x + 1, y + 1)
                        .addIngredients(ForgeTypes.FLUID_STACK, ingredient.getFluid().getFluids().stream().map(fluid -> new FluidStack(fluid, ingredient.getIntAmount())).toList())
                        .setFluidRenderer(ingredient.getIntAmount(), false, 16, 16);
            }

            @Override
            public void addFluidOutputSlot(RecipeResult result, int x, int y) {
                addSlot(x, y);
                if(result.getType().isEmpty())
                    return;
                builder
                        .addSlot(RecipeIngredientRole.OUTPUT, x + 1, y + 1)
                        .addIngredient(ForgeTypes.FLUID_STACK, FluidStackHooksForge.toForge(result.getFluid()))
                        .setFluidRenderer((int) result.getFluid().getAmount(), false, 16, 16);
            }

            private void addSlot(int x, int y){
                TextureAtlasSprite sprite = Textures.SLOT_SMALL.get();
                draws.get(recipe.getId()).add(Pair.of(new Rectangle(x, y, sprite.getWidth(), sprite.getHeight()), sprite));
            }

            @Override
            public void addCustom(int x, int y, int width, int height, TextureAtlasSprite sprite, List<Component> tooltips) {
                if(!draws.containsKey(recipe.getId()))
                    draws.put(recipe.getId(), new ArrayList<>());
                Rectangle rectangle = new Rectangle(x, y, width, height);
                draws.get(recipe.getId()).add(Pair.of(rectangle, sprite));
                BaseCategory.this.tooltips.get(recipe.getId()).add(Pair.of(rectangle, tooltips));
            }

            @Override
            public void addButton(int x, int y, int width, int height, Component text, Runnable onClick) {
                if(!buttons.containsKey(recipe.getId()))
                    buttons.put(recipe.getId(), new ArrayList<>());
                Rectangle rectangle = new Rectangle(x, y, width, height);
                buttons.get(recipe.getId()).add(Pair.of(rectangle, Pair.of(text, onClick)));
            }

            @Override
            public void addText(int x, int y, int color, Component text, boolean shadow) {
                if(!texts.containsKey(recipe.getId()))
                    texts.put(recipe.getId(), new ArrayList<>());
                Point point = new Point(x, y);
                texts.get(recipe.getId()).add(Pair.of(point, Triple.of(text, color, shadow)));
            }
        };

        setRecipe(builder, recipe, focuses, accessor);
    }

    protected abstract void setRecipe(IRecipeLayoutBuilder builder, T recipe, IFocusGroup focuses, RecipeView.IGuiAccessor accessor);

}
