package cn.leomc.multiblockmachine.common.blockentity.fluidslot;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.common.api.SlotType;
import cn.leomc.multiblockmachine.common.registry.BlockEntityRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class FluidOutputSlotBlockEntity extends FluidSlotBlockEntity {

    public FluidOutputSlotBlockEntity() {
        super(BlockEntityRegistry.FLUID_OUTPUT_SLOT.get());
    }

    @Override
    public SlotType getSlotType() {
        return SlotType.OUTPUT;
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("container." + MultiblockMachine.MODID + ".fluid_output_slot");
    }
}
