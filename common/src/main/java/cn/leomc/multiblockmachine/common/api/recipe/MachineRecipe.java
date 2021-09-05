package cn.leomc.multiblockmachine.common.api.recipe;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.common.api.DoubleLong;
import cn.leomc.multiblockmachine.common.api.IngredientExtension;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructure;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructures;
import cn.leomc.multiblockmachine.common.registry.BlockRegistry;
import cn.leomc.multiblockmachine.common.utils.Utils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import me.shedaniel.architectury.fluid.FluidStack;
import me.shedaniel.architectury.utils.Fraction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class MachineRecipe implements Recipe<Container> {

    private final ResourceLocation id;
    private final ResourceLocation machineID;
    private final List<Pair<Ingredient, Integer>> inputs;
    private final List<RecipeIngredient> results;
    private final List<ItemStack> itemResults;
    private final long time;
    private final DoubleLong energy;
    private final DoubleLong energyMaxInput;
    private final boolean outputEnergy;
    private final DoubleLong totalOutput;

    public MachineRecipe(ResourceLocation id, ResourceLocation machineID, List<Pair<Ingredient, Integer>> inputs, List<RecipeIngredient> results, long time) {
        this(id, machineID, inputs, results, time, DoubleLong.of(0));
    }

    public MachineRecipe(ResourceLocation id, ResourceLocation machineID, List<Pair<Ingredient, Integer>> inputs, List<RecipeIngredient> results, long time, DoubleLong energy) {
        this(id, machineID, inputs, results, time, energy, DoubleLong.of(energy));
    }

    public MachineRecipe(ResourceLocation id, ResourceLocation machineID, List<Pair<Ingredient, Integer>> inputs, List<RecipeIngredient> results, long time, DoubleLong energy, DoubleLong energyMaxInput) {
        this.id = id;
        this.machineID = machineID;
        this.inputs = inputs;
        this.results = results;
        this.time = time;
        this.energy = energy;
        this.energyMaxInput = energyMaxInput;
        this.outputEnergy = results.stream().anyMatch(ingredient -> ingredient.getType() == RecipeIngredient.ResultType.ENERGY);
        this.totalOutput = DoubleLong.of(results.stream().filter(ingredient -> ingredient.getType() == RecipeIngredient.ResultType.ENERGY).map(RecipeIngredient::getEnergy).mapToDouble(value -> value.doubleValue).sum());
        this.itemResults = results.stream().filter(ingredient -> ingredient.getType() == RecipeIngredient.ResultType.ITEM).map(RecipeIngredient::getItem).collect(Collectors.toList());
    }

    @Override
    public boolean matches(Container original, Level level) {

        SimpleContainer container = new SimpleContainer(original.getContainerSize());

        for (int i = 0; i < original.getContainerSize(); i++)
            container.addItem(original.getItem(i).copy());


        MultiblockStructure structure = MultiblockStructures.getStructure(machineID);
        if (structure == null) {
            MultiblockMachine.LOGGER.warn("Machine not found for recipe: " + id.toString());
            return false;
        }
        for (Pair<Ingredient, Integer> pair : inputs) {
            boolean has = container.hasAnyOf(Arrays.stream(((IngredientExtension) (Object) pair.getFirst()).getItemsServer()).map(ItemStack::getItem).collect(Collectors.toSet()));
            if (!has)
                return false;
        }


        for (Pair<Ingredient, Integer> pair : inputs) {
            int removed = 0;
            boolean enough = false;
            for (ItemStack item : ((IngredientExtension) (Object) pair.getFirst()).getItemsServer()) {
                removed += container.removeItemType(item.getItem(), pair.getSecond() - removed).getCount();
                if (removed >= pair.getSecond()) {
                    enough = true;
                    break;
                }
            }
            if (!enough)
                return false;
        }
        return true;
    }

    @Override
    public ItemStack assemble(Container container) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(BlockRegistry.CONTROLLER.get());
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.addAll(inputs.stream().map(Pair::getFirst).collect(Collectors.toList()));
        return ingredients;
    }

    @Override
    public RecipeType getType() {
        return MachineRecipeType.INSTANCE;
    }

    public ResourceLocation getMachineID() {
        return machineID;
    }

    public List<Pair<Ingredient, Integer>> getInputs() {
        return inputs;
    }

    public List<RecipeIngredient> getResults() {
        return results;
    }

    public List<ItemStack> getItemResults() {
        return itemResults;
    }

    public boolean requireEnergy() {
        return energy.doubleValue > 0 && energyMaxInput.doubleValue > 0;
    }

    public boolean outputEnergy() {
        return outputEnergy;
    }

    public long getTime() {
        return time;
    }

    public long getProcessTime() {
        return requireEnergy() ? (long) (energy.doubleValue / energyMaxInput.doubleValue) : getTime();
    }

    public DoubleLong getEnergy() {
        return DoubleLong.of(energy);
    }

    public DoubleLong getEnergyMaxInput() {
        return DoubleLong.of(energyMaxInput);
    }

    public DoubleLong getTotalOutputEnergy() {
        return totalOutput;
    }

    public enum Serializer implements RecipeSerializer<MachineRecipe> {
        INSTANCE;

        @Override
        public MachineRecipe fromJson(ResourceLocation id, JsonObject json) {
            ResourceLocation machineID = new ResourceLocation(json.get("machine").getAsString());

            List<Pair<Ingredient, Integer>> inputs = new ArrayList<>();

            for (JsonElement element : json.getAsJsonArray("inputs")) {
                JsonObject object = element.getAsJsonObject();
                inputs.add(Pair.of(Ingredient.fromJson(object.get("ingredient")), object.has("count") ? object.get("count").getAsInt() : 1));
            }

            List<RecipeIngredient> results = new ArrayList<>();

            for (JsonElement element : json.getAsJsonArray("results")) {
                JsonObject object = element.getAsJsonObject();
                RecipeIngredient.ResultType type = RecipeIngredient.ResultType.valueOf(object.get("type").getAsString().toUpperCase(Locale.ROOT));
                switch (type) {
                    case ITEM:
                        results.add(RecipeIngredient.of(ShapedRecipe.itemFromJson(object)));
                        break;
                    case ENERGY:
                        results.add(RecipeIngredient.of(DoubleLong.of(object.get("amount").getAsDouble())));
                        break;
                    case FLUID:
                        results.add(RecipeIngredient.of(FluidStack.create(Utils.getRegistryItem(Registry.FLUID, new ResourceLocation(object.get("fluid").getAsString())), Fraction.ofWhole(object.get("amount").getAsLong()))));
                }
            }

            if (json.has("time") && json.has("energy")) {
                MultiblockMachine.LOGGER.error("Failed to load recipe with id \"" + id + "\": Recipe has both time and energy!");
                return null;
            }

            long time = json.has("time") ? json.get("time").getAsLong() : 0;

            DoubleLong energy = DoubleLong.of(json.has("energy") ? json.get("energy").getAsDouble() : 0);

            DoubleLong energyMaxInput = DoubleLong.of(json.has("energyMaxInput") ? json.get("energyMaxInput").getAsDouble() : energy.doubleValue);

            return new MachineRecipe(id, machineID, inputs, results, time, energy, energyMaxInput);
        }

        @Override
        public MachineRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            CompoundTag tag = buf.readNbt();

            ResourceLocation machineID = new ResourceLocation(tag.getString("machine"));

            List<Pair<Ingredient, Integer>> inputs = new ArrayList<>();
            for (Tag input : tag.getList("inputs", 10)) {
                CompoundTag compound = ((CompoundTag) input);
                List<ItemStack> items = new ArrayList<>();
                for (Tag item : compound.getList("items", 10))
                    items.add(ItemStack.of((CompoundTag) item));
                inputs.add(Pair.of(Ingredient.of(items.stream()), compound.getInt("count")));
            }

            List<RecipeIngredient> results = new ArrayList<>();
            for (Tag input : tag.getList("results", 10))
                results.add(RecipeIngredient.of((CompoundTag) input));

            long time = tag.getLong("time");
            DoubleLong energy = DoubleLong.of(tag.getDouble("energy"));
            DoubleLong energyMaxInput = DoubleLong.of(tag.getDouble("energyMaxInput"));

            return new MachineRecipe(id, machineID, inputs, results, time, energy, energyMaxInput);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, MachineRecipe recipe) {
            CompoundTag tag = new CompoundTag();
            tag.putString("machine", recipe.getMachineID().toString());

            ListTag inputs = new ListTag();

            for (Pair<Ingredient, Integer> input : recipe.getInputs()) {
                CompoundTag compound = new CompoundTag();
                ListTag items = new ListTag();
                for (ItemStack item : ((IngredientExtension) (Object) input.getFirst()).getItemsServer())
                    items.add(item.save(new CompoundTag()));
                compound.putInt("count", input.getSecond());
                compound.put("items", items);
                inputs.add(compound);
            }
            ListTag results = new ListTag();
            for (RecipeIngredient result : recipe.getResults())
                results.add(result.save(new CompoundTag()));
            tag.put("inputs", inputs);
            tag.put("results", results);
            tag.putLong("time", recipe.getTime());
            tag.putDouble("energy", recipe.getEnergy().doubleValue);
            tag.putDouble("energyMaxInput", recipe.getEnergyMaxInput().doubleValue);
            buf.writeNbt(tag);
        }

        public ResourceLocation getRegistryName() {
            return MachineRecipeType.ID;
        }

    }

}
