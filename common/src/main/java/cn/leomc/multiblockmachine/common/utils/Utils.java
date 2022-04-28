package cn.leomc.multiblockmachine.common.utils;

import cn.leomc.multiblockmachine.common.api.ITickableBlockEntity;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Utils {

    public static JsonElement readJson(Resource resource) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(resource.getInputStream())) {
            return JsonParser.parseReader(reader);
        }
    }

    public static JsonElement readJson(File file) throws IOException {
        try (FileReader reader = new FileReader(file)) {
            return JsonParser.parseReader(reader);
        }
    }

    public static <T> T getRegistryItem(Registry<T> registry, ResourceLocation id) {
        return registry.getOptional(id).orElseThrow(() -> new RuntimeException(registry + " with id + \"" + id + "\" not found!"));
    }

    public static <T extends BlockEntity> BlockEntityTicker<T> getTicker(boolean client) {
        return (level1, blockPos, blockState, blockEntity) -> {
            if(blockEntity instanceof ITickableBlockEntity be)
                if(client)
                    be.clientTick((ClientLevel) level1, blockPos, blockState);
                else
                    be.serverTick((ServerLevel) level1, blockPos, blockState);
        };
    }

    @FunctionalInterface
    public interface ClientBlockEntityTicker<T extends BlockEntity> {
        void tick(ClientLevel level, BlockPos blockPos, BlockState blockState, T blockEntity);
    }



}
