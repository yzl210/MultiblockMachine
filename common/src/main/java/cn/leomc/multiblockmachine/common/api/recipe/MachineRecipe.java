package cn.leomc.multiblockmachine.common.api.recipe;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.common.api.FakeContainer;
import cn.leomc.multiblockmachine.common.api.IFluidHandler;
import cn.leomc.multiblockmachine.common.api.MultipleContainer;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructure;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructures;
import cn.leomc.multiblockmachine.common.registry.BlockRegistry;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.architectury.fluid.FluidStack;
import net.minecraft.core.NonNullList;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;

import java.util.*;
import java.util.stream.Collectors;

public class MachineRecipe implements Recipe<FakeContainer> {

    private final ResourceLocation id;
    private final ResourceLocation machineID;
    private final List<RecipeIngredient> inputs;
    private final List<RecipeIngredient> itemInputs;
    private final List<RecipeIngredient> fluidInputs;
    private final List<RecipeResult> results;
    private final List<RecipeResult> itemResults;
    private final List<RecipeResult> fluidResults;
    private final long time;
    private final long energy;
    private final long energyMaxInput;
    private final boolean outputEnergy;
    private final long totalOutput;

    public MachineRecipe(ResourceLocation id, ResourceLocation machineID, List<RecipeIngredient> inputs, List<RecipeResult> results, long time) {
        this(id, machineID, inputs, results, time, 0);
    }

    public MachineRecipe(ResourceLocation id, ResourceLocation machineID, List<RecipeIngredient> inputs, List<RecipeResult> results, long time, long energyMaxInput) {
        this.id = id;
        this.machineID = machineID;
        this.inputs = inputs;
        this.itemInputs = inputs.stream().filter(ingredient -> ingredient.getType() == IngredientType.ITEM).toList();
        this.fluidInputs = inputs.stream().filter(ingredient -> ingredient.getType() == IngredientType.FLUID).toList();
        this.results = results;
        this.time = time;
        this.energy = inputs.stream().filter(ingredient -> ingredient.getType() == IngredientType.ENERGY).map(RecipeIngredient::getEnergy).mapToLong(value -> value).sum();
        this.energyMaxInput = energyMaxInput < 1 ? energy : energyMaxInput;
        this.outputEnergy = results.stream().anyMatch(result -> result.getType() == IngredientType.ENERGY);
        this.totalOutput = results.stream().filter(result -> result.getType() == IngredientType.ENERGY).map(RecipeResult::getEnergy).mapToLong(value -> value).sum();
        this.itemResults = results.stream().filter(result -> result.getType() == IngredientType.ITEM).toList();
        this.fluidResults = results.stream().filter(result -> result.getType() == IngredientType.FLUID).toList();
    }

    @Override
    public boolean matches(FakeContainer fakeContainer, Level level) {
        MultiblockStructure structure = MultiblockStructures.getStructure(machineID);
        if (structure == null) {
            MultiblockMachine.LOGGER.warn("Machine not found for recipe: " + id.toString());
            return false;
        }

        SimpleContainer items = fakeContainer.getSimpleContainer();

        for (RecipeIngredient input : itemInputs) {
            boolean has = items.hasAnyOf(Arrays.stream(input.getItem().getItems()).map(ItemStack::getItem).collect(Collectors.toSet()));
            if (!has)
                return false;
        }

        for (RecipeIngredient input : itemInputs) {
            int removed = 0;
            boolean enough = false;
            for (ItemStack item : input.getItem().getItems()) {
                removed += items.removeItemType(item.getItem(), ((int) input.getAmount()) - removed).getCount();
                if (removed >= input.getAmount()) {
                    enough = true;
                    break;
                }
            }
            if (!enough)
                return false;
        }

        IFluidHandler fluids = fakeContainer.getFluidHandler();


        if(fluidInputs.isEmpty())
            return true;

        outer:
        for (RecipeIngredient input : fluidInputs) {
            long removed = 0;
            for (Fluid fluid : input.getFluid().getFluids()) {
                for(int i = 0;i < fluids.getSize();i++){
                    removed += fluids.extractFluid(i, FluidStack.create(fluid, input.getIntAmount() - removed), false, false);
                    if (removed >= input.getAmount()) {
                        continue outer;
                    }
                }
            }
            return false;
        }

        return true;
    }

