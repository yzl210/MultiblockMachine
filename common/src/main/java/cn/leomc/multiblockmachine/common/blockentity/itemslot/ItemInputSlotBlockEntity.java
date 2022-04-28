package cn.leomc.multiblockmachine.common.blockentity.itemslot;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.common.api.SlotType;
import cn.leomc.multiblockmachine.common.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.block.state.BlockState;

public class ItemInputSlotBlockEntity extends ItemSlotBlockEntity {

    public ItemInputSlotBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.ITEM_INPUT_SLOT.get(), pos, state);
    }
    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("container." + MultiblockMachine.MODID + ".item_input_slot");
    }

    @Override
    protected SimpleContainer createContainer() {
        return new SimpleContainer(2);
    }

    @Override
    public Container getContainer() {
        return container;
    }

    @Override
    public SlotType getSlotType() {
        return SlotType.INPUT;
    }
}
