package cn.leomc.multiblockmachine.common.blockentity.fluidslot;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.common.api.SlotType;
import cn.leomc.multiblockmachine.common.registry.BlockEntityRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class FluidInputSlotBlockEntity extends FluidSlotBlockEntity {

    public FluidInputSlotBlockEntity() {
        super(BlockEntityRegistry.FLUID_INPUT_SLOT.get());
    }

    @Override
    public SlotType getSlotType() {
        return SlotType.INPUT;
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("container." + MultiblockMachine.MODID + ".fluid_input_slot");
    }
}
