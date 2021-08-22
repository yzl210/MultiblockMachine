package cn.leomc.multiblockmachine.common.api.multiblock;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.common.utils.Utils;
import com.google.common.base.Stopwatch;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public enum MultiblockStructures implements ResourceManagerReloadListener {

    INSTANCE;

    private final HashMap<ResourceLocation, MultiblockStructure> MACHINES = new HashMap<>();

    // public static MultiblockStructure TESTSTRUCTURE = new MultiblockStructure(new ResourceLocation("multiblockmachine", "test"), new HashMap<>())
    //       .addBlock(new BlockPos(1, 0, 0), new PositionBlock(Blocks.IRON_BLOCK))
    //     .addBlock(new BlockPos(0, 0, 1), new PositionBlock(Blocks.DIAMOND_BLOCK))
    //   .addBlock(new BlockPos(0, 1, 0), new PositionBlock(BlockTags.WOOL))
    // .addBlock(new BlockPos(-1,0,0), new PositionBlock(TagsRegistry.ITEM_SLOT_BLOCKS));

    @Nullable
    public static MultiblockStructure getFormedStructure(Level level, BlockPos origin, Direction facing) {
        for (MultiblockStructure structure : INSTANCE.MACHINES.values()) {
            if (structure.isFormed(level, origin, facing)) {
                return structure;
            }
        }
        return null;
    }

    public static MultiblockStructure getStructure(ResourceLocation id) {
        return INSTANCE.MACHINES.get(id);
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        MACHINES.clear();
        try {
            for (ResourceLocation rl : manager.listResources("multiblock_machines", s -> s.endsWith(".json"))) {
                Resource resource = manager.getResource(rl);
                try {
                    JsonObject json = Utils.readJson(resource).getAsJsonObject();
                    ResourceLocation id = new ResourceLocation(json.get("id").getAsString());
                    if (MACHINES.containsKey(id))
                        throw new RuntimeException("Duplicated ID");
                    MultiblockStructure structure = new MultiblockStructure(id, new HashMap<>());
                    for (JsonElement element : json.getAsJsonArray("blocks")) {
                        JsonObject block = element.getAsJsonObject();
                        JsonObject block1 = block.getAsJsonObject("block");

                        PositionBlock positionBlock = new PositionBlock(Utils.getRegistryItem(Registry.BLOCK, new ResourceLocation(block1.get("id").getAsString())));
                        structure.addBlock(new BlockPos(block.get("right").getAsInt(), block.get("up").getAsInt(), block.get("back").getAsInt()), positionBlock);
                    }

                    MACHINES.put(id, structure);
                } catch (Exception e) {
                    MultiblockMachine.LOGGER.error("Failed to load machine with id \"" + rl + "\": " + e);
                }
            }
        } catch (IOException e) {
            if (!(e instanceof FileNotFoundException))
                e.printStackTrace();
        }
        MultiblockMachine.LOGGER.info("Loaded " + MACHINES.size() + " machines, took " + stopwatch.stop().elapsed(TimeUnit.MILLISECONDS) + " ms");
    }


}
