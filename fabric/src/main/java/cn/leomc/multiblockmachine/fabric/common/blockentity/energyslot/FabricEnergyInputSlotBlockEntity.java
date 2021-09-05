package cn.leomc.multiblockmachine.fabric.common.blockentity.energyslot;

import cn.leomc.multiblockmachine.common.blockentity.energyslot.EnergyInputSlotBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import team.reborn.energy.*;

public class FabricEnergyInputSlotBlockEntity extends EnergyInputSlotBlockEntity implements EnergyStorage {

    private EnergyHandler energyHandler;

    public FabricEnergyInputSlotBlockEntity(){
        energyHandler = Energy.of(handler);
    }



    @Override
    public double getStored(EnergySide face) {
        return energyHandler.getEnergy();
    }

    @Override
    public void setStored(double amount) {
        energyHandler.set(amount);
    }

    @Override
    public double getMaxStoredPower() {
        return energyHandler.getMaxStored();
    }

    @Override
    public double getMaxInput(EnergySide side) {
        return energyHandler.getMaxInput();
    }

    @Override
    public double getMaxOutput(EnergySide side) {
        return 0;
    }

    @Override
    public EnergyTier getTier() {
        return EnergyTier.INFINITE;
    }
}
