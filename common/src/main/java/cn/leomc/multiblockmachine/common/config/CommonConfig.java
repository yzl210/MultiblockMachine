package cn.leomc.multiblockmachine.common.config;

import java.util.Locale;

public enum CommonConfig implements IConfig {
    ENERGY_INPUT_SLOT_CAPACITY(1024.0, "The base capacity of the energy input slot"),
    ENERGY_INPUT_SLOT_RECEIVE(128.0, "The base max energy receive per tick of the energy input slot"),
    ENERGY_OUTPUT_SLOT_CAPACITY(1024.0,  "The base capacity of the energy output slot"),
    ENERGY_OUTPUT_SLOT_EXTRACT(128.0, "The base max energy extract per tick of the energy output slot");

    private final String comment;
    private final Object defaultValue;

    CommonConfig(Object defaultValue){
        this(defaultValue, "");
    }

    CommonConfig(Object defaultValue, String comment){
        this.defaultValue = defaultValue;
        this.comment = comment;
    }

    @Override
    public Object getDefaultValue() {
        return defaultValue;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public String getID() {
        return toString().toLowerCase(Locale.ROOT);
    }


}
