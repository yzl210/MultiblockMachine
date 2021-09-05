package cn.leomc.multiblockmachine.fabric.common.blockentity.energyslot;

import cn.leomc.multiblockmachine.common.blockentity.energyslot.EnergyOutputSlotBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import team.reborn.energy.*;

public class FabricEnergyOutputSlotBlockEntity extends EnergyOutputSlotBlockEntity implements EnergyStorage {

    private EnergyHandler energyHandler;

    public FabricEnergyOutputSlotBlockEntity() {
        energyHandler = Energy.of(handler);
    }

    @Override
    public void tick() {
        super.tick();
        if (level.isClientSide)
            return;
        if (energyHandler.getEnergy() > 0)
            for (Direction direction : Direction.values())
                try {
                    BlockEntity entity = level.getBlockEntity(worldPosition.relative(direction));
                    if(entity != null)
                        energyHandler
                              .into(Energy.of(entity))
                              .move();
                } catch (UnsupportedOperationException ignored){
                }


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
        return 0;
    }

    @Override
    public double getMaxOutput(EnergySide side) {
        return energyHandler.getMaxOutput();
    }

    @Override
    public EnergyTier getTier() {
        return EnergyTier.INFINITE;
    }
}
