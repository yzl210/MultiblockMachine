package cn.leomc.multiblockmachine.common.utils.fabric;

import cn.leomc.multiblockmachine.common.api.DoubleLong;
import cn.leomc.multiblockmachine.common.api.IEnergyHandler;
import cn.leomc.multiblockmachine.common.blockentity.itemslot.ItemInputSlotBlockEntity;
import cn.leomc.multiblockmachine.common.blockentity.itemslot.ItemOutputSlotBlockEntity;
import cn.leomc.multiblockmachine.fabric.api.EnergyHandlerImpl;
import cn.leomc.multiblockmachine.fabric.common.blockentity.energyslot.FabricEnergyInputSlotBlockEntity;
import cn.leomc.multiblockmachine.fabric.common.blockentity.energyslot.FabricEnergyOutputSlotBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;

public class PlatformSpecificImpl {
    public static IEnergyHandler createEnergyHandler(DoubleLong capacity, DoubleLong maxReceive, DoubleLong maxExtract) {
        return new EnergyHandlerImpl(capacity, maxReceive, maxExtract);
    }

    public static BlockEntity getBlockEntity(String blockEntity) {
        switch (blockEntity) {
            case "ItemInputSlot":
                return new ItemInputSlotBlockEntity();
            case "ItemOutputSlot":
                return new ItemOutputSlotBlockEntity();
            case "EnergyInputSlot":
                return new FabricEnergyInputSlotBlockEntity();
            case "EnergyOutputSlot":
                return new FabricEnergyOutputSlotBlockEntity();
        }
        return null;
    }
}
