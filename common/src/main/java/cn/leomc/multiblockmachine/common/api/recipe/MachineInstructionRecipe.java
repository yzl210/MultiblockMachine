package cn.leomc.multiblockmachine.common.api.recipe;

import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructure;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class MachineInstructionRecipe implements Recipe<Container> {

    private ResourceLocation id;
    private ResourceLocation machine;
    private MultiblockStructure structure;

    public MachineInstructionRecipe(ResourceLocation id, ResourceLocation machine){
        this.id = id;
        this.machine = machine;
        this.structure = MultiblockStructures.getStructure(machine);
    }

    public ResourceLocation getMachine() {
        return machine;
    }

    public MultiblockStructure getStructure() {
        return structure;
    }

    @Override
    public boolean matches(Container container, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(Container container) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int i, int j) {
        return false;
    }

    @Override
    public ItemStack getResultItem() {
        return null;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return MachineInstructionRecipeType.INSTANCE;
    }
}
