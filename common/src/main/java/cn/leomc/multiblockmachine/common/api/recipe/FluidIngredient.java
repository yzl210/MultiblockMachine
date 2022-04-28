package cn.leomc.multiblockmachine.common.api.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class FluidIngredient implements Predicate<Fluid> {

    public static final FluidIngredient EMPTY = FluidIngredient.of();

    private final List<Value> values;
    private final List<Fluid> fluids;

    private FluidIngredient() {
        this.values = new ArrayList<>();
        this.fluids = new ArrayList<>();
    }

    public static FluidIngredient of(JsonElement json) {
        if (json.isJsonObject()) {
            return new FluidIngredient().add(value(json.getAsJsonObject()));
        } else if (json.isJsonArray()) {
            var fluidIngredient = new FluidIngredient();
            for (JsonElement element : json.getAsJsonArray())
                if (element.isJsonObject())
                    fluidIngredient.add(value(element.getAsJsonObject()));
            return fluidIngredient;
        } else
            throw new JsonSyntaxException("Expected fluid to be a json object or json array");
    }

    private static Value value(JsonObject object) {
        if (object.has("tag") && object.has("fluid"))
            throw new JsonParseException("A fluid entry is either a tag or a fluid, not both");

        if (object.has("fluid"))
            return new FluidValue(new ResourceLocation(object.get("fluid").getAsString()));
        else if (object.has("tag"))
            return new TagValue(new ResourceLocation(object.get("tag").getAsString()));
        else
            throw new JsonParseException("A fluid entry needs either a tag or a fluid");

    }

    public static FluidIngredient of(CompoundTag tag) {
        var fluidIngredient = new FluidIngredient();
        for (net.minecraft.nbt.Tag value : tag.getList("values", 10)) {
            CompoundTag compound = (CompoundTag) value;
            if ("fluid".equals(compound.getString("type")))
                fluidIngredient.add(new FluidValue(new ResourceLocation(compound.getString("fluid"))));
            if ("tag".equals(compound.getString("type")))
                fluidIngredient.add(new TagValue(new ResourceLocation(compound.getString("tag"))));
        }
        return fluidIngredient;
    }

    public static FluidIngredient of() {
        return new FluidIngredient();
    }

    public static FluidIngredient of(Fluid... fluids) {
        var fluidIngredient = new FluidIngredient();
        for (Fluid fluid : fluids)
            fluidIngredient.add(new FluidValue(fluid));
        return fluidIngredient;
    }

    public FluidIngredient add(Value value) {
        this.values.add(value);
        return this;
    }

    public List<Value> getValues() {
        return values;
    }

    private void dissolve() {
        if (!fluids.isEmpty())
            return;
        for (Value value : values)
            fluids.addAll(value.getFluids());
    }

    public List<Fluid> getFluids() {
        dissolve();
        return fluids;
    }

    public boolean test(Fluid fluid) {
        dissolve();
        return fluids.stream().anyMatch(fluid1 -> Registry.FLUID.getKey(fluid).equals(Registry.FLUID.getKey(fluid1)));
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        for (Value value : values)
            list.add(value.save());
        tag.put("values", list);
        return tag;
    }


    public interface Value {
        List<Fluid> getFluids();

        CompoundTag save();
    }

    public static class FluidValue implements Value {
        private final Fluid fluid;

        public FluidValue(ResourceLocation id) {
            this.fluid = Registry.FLUID.get(id);
        }

        public FluidValue(Fluid fluid) {
            this.fluid = fluid;
        }

        @Override
        public List<Fluid> getFluids() {
            return fluid == null ? Collections.emptyList() : Collections.singletonList(fluid);
        }

        @Override
        public CompoundTag save() {
            CompoundTag compound = new CompoundTag();
            compound.putString("type", "fluid");
            compound.putString("fluid", Registry.FLUID.getKey(fluid).toString());
            return compound;
        }
    }

    public static class TagValue implements Value {
        private final Optional<HolderSet.Named<Fluid>> tag;
        private final ResourceLocation id;

        public TagValue(ResourceLocation id) {
            this.tag = Registry.FLUID.getTag(TagKey.create(Registry.FLUID_REGISTRY, id));
            this.id = id;
        }

        @Override
        public List<Fluid> getFluids() {
            return tag.isPresent() ? tag.get().stream().map(Holder::value).toList() : Collections.emptyList();
        }

        public Optional<HolderSet.Named<Fluid>> getTag() {
            return tag;
        }

        @Override
        public CompoundTag save() {
            CompoundTag compound = new CompoundTag();
            compound.putString("type", "tag");
            compound.putString("tag", id.toString());
            return compound;
        }
    }

}
