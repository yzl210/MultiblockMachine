package cn.leomc.multiblockmachine.forge.jei;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.client.utils.RecipeView;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructure;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructures;
import cn.leomc.multiblockmachine.common.api.recipe.MachineRecipe;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;


public class MachineRecipeCategory extends BaseCategory<MachineRecipe> {

    private ResourceLocation machine;
    private MultiblockStructure structure;


    public MachineRecipeCategory(IGuiHelper helper, ResourceLocation machine){
        super(helper);
        this.machine = machine;
        this.structure = MultiblockStructures.getStructure(machine);
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
    public RecipeType<MachineRecipe> getRecipeType() {
        return RecipeType.create(MultiblockMachine.MODID, machine.getNamespace() + "_" + machine.getPath(), MachineRecipe.class);
    }

    @Override
    public Component getTitle() {
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
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, structure.getItem());
    }


    @Override
    protected void setRecipe(IRecipeLayoutBuilder builder, MachineRecipe recipe, IFocusGroup focuses, RecipeView.IGuiAccessor accessor) {
        draws.put(recipe.getId(), new ArrayList<>());
        tooltips.put(recipe.getId(), new ArrayList<>());
        RecipeView.recipe(accessor, recipe);
    }

}
