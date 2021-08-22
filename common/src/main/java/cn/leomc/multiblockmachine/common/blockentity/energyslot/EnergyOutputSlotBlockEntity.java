package cn.leomc.multiblockmachine.common.blockentity.energyslot;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.common.api.DoubleLong;
import cn.leomc.multiblockmachine.common.api.IEnergyHandler;
import cn.leomc.multiblockmachine.common.api.SlotType;
import cn.leomc.multiblockmachine.common.config.CommonConfig;
import cn.leomc.multiblockmachine.common.config.Config;
import cn.leomc.multiblockmachine.common.registry.BlockEntityRegistry;
import cn.leomc.multiblockmachine.common.utils.PlatformSpecific;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class EnergyOutputSlotBlockEntity extends EnergySlotBlockEntity {

    public EnergyOutputSlotBlockEntity() {
        super(BlockEntityRegistry.ENERGY_OUTPUT_SLOT.get());
    }

    @Override
    public SlotType getSlotType() {
        return SlotType.OUTPUT;
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("container." + MultiblockMachine.MODID + ".energy_output_slot");
    }

    @Override
    public IEnergyHandler createEnergyHandler() {
        return PlatformSpecific.createEnergyHandler(DoubleLong.of((double) Config.get(CommonConfig.ENERGY_OUTPUT_SLOT_CAPACITY)), DoubleLong.of(0), DoubleLong.of((double) Config.get(CommonConfig.ENERGY_OUTPUT_SLOT_EXTRACT)));
    }
}
