package cn.leomc.multiblockmachine.common.api.recipe;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.architectury.fluid.FluidStack;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;


public class RecipeResult {

    public static final RecipeResult EMPTY = new RecipeResult(IngredientType.EMPTY);

    protected ItemStack item;
    protected long energy;
    protected FluidStack fluid;

    protected IngredientType type;

    private RecipeResult(IngredientType type) {
        this.type = type;
        this.energy = -1;
        this.item = ItemStack.EMPTY;
        this.fluid = FluidStack.empty();
    }


    public static RecipeResult of(long energy) {
        RecipeResult result = new RecipeResult(IngredientType.ENERGY);
        result.energy = energy;
        return result;
    }

    public static RecipeResult of(ItemStack ingredient) {
        RecipeResult result = new RecipeResult(IngredientType.ITEM);
        result.item = ingredient;
        return result;
    }

    public static RecipeResult of(FluidStack fluid) {
        RecipeResult result = new RecipeResult(IngredientType.FLUID);
        result.fluid = fluid;
        return result;
    }

    public static RecipeResult of(CompoundTag tag) {
        IngredientType type = IngredientType.valueOf(tag.getString("type").toUpperCase());
        return switch (type) {
            case ITEM -> RecipeResult.of(ItemStack.of(tag));
            case ENERGY -> RecipeResult.of(tag.getLong("energy"));
            case FLUID -> RecipeResult.of(FluidStack.read(tag.getCompound("fluid")));
            default -> RecipeResult.EMPTY;
        };
    }


    public static RecipeResult of(JsonObject object) {
        IngredientType type = IngredientType.valueOf(object.get("type").getAsString().toUpperCase());
        return switch (type) {
            case ITEM -> RecipeResult.of(ShapedRecipe.itemStackFromJson(object));
            case ENERGY -> RecipeResult.of(object.get("amount").getAsLong());
            case FLUID -> RecipeResult.of(FluidStack.create(Registry.FLUID.get(ResourceLocation.tryParse(object.get("fluid").getAsString())),
                    object.get("amount").getAsLong()));
            default -> RecipeResult.EMPTY;
        };
    }


    public long getEnergy() {
        return energy;
    }

    public FluidStack getFluid() {
        return fluid;
    }

    public ItemStack getItem() {
        return item;
    }

    public IngredientType getType() {
        return type;
    }


    public CompoundTag save(CompoundTag tag) {
        tag.putString("type", type.toString());
        switch (type) {
            case ITEM -> tag.put("item", item.save(new CompoundTag()));
            case ENERGY -> tag.putLong("energy", energy);
            case FLUID -> tag.put("fluid", fluid.write(new CompoundTag()));
        }
        return tag;
    }



}
