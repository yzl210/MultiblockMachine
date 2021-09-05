package cn.leomc.multiblockmachine.common.utils;

import cn.leomc.multiblockmachine.common.api.DoubleLong;
import cn.leomc.multiblockmachine.common.api.IEnergyHandler;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.block.entity.BlockEntity;

public class PlatformSpecific {


    @ExpectPlatform
    public static IEnergyHandler createEnergyHandler(DoubleLong capacity, DoubleLong maxReceive, DoubleLong maxExtract) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static BlockEntity getBlockEntity(String blockEntity) {
        throw new AssertionError();
    }

}
