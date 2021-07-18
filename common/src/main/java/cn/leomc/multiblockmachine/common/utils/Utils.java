package cn.leomc.multiblockmachine.common.utils;

import me.shedaniel.architectury.platform.Platform;
import net.minecraft.world.level.block.entity.BlockEntity;


public class Utils {

    public static <T extends BlockEntity> T getBlockEntity(String name) {
        if (Platform.isForge()) {
            try {
                return (T) Class.forName("cn.leomc.multiblockmachine.forge.common.blockentity.Forge" + name + "BlockEntity").newInstance();
            } catch (ClassNotFoundException | ClassCastException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
        try {
            return (T) Class.forName("cn.leomc.multiblockmachine.common.blockentity." + name + "BlockEntity").newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
