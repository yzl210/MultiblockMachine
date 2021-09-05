package cn.leomc.multiblockmachine.common.api.multiblock;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.common.block.ControllerBlock;
import cn.leomc.multiblockmachine.common.block.InstructionBlock;
import cn.leomc.multiblockmachine.common.blockentity.InstructionBlockEntity;
import cn.leomc.multiblockmachine.common.network.NetworkHandler;
import cn.leomc.multiblockmachine.common.registry.BlockRegistry;
import cn.leomc.multiblockmachine.common.utils.Utils;
import com.google.common.base.Stopwatch;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public enum MultiblockStructures implements ResourceManagerReloadListener {

    INSTANCE;

    public final HashMap<ResourceLocation, MultiblockStructure> MACHINES = new HashMap<>();

    @Environment(EnvType.CLIENT)
    public final List<Pair<BlockPos, MultiblockStructure>> RENDERING = new ArrayList<>();

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

    public static MutableComponent getStructureName(ResourceLocation id) {
        if (id == null)
            return new TextComponent("");
        return new TranslatableComponent("multiblockmachine." + id.getNamespace() + "." + id.getPath());
    }

    public static void buildStructure(MultiblockStructure structure, ServerLevel level, BlockPos pos, Direction direction){
        level.setBlockAndUpdate(pos, BlockRegistry.CONTROLLER.get().defaultBlockState().setValue(ControllerBlock.FACING, direction));
        level.getChunkSource().blockChanged(pos);

        for (Map.Entry<BlockPos, PositionBlock> entry : structure.getBlocks().entrySet()) {
            BlockPos blockPos = pos.offset(getOffset(entry.getKey(), direction));

            level.setBlockAndUpdate(blockPos, entry.getValue().getAllowedBlocks().get(0).defaultBlockState());
            level.getChunkSource().blockChanged(blockPos);
        }
    }

    public static void buildStructureInstruction(MultiblockStructure structure, ServerLevel level, BlockPos pos, Direction direction){
        if(level.getBlockState(pos).isAir()) {
            level.setBlockAndUpdate(pos, BlockRegistry.INSTRUCTION_BLOCK.get().defaultBlockState());
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof InstructionBlockEntity)
                ((InstructionBlockEntity) entity).setSpecial(BlockRegistry.CONTROLLER.get().defaultBlockState().setValue(ControllerBlock.FACING, direction));
            level.getChunkSource().blockChanged(pos);
        }
        for (Map.Entry<BlockPos, PositionBlock> entry : structure.getBlocks().entrySet()) {
            BlockPos blockPos = pos.offset(getOffset(entry.getKey(), direction));

            if(!level.getBlockState(blockPos).isAir())
                continue;

            level.setBlockAndUpdate(blockPos, BlockRegistry.INSTRUCTION_BLOCK.get().defaultBlockState());
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if(blockEntity instanceof InstructionBlockEntity)
                ((InstructionBlockEntity) blockEntity).setBlock(entry.getValue());
            level.getChunkSource().blockChanged(blockPos);
        }
    }

    public static BlockPos getOffset(BlockPos pos, Direction direction){

        BlockPos.MutableBlockPos offset = new BlockPos.MutableBlockPos();

        switch (direction) {
            case EAST:
                offset.setX(-pos.getZ());
                offset.setY(pos.getY());
                offset.setZ(-pos.getX());
                break;
            case WEST:
                offset.setX(pos.getZ());
                offset.setY(pos.getY());
                offset.setZ(pos.getX());
                break;
            case SOUTH:
                offset.setX(pos.getX());
                offset.setY(pos.getY());
                offset.setZ(-pos.getZ());
                break;
            case NORTH:
                offset.setX(-pos.getX());
                offset.setY(pos.getY());
                offset.setZ(pos.getZ());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }
        return offset;
    }


    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        MACHINES.clear();
        try {
            for (ResourceLocation rl : manager.listResources("multiblock_machines", s -> s.endsWith(".json"))) {
                Resource resource = manager.getResource(rl);
                try {
                    if (MACHINES.containsKey(rl))
                        throw new RuntimeException("Duplicated ID");
                    ResourceLocation id = new ResourceLocation(rl.getNamespace(), rl.getPath().replace("multiblock_machines/", "").replace(".json", ""));
                    JsonObject json = Utils.readJson(resource).getAsJsonObject();
                    MultiblockStructure structure = new MultiblockStructure(id, new HashMap<>());
                    for (JsonElement element : json.getAsJsonArray("blocks")) {
                        JsonObject block = element.getAsJsonObject();
                        //x: right
                        //y: up
                        //z: back
                        structure.addBlock(new BlockPos(block.get("x").getAsInt(), block.get("y").getAsInt(), block.get("z").getAsInt()), PositionBlock.of(block.get("block")));
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
        NetworkHandler.syncStructures();
        MultiblockMachine.LOGGER.info("Loaded " + MACHINES.size() + " machines, took " + stopwatch.stop().elapsed(TimeUnit.MILLISECONDS) + " ms");
    }


}
