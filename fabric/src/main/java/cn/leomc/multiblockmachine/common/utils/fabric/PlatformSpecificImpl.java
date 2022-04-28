package cn.leomc.multiblockmachine.common.utils.fabric;

import cn.leomc.multiblockmachine.common.api.IEnergyHandler;
import cn.leomc.multiblockmachine.common.utils.PlatformSpecific;
import cn.leomc.multiblockmachine.fabric.FabricCreativeEnergySourceBlockEntity;
import cn.leomc.multiblockmachine.fabric.api.EnergyHandlerImpl;
import cn.leomc.multiblockmachine.fabric.common.blockentity.energyslot.FabricEnergyInputSlotBlockEntity;
import cn.leomc.multiblockmachine.fabric.common.blockentity.energyslot.FabricEnergyOutputSlotBlockEntity;
import cn.leomc.multiblockmachine.fabric.common.blockentity.fluidslot.FabricFluidInputSlotBlockEntity;
import cn.leomc.multiblockmachine.fabric.common.blockentity.fluidslot.FabricFluidOutputSlotBlockEntity;
import cn.leomc.multiblockmachine.fabric.common.blockentity.itemslot.FabricItemInputSlotBlockEntity;
import cn.leomc.multiblockmachine.fabric.common.blockentity.itemslot.FabricItemOutputSlotBlockEntity;
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
            case ITEM_INPUT_SLOT -> new FabricItemInputSlotBlockEntity(pos, state);
            case ITEM_OUTPUT_SLOT -> new FabricItemOutputSlotBlockEntity(pos, state);
            case FLUID_INPUT_SLOT -> new FabricFluidInputSlotBlockEntity(pos, state);
            case FLUID_OUTPUT_SLOT -> new FabricFluidOutputSlotBlockEntity(pos, state);
            case ENERGY_INPUT_SLOT -> new FabricEnergyInputSlotBlockEntity(pos, state);
            case ENERGY_OUTPUT_SLOT -> new FabricEnergyOutputSlotBlockEntity(pos, state);
            case CREATIVE_ENERGY_SOURCE -> new FabricCreativeEnergySourceBlockEntity(pos, state);
        };
    }


}
