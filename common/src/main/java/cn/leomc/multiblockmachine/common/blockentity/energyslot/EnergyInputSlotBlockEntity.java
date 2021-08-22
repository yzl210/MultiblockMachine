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

public class EnergyInputSlotBlockEntity extends EnergySlotBlockEntity {

    public EnergyInputSlotBlockEntity() {
        super(BlockEntityRegistry.ENERGY_INPUT_SLOT.get());
    }

    @Override
    public SlotType getSlotType() {
        return SlotType.INPUT;
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("container." + MultiblockMachine.MODID + ".energy_input_slot");
    }

    @Override
    public IEnergyHandler createEnergyHandler() {
        return PlatformSpecific.createEnergyHandler(DoubleLong.of((double) Config.get(CommonConfig.ENERGY_INPUT_SLOT_CAPACITY)), DoubleLong.of((double) Config.get(CommonConfig.ENERGY_INPUT_SLOT_RECEIVE)), DoubleLong.of(0));
    }
}
