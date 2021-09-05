package cn.leomc.multiblockmachine.forge.client.jei;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.client.utils.Textures;
import cn.leomc.multiblockmachine.client.utils.RecipeView;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructure;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructures;
import cn.leomc.multiblockmachine.common.api.recipe.MachineRecipe;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import me.shedaniel.math.Rectangle;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class MachineRecipeCategory implements IRecipeCategory<MachineRecipe> {

    private IGuiHelper helper;
    private ResourceLocation machine;
    private MultiblockStructure structure;

    private HashMap<ResourceLocation, List<Pair<Rectangle, TextureAtlasSprite>>> draws;
    private HashMap<ResourceLocation, List<Pair<Rectangle, List<Component>>>> tooltips;



    public MachineRecipeCategory(IGuiHelper helper, ResourceLocation machine){
        this.helper = helper;
        this.machine = machine;
        this.structure = MultiblockStructures.getStructure(machine);
        this.draws = new HashMap<>();
        this.tooltips = new HashMap<>();
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(MultiblockMachine.MODID, machine.getNamespace() + "_" + machine.getPath());
    }

    @Override
    public Class<? extends MachineRecipe> getRecipeClass() {
        return MachineRecipe.class;
    }

    @Override
    public String getTitle() {
        return getTitleAsTextComponent().getString();
    }

    @Override
    public Component getTitleAsTextComponent() {
        return MultiblockStructures.getStructureName(machine);
    }

    @Override
    public IDrawable getBackground() {
        return new IDrawable() {
            @Override
            public int getWidth() {
                return 160;
            }

            @Override
            public int getHeight() {
                return 144;
            }

            @Override
            public void draw(PoseStack poseStack, int x, int y) {

            }
        };
    }

    @Override
    public IDrawable getIcon() {
        return helper.createDrawableIngredient(structure.getItem());
    }

    @Override
    public void setIngredients(MachineRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutputs(VanillaTypes.ITEM, recipe.getItemResults());
    }

    @Override
    public void draw(MachineRecipe recipe, PoseStack poseStack, double mouseX, double mouseY) {
        Minecraft.getInstance().getTextureManager().bind(InventoryMenu.BLOCK_ATLAS);
        for (Pair<Rectangle, TextureAtlasSprite> pair : draws.get(recipe.getId()))
            GuiComponent.blit(poseStack, pair.getFirst().x, pair.getFirst().y, 0, pair.getFirst().width, pair.getFirst().height, pair.getSecond());
    }

    @Override
    public List<Component> getTooltipStrings(MachineRecipe recipe, double mouseX, double mouseY) {
        for (Pair<Rectangle, List<Component>> pair : tooltips.get(recipe.getId()))
            if(pair.getFirst().contains(mouseX, mouseY))
                return pair.getSecond();


        return Collections.emptyList();
    }

    @Override
    public void setRecipe(IRecipeLayout layout, MachineRecipe recipe, IIngredients ingredients) {
        draws.put(recipe.getId(), new ArrayList<>());
        tooltips.put(recipe.getId(), new ArrayList<>());
        IGuiItemStackGroup item = layout.getItemStacks();
        RecipeView.apply(new RecipeView.IGuiAccessor() {
            int i = 0;
            @Override
            public void addInputSlot(Ingredient ingredient, int x, int y) {
                addSlot(x, y);
                item.init(i, true, x, y);
                item.set(i, Arrays.asList(ingredient.getItems()));
                i++;
            }

            @Override
            public void addOutputSlot(ItemStack itemStack, int x, int y) {
                addSlot(x, y);
                if(itemStack.isEmpty())
                    return;
                item.init(i, false, x, y);
                item.set(i, itemStack);
                i++;
            }

            private void addSlot(int x, int y){
                TextureAtlasSprite sprite = Textures.SLOT_SMALL.get();
                draws.get(recipe.getId()).add(Pair.of(new Rectangle(x, y, sprite.getWidth(), sprite.getHeight()), sprite));
            }

            @Override
            public void addCustom(int x, int y, int width, int height, TextureAtlasSprite sprite, List<Component> tooltips) {
                Rectangle rectangle = new Rectangle(x, y, width, height);
                draws.get(recipe.getId()).add(Pair.of(rectangle, sprite));
                MachineRecipeCategory.this.tooltips.get(recipe.getId()).add(Pair.of(rectangle, tooltips));
            }
        }, recipe);

    }
}
