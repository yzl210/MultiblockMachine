package cn.leomc.multiblockmachine.fabric.api;

import cn.leomc.multiblockmachine.common.blockentity.energyslot.EnergySlotBlockEntity;
import team.reborn.energy.api.EnergyStorage;

public interface IEnergyStorage {

    default EnergySlotBlockEntity self(){
        return (EnergySlotBlockEntity) this;
    }

    default EnergyStorage getEnergyStorage(){
        return (EnergyStorage) self().getEnergyHandler();
    }

}
