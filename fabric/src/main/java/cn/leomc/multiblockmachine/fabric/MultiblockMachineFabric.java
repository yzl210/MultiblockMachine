package cn.leomc.multiblockmachine.fabric;

import cn.leomc.multiblockmachine.MultiblockMachine;
import net.fabricmc.api.ModInitializer;

public class MultiblockMachineFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        new MultiblockMachine();
    }
}
