package cn.leomc.multiblockmachine.forge;

import cn.leomc.multiblockmachine.MultiblockMachine;
import me.shedaniel.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MultiblockMachine.MODID)
public class MultiblockMachineForge {
    public MultiblockMachineForge() {
        EventBuses.registerModEventBus(MultiblockMachine.MODID, FMLJavaModLoadingContext.get().getModEventBus());
        new MultiblockMachine();
    }
}
