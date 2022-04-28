package cn.leomc.multiblockmachine.common.utils.forge;

import cn.leomc.multiblockmachine.common.api.IEnergyHandler;
import cn.leomc.multiblockmachine.common.utils.PlatformSpecific;
import cn.leomc.multiblockmachine.forge.api.EnergyHandlerImpl;
import cn.leomc.multiblockmachine.forge.common.blockentity.ForgeCreativeEnergySourceBlockEntity;
import cn.leomc.multiblockmachine.forge.common.blockentity.energyslot.ForgeEnergyInputSlotBlockEntity;
import cn.leomc.multiblockmachine.forge.common.blockentity.energyslot.ForgeEnergyOutputSlotBlockEntity;
import cn.leomc.multiblockmachine.forge.common.blockentity.fluidslot.ForgeFluidInputSlotBlockEntity;
import cn.leomc.multiblockmachine.forge.common.blockentity.fluidslot.ForgeFluidOutputSlotBlockEntity;
import cn.leomc.multiblockmachine.forge.common.blockentity.itemslot.ForgeItemInputSlotBlockEntity;
import cn.leomc.multiblockmachine.forge.common.blockentity.itemslot.ForgeItemOutputSlotBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;

public class PlatformSpecificImpl {
    public static IEnergyHandler createEnergyHandler(long capacity, long maxReceive, long maxExtract, Consumer<Long> onChanged) {
        return new EnergyHandlerImpl(capacity, maxReceive, maxExtract, onChanged);
    }

    public static BlockEntity getBlockEntity(PlatformSpecific.BlockEntities blockEntity, BlockPos pos, BlockState state) {
        return switch (blockEntity) {
            case ITEM_INPUT_SLOT -> new ForgeItemInputSlotBlockEntity(pos, state);
            case ITEM_OUTPUT_SLOT -> new ForgeItemOutputSlotBlockEntity(pos, state);
            case ENERGY_INPUT_SLOT -> new ForgeEnergyInputSlotBlockEntity(pos, state);
            case ENERGY_OUTPUT_SLOT -> new ForgeEnergyOutputSlotBlockEntity(pos, state);
            case FLUID_INPUT_SLOT -> new ForgeFluidInputSlotBlockEntity(pos, state);
            case FLUID_OUTPUT_SLOT -> new ForgeFluidOutputSlotBlockEntity(pos, state);
            case CREATIVE_ENERGY_SOURCE -> new ForgeCreativeEnergySourceBlockEntity(pos, state);
        };
    }
}
