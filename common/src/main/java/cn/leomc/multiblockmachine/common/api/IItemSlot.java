package cn.leomc.multiblockmachine.common.api;

import net.minecraft.world.Container;

public interface IItemSlot {

    Container getContainer();

    SlotType getSlotType();

    void dropAllItems();
}
