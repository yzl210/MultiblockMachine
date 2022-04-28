package cn.leomc.multiblockmachine.common.api.recipe;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.crafting.Ingredient;


public class RecipeIngredient {

    public static final RecipeIngredient EMPTY = new RecipeIngredient(IngredientType.EMPTY);

    protected Ingredient item;
    protected long energy;
    protected FluidIngredient fluid;

    protected long amount;

    protected IngredientType type;

    private RecipeIngredient(IngredientType type) {
        this.type = type;
        this.energy = -1;
        this.item = Ingredient.EMPTY;
        this.fluid = FluidIngredient.EMPTY;
    }


    public static RecipeIngredient of(long energy) {
        RecipeIngredient result = new RecipeIngredient(IngredientType.ENERGY);
        result.energy = energy;
        result.amount = energy;
        return result;
    }

    public static RecipeIngredient of(Ingredient ingredient, int count) {
        RecipeIngredient result = new RecipeIngredient(IngredientType.ITEM);
        result.item = ingredient;
        result.amount = count;
        return result;
    }

    public static RecipeIngredient of(FluidIngredient fluid, long amount) {
        RecipeIngredient result = new RecipeIngredient(IngredientType.FLUID);
        result.fluid = fluid;
        result.amount = amount;
        return result;
    }

    public static RecipeIngredient of(CompoundTag tag) {
        IngredientType type = IngredientType.valueOf(tag.getString("type").toUpperCase());
        return switch (type) {
            case ITEM -> RecipeIngredient.of(Ingredient.fromJson(JsonParser.parseString(tag.getString("item"))), getCount(tag));
            case ENERGY -> RecipeIngredient.of(tag.getLong("energy"));
            case FLUID -> RecipeIngredient.of(FluidIngredient.of(tag.getCompound("fluid")), tag.getLong("amount"));
            default -> RecipeIngredient.EMPTY;
        };
    }


    public static RecipeIngredient of(JsonObject object) {
        IngredientType type = IngredientType.valueOf(object.get("type").getAsString().toUpperCase());
        JsonElement ingredient = object.get("ingredient");
        return switch (type) {
            case ITEM -> RecipeIngredient.of(Ingredient.fromJson(ingredient), getCount(object));
            case FLUID -> RecipeIngredient.of(FluidIngredient.of(ingredient), object.get("amount").getAsLong());
            case ENERGY -> RecipeIngredient.of(ingredient.getAsJsonObject().get("amount").getAsLong());
            default -> RecipeIngredient.EMPTY;
        };
    }

    private static int getCount(JsonObject object){
        return object.has("count") ? object.get("count").getAsInt() : 1;
    }

    private static int getCount(CompoundTag tag){
        return tag.contains("count") ? tag.getInt("count") : 1;
    }


    public long getEnergy() {
        return energy;
    }

    public FluidIngredient getFluid() {
        return fluid;
    }

    public Ingredient getItem() {
        return item;
    }

    public IngredientType getType() {
        return type;
    }

    public long getAmount() {
        return amount;
    }

    public int getIntAmount() {
        return (int) amount;
    }

    public CompoundTag save(CompoundTag tag) {
        tag.putString("type", type.toString());
        switch (type) {
            case ITEM -> {
                tag.putString("item", item.toJson().toString());
                tag.putInt("count", (int) amount);
            }
            case ENERGY -> tag.putLong("energy", energy);
            case FLUID -> {
                tag.put("fluid", fluid.save());
                tag.putLong("amount", amount);
            }
        }
        return tag;
    }

}
