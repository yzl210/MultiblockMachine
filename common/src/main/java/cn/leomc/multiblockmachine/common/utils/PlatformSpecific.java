package cn.leomc.multiblockmachine.common.utils;

import cn.leomc.multiblockmachine.common.api.IEnergyHandler;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;

public class PlatformSpecific {


    @ExpectPlatform
    public static IEnergyHandler createEnergyHandler(long capacity, long maxReceive, long maxExtract, Consumer<Long> onChanged) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static BlockEntity getBlockEntity(BlockEntities blockEntity, BlockPos pos, BlockState state) {
        throw new AssertionError();
    }


    public static String getEnergy(){
        return Platform.isForge() ? "FE" : "E";
    }

    public enum BlockEntities {
        ITEM_INPUT_SLOT,
        ITEM_OUTPUT_SLOT,
        ENERGY_INPUT_SLOT,
        ENERGY_OUTPUT_SLOT,
        FLUID_INPUT_SLOT,
        FLUID_OUTPUT_SLOT,
        CREATIVE_ENERGY_SOURCE
    }

}
