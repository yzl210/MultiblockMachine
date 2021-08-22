package cn.leomc.multiblockmachine.common.api.recipe;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.common.api.DoubleLong;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructure;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructures;
import cn.leomc.multiblockmachine.common.registry.BlockRegistry;
import cn.leomc.multiblockmachine.common.utils.Utils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.shedaniel.architectury.fluid.FluidStack;
import me.shedaniel.architectury.utils.Fraction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MachineRecipe implements Recipe<Container> {

    private final ResourceLocation id;
    private final ResourceLocation machineID;
    private final List<ItemStack> inputs;
    private final List<RecipeResult> results;
    private final long time;
    private final DoubleLong energy;
    private final DoubleLong energyMaxInput;


    public MachineRecipe(ResourceLocation id, ResourceLocation machineID, List<ItemStack> inputs, List<RecipeResult> results, long time) {
        this(id, machineID, inputs, results, time, DoubleLong.of(0));
    }

    public MachineRecipe(ResourceLocation id, ResourceLocation machineID, List<ItemStack> inputs, List<RecipeResult> results, long time, DoubleLong energy) {
        this(id, machineID, inputs, results, time, energy, DoubleLong.of(energy));
    }

    public MachineRecipe(ResourceLocation id, ResourceLocation machineID, List<ItemStack> inputs, List<RecipeResult> results, long time, DoubleLong energy, DoubleLong energyMaxInput) {
        this.id = id;
        this.machineID = machineID;
        this.inputs = inputs;
        this.results = results;
        this.time = time;
        this.energy = energy;
        this.energyMaxInput = energyMaxInput;
    }

    @Override
    public boolean matches(Container container, Level level) {
        MultiblockStructure structure = MultiblockStructures.getStructure(machineID);
        if (structure == null)
            throw new RuntimeException("Machine not found!");
        for (ItemStack item : inputs) {
            int count = container.countItem(item.getItem());
            if (count == 0 || count < item.getCount())
                return false;
        }
        for(int i = 0;i < container.getContainerSize();i++){
            ItemStack itemStack = container.getItem(i);
            if(itemStack.isEmpty())
                continue;
            boolean have = false;
            for (ItemStack item : inputs) {
                if(item.getItem() == itemStack.getItem()) {
                    have = true;
                    break;
                }
            }
            if(!have)
                return false;
        }
        return true;
    }

    @Override
    public ItemStack assemble(Container container) {
        return null;
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
    public RecipeType<?> getType() {
        return MachineRecipeType.INSTANCE;
    }

    public ResourceLocation getMachineID() {
        return machineID;
    }

    public List<ItemStack> getInputs() {
        return inputs;
    }

    public List<RecipeResult> getResults() {
        return results;
    }

    public boolean requireEnergy() {
        return energy.doubleValue > 0 && energyMaxInput.doubleValue > 0;
    }

    public long getTime() {
        return time;
    }

    public DoubleLong getEnergy() {
        return DoubleLong.of(energy);
    }

    public DoubleLong getEnergyMaxInput() {
        return DoubleLong.of(energyMaxInput);
    }

    public enum Serializer implements RecipeSerializer<MachineRecipe> {
        INSTANCE;

        @Override
        public MachineRecipe fromJson(ResourceLocation id, JsonObject json) {
            ResourceLocation machineID = new ResourceLocation(json.get("machine").getAsString());

            List<ItemStack> inputs = new ArrayList<>();

            for (JsonElement element : json.getAsJsonArray("inputs"))
                inputs.add(ShapedRecipe.itemFromJson(element.getAsJsonObject()));

            List<RecipeResult> results = new ArrayList<>();

            for (JsonElement element : json.getAsJsonArray("results")) {
                JsonObject object = element.getAsJsonObject();
                RecipeResult.ResultType type = RecipeResult.ResultType.valueOf(object.get("type").getAsString().toUpperCase(Locale.ROOT));
                switch (type){
                    case ITEM:
                        results.add(RecipeResult.of(ShapedRecipe.itemFromJson(object)));
                        break;
                    case ENERGY:
                        results.add(RecipeResult.of(DoubleLong.of(object.get("amount").getAsDouble())));
                        break;
                    case FLUID:
                        results.add(RecipeResult.of(FluidStack.create(Utils.getRegistryItem(Registry.FLUID, new ResourceLocation(object.get("fluid").getAsString())), Fraction.ofWhole(object.get("amount").getAsLong()))));
                }
            }

            if (json.has("time") && json.has("energy")) {
                MultiblockMachine.LOGGER.error("Failed to load recipe with id \"" + id + "\": Recipe has both time and energy!");
                return null;
            }

            long time = json.has("time") ? json.get("time").getAsLong() : 0;

            DoubleLong energy = DoubleLong.of(json.has("energy") ? json.get("energy").getAsDouble() : 0);

            DoubleLong energyMaxInput = DoubleLong.of(json.has("energyMaxInput") ? json.get("energyMaxInput").getAsDouble() : energy.doubleValue == 0 ? 0 : energy.doubleValue);

            return new MachineRecipe(id, machineID, inputs, results, time, energy, energyMaxInput);
        }

        @Override
        public MachineRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            CompoundTag tag = buf.readNbt();

            ResourceLocation machineID = new ResourceLocation(tag.getString("machine"));

            List<ItemStack> inputs = new ArrayList<>();
            for (Tag input : tag.getList("inputs", 10))
                inputs.add(ItemStack.of((CompoundTag) input));

            List<RecipeResult> results = new ArrayList<>();
            for (Tag input : tag.getList("results", 10))
                results.add(RecipeResult.of((CompoundTag) input));

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
            for (ItemStack input : recipe.getInputs())
                inputs.add(input.save(new CompoundTag()));
            tag.put("inputs", inputs);
            ListTag results = new ListTag();
            for(RecipeResult result : recipe.getResults())
                results.add(result.save(new CompoundTag()));
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
