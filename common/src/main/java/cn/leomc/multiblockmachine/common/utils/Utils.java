package cn.leomc.multiblockmachine.common.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Utils {

    public static JsonElement readJson(Resource resource) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(resource.getInputStream())) {
            return new JsonParser().parse(reader);
        }
    }

    public static JsonElement readJson(File file) throws IOException {
        try (FileReader reader = new FileReader(file)) {
            return new JsonParser().parse(reader);
        }
    }

    public static <T> T getRegistryItem(Registry<T> registry, ResourceLocation id) {
        return registry.getOptional(id).orElseThrow(() -> new RuntimeException(registry + " with id + \"" + id + "\" not found!"));
    }
}
