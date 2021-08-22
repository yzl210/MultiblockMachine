package cn.leomc.multiblockmachine.common.api.recipe;

import cn.leomc.multiblockmachine.common.api.DoubleLong;
import me.shedaniel.architectury.fluid.FluidStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class RecipeResult {

    public static final RecipeResult EMPTY = new RecipeResult(ResultType.EMPTY);

    protected ItemStack item;
    protected DoubleLong energy;
    protected FluidStack fluid;

    protected ResultType type;

    private RecipeResult(ResultType type){
        this.type = type;
    }



    public static RecipeResult of(DoubleLong energy){
        RecipeResult result = new RecipeResult(ResultType.ENERGY);
        result.energy = DoubleLong.of(energy);
        return result;
    }

    public static RecipeResult of(ItemStack item){
        RecipeResult result = new RecipeResult(ResultType.ITEM);
        result.item = item.copy();
        return result;
    }

    public static RecipeResult of(FluidStack fluid){
        RecipeResult result = new RecipeResult(ResultType.FLUID);
        result.fluid = fluid;
        return result;
    }

    public static RecipeResult of(CompoundTag tag){
        ResultType type = ResultType.valueOf(tag.getString("type"));
        switch (type){
            case ITEM:
                return RecipeResult.of(ItemStack.of(tag.getCompound("item")));
            case ENERGY:
                return RecipeResult.of(DoubleLong.of((tag.getDouble("energy"))));
            case FLUID:
                return RecipeResult.of(FluidStack.read(tag.getCompound("fluid")));
        }
        return RecipeResult.EMPTY;
    }


    public DoubleLong getEnergy() {
        return energy;
    }

    public FluidStack getFluid() {
        return fluid;
    }

    public ItemStack getItem() {
        return item;
    }

    public ResultType getType() {
        return type;
    }


    public CompoundTag save(CompoundTag tag){
        tag.putString("type", type.toString());
        switch (type){
            case ITEM:
                tag.put("item", item.save(new CompoundTag()));
                break;
            case ENERGY:
                tag.putDouble("energy", energy.doubleValue);
                break;
            case FLUID:
                tag.put("fluid", fluid.write(new CompoundTag()));
        }
        return tag;
    }

    public enum ResultType{
        ITEM,
        ENERGY,
        FLUID,
        EMPTY;
    }

}
