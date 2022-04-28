package cn.leomc.multiblockmachine.forge.jei;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.client.utils.RecipeView;
import cn.leomc.multiblockmachine.common.api.recipe.MachineInstructionRecipe;
import cn.leomc.multiblockmachine.common.registry.ItemRegistry;
import com.mojang.blaze3d.MethodsReturnNonnullByDefault;
import com.mojang.blaze3d.vertex.PoseStack;
import me.shedaniel.math.Dimension;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;


public class MachineInstructionCategory extends BaseCategory<MachineInstructionRecipe> {

    public static final ResourceLocation ID = new ResourceLocation(MultiblockMachine.MODID, "instruction");


    public MachineInstructionCategory(IGuiHelper helper){
        super(helper);
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public Class<? extends MachineInstructionRecipe> getRecipeClass() {
        return MachineInstructionRecipe.class;
    }

    @Override
    public RecipeType<MachineInstructionRecipe> getRecipeType() {
        return RecipeType.create(ID.getNamespace(), ID.getPath(), MachineInstructionRecipe.class);
    }

    @Override
    public Component getTitle() {
        return new TranslatableComponent("text.multiblockmachine.build_instruction_category");
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
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ItemRegistry.MACHINE_ITEM.get()));
    }

    @Override
    protected void setRecipe(IRecipeLayoutBuilder builder, MachineInstructionRecipe recipe, IFocusGroup focuses, RecipeView.IGuiAccessor accessor) {
        draws.put(recipe.getId(), new ArrayList<>());
        tooltips.put(recipe.getId(), new ArrayList<>());
        IDrawable drawable = getBackground();
        RecipeView.instruction(accessor, recipe, new Dimension(drawable.getWidth(), drawable.getHeight()));
    }
}
