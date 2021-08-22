package cn.leomc.multiblockmachine.common.config.forge;

import cn.leomc.multiblockmachine.common.config.IConfig;

public class ConfigImpl {
    public static Object get(IConfig config) {
       return ForgeConfig.VALUES.get(config.getID()).get();
    }
}
