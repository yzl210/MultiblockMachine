package cn.leomc.multiblockmachine.forge.api;

import cn.leomc.multiblockmachine.common.api.DoubleLong;
import cn.leomc.multiblockmachine.common.api.IEnergyHandler;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyHandlerImpl implements IEnergyHandler, IEnergyStorage {

    private long energy;
    private long capacity;
    private long maxReceive;
    private long maxExtract;

    public EnergyHandlerImpl(DoubleLong capacity, DoubleLong maxReceive, DoubleLong maxExtract) {
        this.energy = 0;
        this.capacity = capacity.longValue;
        this.maxReceive = maxReceive.longValue;
        this.maxExtract = maxExtract.longValue;
    }

    @Override
    public DoubleLong receiveEnergy(DoubleLong maxReceive, boolean simulate, boolean force) {
        if (!force && !canReceive())
            return DoubleLong.of(0);

        long energyReceived = Math.min(capacity - energy, force ? maxReceive.longValue : Math.min(this.maxReceive, maxReceive.longValue));
        if (!simulate)
            energy += energyReceived;
        return DoubleLong.of(energyReceived);
    }

    @Override
    public DoubleLong extractEnergy(DoubleLong maxExtract, boolean simulate, boolean force) {
        if (!force && !canExtract())
            return DoubleLong.of(0);

        long energyExtracted = Math.min(energy, force ? maxExtract.longValue : Math.min(this.maxExtract, maxExtract.longValue));
        if (!simulate)
            energy -= energyExtracted;
        return DoubleLong.of(energyExtracted);
    }

    @Override
    public void setEnergyStored(DoubleLong energy) {
        this.energy = energy.longValue;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!canReceive())
            return 0;

        int energyReceived = (int) Math.min(capacity - energy, Math.min(maxReceive, (int) this.maxReceive));
        if (!simulate)
            energy += energyReceived;
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!canExtract())
            return 0;

        int energyExtracted = (int) Math.min(energy, Math.min(maxExtract, (int) this.maxExtract));
        if (!simulate)
            energy -= energyExtracted;
        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        return (int) energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return (int) capacity;
    }

    @Override
    public DoubleLong getEnergy() {
        return DoubleLong.of(energy);
    }

    @Override
    public DoubleLong getMaxEnergy() {
        return DoubleLong.of(capacity);
    }

    @Override
    public boolean canExtract() {
        return maxExtract > 0;
    }

    @Override
    public boolean canReceive() {
        return maxReceive > 0;
    }
}
