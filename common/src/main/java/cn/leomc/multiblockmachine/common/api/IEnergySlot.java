package cn.leomc.multiblockmachine.common.api;

public interface IEnergySlot {

    IEnergyHandler getEnergyHandler();

    SlotType getSlotType();

}
