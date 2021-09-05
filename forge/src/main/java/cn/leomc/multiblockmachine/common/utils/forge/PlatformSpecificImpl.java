package cn.leomc.multiblockmachine.common.utils.forge;

import cn.leomc.multiblockmachine.common.api.DoubleLong;
import cn.leomc.multiblockmachine.common.api.IEnergyHandler;
import cn.leomc.multiblockmachine.forge.api.EnergyHandlerImpl;
import cn.leomc.multiblockmachine.forge.common.blockentity.energyslot.ForgeEnergyInputSlotBlockEntity;
import cn.leomc.multiblockmachine.forge.common.blockentity.energyslot.ForgeEnergyOutputSlotBlockEntity;
import cn.leomc.multiblockmachine.forge.common.blockentity.fluidslot.ForgeFluidInputSlotBlockEntity;
import cn.leomc.multiblockmachine.forge.common.blockentity.fluidslot.ForgeFluidOutputSlotBlockEntity;
import cn.leomc.multiblockmachine.forge.common.blockentity.itemslot.ForgeItemInputSlotBlockEntity;
import cn.leomc.multiblockmachine.forge.common.blockentity.itemslot.ForgeItemOutputSlotBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;

public class PlatformSpecificImpl {
    public static IEnergyHandler createEnergyHandler(DoubleLong capacity, DoubleLong maxReceive, DoubleLong maxExtract) {
        return new EnergyHandlerImpl(capacity, maxReceive, maxExtract);
    }

    public static BlockEntity getBlockEntity(String blockEntity) {
        switch (blockEntity){
            case "ItemInputSlot": return new ForgeItemInputSlotBlockEntity();
            case "ItemOutputSlot": return new ForgeItemOutputSlotBlockEntity();
            case "EnergyInputSlot": return new ForgeEnergyInputSlotBlockEntity();
            case "EnergyOutputSlot": return new ForgeEnergyOutputSlotBlockEntity();
            case "FluidInputSlot": return new ForgeFluidInputSlotBlockEntity();
            case "FluidOutputSlot": return new ForgeFluidOutputSlotBlockEntity();
        }
        return null;
    }
}
