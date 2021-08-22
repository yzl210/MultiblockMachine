package cn.leomc.multiblockmachine.common.config.forge;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.common.config.CommonConfig;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.HashMap;

public class ForgeConfig {


    private static ForgeConfigSpec SPEC;

    public static final HashMap<String, ForgeConfigSpec.ConfigValue<?>> VALUES = new HashMap<>();

    public static void register(){
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push(MultiblockMachine.MODID);

        for(CommonConfig config : CommonConfig.values()){
            VALUES.put(config.getID(), builder
                    .comment(config.getComment())
                    .define(config.getID(), config.getDefaultValue()));
        }

        builder.pop();

        SPEC = builder.build();
    }

}
