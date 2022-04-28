package cn.leomc.multiblockmachine.common.blockentity.fluidslot;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.common.api.SlotType;
import cn.leomc.multiblockmachine.common.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.state.BlockState;

public class FluidInputSlotBlockEntity extends FluidSlotBlockEntity {

    public FluidInputSlotBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.FLUID_INPUT_SLOT.get(), pos, state);
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
