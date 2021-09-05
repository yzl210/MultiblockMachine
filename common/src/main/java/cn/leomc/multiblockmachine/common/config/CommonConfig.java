package cn.leomc.multiblockmachine.common.config;

import cn.leomc.multiblockmachine.MultiblockMachine;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "common")
public class CommonConfig implements ConfigData {

    @ConfigEntry.Category("energy_input_slot")
    @ConfigEntry.Gui.CollapsibleObject
    public EnergyInputSlot energy_input_slot = new EnergyInputSlot();

    @ConfigEntry.Category("energy_output_slot")
    @ConfigEntry.Gui.CollapsibleObject
    public EnergyOutputSlot energy_output_slot = new EnergyOutputSlot();

    @ConfigEntry.Category("controller")
    @ConfigEntry.Gui.CollapsibleObject
    public Controller controller = new Controller();

    @ConfigEntry.Category("upgrades")
    @ConfigEntry.Gui.CollapsibleObject
    public Upgrades upgrades = new Upgrades();

    @Config(name = "upgrades")
    public static class Upgrades implements ConfigData {

        @ConfigEntry.Gui.TransitiveObject
        @Comment("Enable speed upgrade items")
        public boolean enable_speed = true;

        @ConfigEntry.Gui.TransitiveObject
        @Comment("The max amount speed upgrades can fit into")
        public int max_speed_amount = 2;

        @ConfigEntry.Gui.TransitiveObject
        @Comment("Enable capacity upgrade items")
        public boolean enable_capacity = true;

        @ConfigEntry.Gui.TransitiveObject
        @Comment("The max amount speed upgrades can fit into")
        public int max_capacity_amount = 2;

    }

    @Config(name = "controller")
    public static class Controller implements ConfigData {

        @ConfigEntry.Gui.TransitiveObject
        @Comment("Range: >= 0\nThe execution interval of the controller in ticks.\nLower is faster but more laggy.\nThis won't decrease the time needed for a recipe!")
        public int interval = 5;

        @Override
        public void validatePostLoad() {
            if(interval < 0)
                interval = 0;
        }
    }

    @Config(name = "energy_input_slot")
    public static class EnergyInputSlot implements ConfigData {

        @ConfigEntry.Gui.TransitiveObject
        @Comment("Range: >= 0\nThe base capacity of the energy input slot")
        public double capacity = 1024;

        @ConfigEntry.Gui.TransitiveObject
        @Comment("Range: >= 0\nThe base max energy receive per tick of the energy input slot")
        public double input = 128;

        @Override
        public void validatePostLoad() {
            if(capacity < 0)
                capacity = 0;
            if(input < 0)
                input = 0;
        }

    }

    @Config(name = "energy_output_slot")
    public static class EnergyOutputSlot implements ConfigData {

        @ConfigEntry.Gui.TransitiveObject
        @Comment("Range: >= 0\nThe base capacity of the energy output slot")
        public double capacity = 1024;

        @ConfigEntry.Gui.TransitiveObject
        @Comment("Range: >= 0\nThe base max energy extract per tick of the energy output slot")
        public double output = 128;

        @Override
        public void validatePostLoad() {
            if(capacity < 0)
                capacity = 0;
            if(output < 0)
                output = 0;
        }

    }

    @Override
    public void validatePostLoad() {
        controller.validatePostLoad();
        energy_input_slot.validatePostLoad();
        energy_output_slot.validatePostLoad();
    }
}
