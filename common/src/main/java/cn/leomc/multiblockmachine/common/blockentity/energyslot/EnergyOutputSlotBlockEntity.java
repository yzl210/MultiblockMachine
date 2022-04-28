package cn.leomc.multiblockmachine.common.blockentity.energyslot;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.common.api.IEnergyHandler;
import cn.leomc.multiblockmachine.common.api.SlotType;
import cn.leomc.multiblockmachine.common.api.UpgradeType;
import cn.leomc.multiblockmachine.common.registry.BlockEntityRegistry;
import cn.leomc.multiblockmachine.common.utils.PlatformSpecific;
import dev.architectury.hooks.block.BlockEntityHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.state.BlockState;

public class EnergyOutputSlotBlockEntity extends EnergySlotBlockEntity {

    public EnergyOutputSlotBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.ENERGY_OUTPUT_SLOT.get(), pos, state);
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
        long capacity = MultiblockMachine.CONFIG.common.energy_output_slot.capacity;
        long output = MultiblockMachine.CONFIG.common.energy_output_slot.output;
        if(MultiblockMachine.CONFIG.common.upgrades.enable_capacity)
            capacity *= upgrades.getMultiplier(UpgradeType.CAPACITY);
        if(MultiblockMachine.CONFIG.common.upgrades.enable_speed)
            output *= upgrades.getMultiplier(UpgradeType.SPEED);

        return PlatformSpecific.createEnergyHandler(capacity, 0, output, unused -> {
            if(level != null && !level.isClientSide)
                BlockEntityHooks.syncData(this);
        });
    }
}
