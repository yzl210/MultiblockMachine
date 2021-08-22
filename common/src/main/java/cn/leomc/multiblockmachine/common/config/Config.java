package cn.leomc.multiblockmachine.common.config;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class Config {

    @ExpectPlatform
    public static Object get(IConfig config){
        throw new AssertionError();
    }

}
