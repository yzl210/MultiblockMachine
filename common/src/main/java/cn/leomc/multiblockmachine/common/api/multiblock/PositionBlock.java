package cn.leomc.multiblockmachine.common.api.multiblock;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

public class PositionBlock implements Predicate<Block> {

    private final List<Value> values;
    private final List<Block> blocks;

    private PositionBlock() {
        this.values = new ArrayList<>();
        this.blocks = new ArrayList<>();
    }

    public static PositionBlock of(JsonElement json) {
        if (json.isJsonObject()) {
            return new PositionBlock().add(value(json.getAsJsonObject()));
        } else if (json.isJsonArray()) {
            PositionBlock positionBlock = new PositionBlock();
            for (JsonElement element : json.getAsJsonArray())
                if (element.isJsonObject())
                    positionBlock.add(value(element.getAsJsonObject()));
            return positionBlock;
        } else
            throw new JsonSyntaxException("Expected block to be a json object or json array");
    }

    private static Value value(JsonObject object) {
        if (object.has("tag") && object.has("block"))
            throw new JsonParseException("A block entry is either a tag or a block, not both");

        if (object.has("block"))
            return new BlockValue(new ResourceLocation(object.get("block").getAsString()));
        else if (object.has("tag"))
            return new TagValue(new ResourceLocation(object.get("tag").getAsString()));
        else
            throw new JsonParseException("A block entry needs either a tag or a block");

    }

    public static PositionBlock of(CompoundTag tag) {
        PositionBlock positionBlock = new PositionBlock();
        for (net.minecraft.nbt.Tag value : tag.getList("values", 10)) {
            CompoundTag compound = (CompoundTag) value;
            if ("block".equals(compound.getString("type")))
                positionBlock.add(new BlockValue(new ResourceLocation(compound.getString("block"))));
            if ("tag".equals(compound.getString("type")))
                positionBlock.add(new TagValue(new ResourceLocation(compound.getString("tag"))));
        }
        return positionBlock;
    }

    public static PositionBlock of() {
        return new PositionBlock();
    }

    public static PositionBlock of(Block... blocks) {
        PositionBlock positionBlock = new PositionBlock();
        for (Block block : blocks)
            positionBlock.add(new BlockValue(block));
        return positionBlock;
    }

    public PositionBlock add(Value value) {
        this.values.add(value);
        return this;
    }

    public List<Value> getValues() {
        return List.copyOf(values);
    }

    private void dissolve() {
        if (blocks.isEmpty())
            for (Value value : values)
                blocks.addAll(value.getBlocks());
    }

    public List<Block> getAllowedBlocks() {
        dissolve();
        return List.copyOf(blocks);
    }

    public boolean test(Block block) {
        dissolve();
        return blocks.stream().anyMatch(block1 -> Registry.BLOCK.getKey(block).equals(Registry.BLOCK.getKey(block1)));
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
        List<Block> getBlocks();

        CompoundTag save();
    }

    public static class BlockValue implements Value {
        private final Block block;

        public BlockValue(ResourceLocation id) {
            this.block = Registry.BLOCK.get(id);
        }

        public BlockValue(Block block) {
            this.block = block;
        }

        @Override
        public List<Block> getBlocks() {
            return block == null ? Collections.emptyList() : Collections.singletonList(block);
        }

        @Override
        public CompoundTag save() {
            CompoundTag compound = new CompoundTag();
            compound.putString("type", "block");
            compound.putString("block", Registry.BLOCK.getKey(block).toString());
            return compound;
        }
    }

    public static class TagValue implements Value {
        private final TagKey<Block> tag;
        private final ResourceLocation id;

        public TagValue(ResourceLocation id) {
            this.tag = TagKey.create(Registry.BLOCK_REGISTRY, id);
            this.id = id;
        }

        @Override
        public List<Block> getBlocks() {
            return StreamSupport.stream(Registry.BLOCK.getTagOrEmpty(tag).spliterator(), false).map(Holder::value).toList();
        }

        public TagKey<Block> getTag() {
            return tag;
        }

        public ResourceLocation getId() {
            return id;
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
