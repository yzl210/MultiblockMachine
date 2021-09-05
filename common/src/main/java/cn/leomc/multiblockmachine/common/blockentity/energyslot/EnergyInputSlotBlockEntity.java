package cn.leomc.multiblockmachine.common.blockentity.energyslot;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.common.api.DoubleLong;
import cn.leomc.multiblockmachine.common.api.IEnergyHandler;
import cn.leomc.multiblockmachine.common.api.SlotType;
import cn.leomc.multiblockmachine.common.api.UpgradeType;
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
        double capacity = MultiblockMachine.CONFIG.common.energy_input_slot.capacity;
        double input = MultiblockMachine.CONFIG.common.energy_input_slot.input;
        if(MultiblockMachine.CONFIG.common.upgrades.enable_capacity)
            capacity *= upgrades.getMultiplier(UpgradeType.CAPACITY);
        if(MultiblockMachine.CONFIG.common.upgrades.enable_speed)
            input *= upgrades.getMultiplier(UpgradeType.SPEED);


        return PlatformSpecific.createEnergyHandler(DoubleLong.of(capacity), DoubleLong.of(input), DoubleLong.of(0));
    }
}