    @Override
    public ItemStack assemble(FakeContainer container) {
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
        ingredients.addAll(inputs.stream()
                .filter(ingredient -> ingredient.getType() == IngredientType.ITEM)
                .map(RecipeIngredient::getItem).toList());
        return ingredients;
    }

    @Override
    public RecipeType<?> getType() {
        return MachineRecipeType.INSTANCE;
    }

    public ResourceLocation getMachineID() {
        return machineID;
    }

    public List<RecipeIngredient> getInputs() {
        return inputs;
    }

    public List<RecipeIngredient> getItemInputs() {
        return itemInputs;
    }


    public List<RecipeIngredient> getFluidInputs() {
        return fluidInputs;
    }


    public List<RecipeResult> getResults() {
        return results;
    }

    public List<RecipeResult> getItemResults() {
        return itemResults;
    }

    public List<RecipeResult> getFluidResults() {
        return fluidResults;
    }

    public boolean requireEnergy() {
        return energy > 0 && energyMaxInput > 0;
    }

    public boolean outputEnergy() {
        return outputEnergy;
    }

    public long getTime() {
        return time;
    }

    public long getProcessTime() {
        return requireEnergy() ? (energy / energyMaxInput) : getTime();
    }

    public long getEnergy() {
        return energy;
    }

    public long getEnergyMaxInput() {
        return energyMaxInput;
    }

    public long getTotalOutputEnergy() {
        return totalOutput;
    }

    public enum Serializer implements RecipeSerializer<MachineRecipe> {
        INSTANCE;

        @Override
        public MachineRecipe fromJson(ResourceLocation id, JsonObject json) {
            ResourceLocation machineID = new ResourceLocation(json.get("machine").getAsString());

            List<RecipeIngredient> inputs = new ArrayList<>();
            for (JsonElement element : json.getAsJsonArray("inputs")) {
                JsonObject object = element.getAsJsonObject();
                inputs.add(RecipeIngredient.of(object));
            }

            List<RecipeResult> results = new ArrayList<>();
            for (JsonElement element : json.getAsJsonArray("results")) {
                JsonObject object = element.getAsJsonObject();
                results.add(RecipeResult.of(object));
            }

            if (json.has("time") && json.has("energy")) {
                MultiblockMachine.LOGGER.error("Failed to load recipe with id \"" + id + "\": Recipe has both time and energy!");
                return null;
            }

            long time = json.has("time") ? json.get("time").getAsLong() : 0;
            long energyMaxInput = json.has("energyMaxInput") ? json.get("energyMaxInput").getAsLong() : 0;

            return new MachineRecipe(id, machineID, inputs, results, time, energyMaxInput);
        }

        @Override
        public MachineRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            CompoundTag tag = buf.readNbt();

            ResourceLocation machineID = new ResourceLocation(tag.getString("machine"));

            List<RecipeIngredient> inputs = new ArrayList<>();
            for (Tag input : tag.getList("inputs", 10))
                inputs.add(RecipeIngredient.of((CompoundTag) input));


            List<RecipeResult> results = new ArrayList<>();
            for (Tag input : tag.getList("results", 10))
                results.add(RecipeResult.of((CompoundTag) input));

            long time = tag.getLong("time");
            long energyMaxInput = tag.getLong("energyMaxInput");

            return new MachineRecipe(id, machineID, inputs, results, time, energyMaxInput);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, MachineRecipe recipe) {
            CompoundTag tag = new CompoundTag();
            tag.putString("machine", recipe.getMachineID().toString());

            ListTag inputs = new ListTag();
            for (RecipeIngredient input : recipe.getInputs())
                inputs.add(input.save(new CompoundTag()));

            ListTag results = new ListTag();
            for (RecipeResult result : recipe.getResults())
                results.add(result.save(new CompoundTag()));

            tag.put("inputs", inputs);
            tag.put("results", results);
            tag.putLong("time", recipe.getTime());
            tag.putLong("energyMaxInput", recipe.getEnergyMaxInput());
            buf.writeNbt(tag);
        }

        public ResourceLocation getRegistryName() {
            return MachineRecipeType.ID;
        }

    }

}
