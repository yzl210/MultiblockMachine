package cn.leomc.multiblockmachine.forge.api;

import cn.leomc.multiblockmachine.common.api.IEnergyHandler;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.function.Consumer;

public class EnergyHandlerImpl implements IEnergyHandler, IEnergyStorage {

    private long energy;
    private long capacity;
    private long maxReceive;
    private long maxExtract;
    private Consumer<Long> onChanged;


    public EnergyHandlerImpl(long capacity, long maxReceive, long maxExtract, Consumer<Long> onChanged) {
        this.energy = 0;
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.onChanged = onChanged;
    }

    @Override
    public long receiveEnergy(long maxReceive, boolean simulate, boolean force) {
        if (!force && !canReceive())
            return 0;

        long energyReceived = Math.min(capacity - energy, force ? maxReceive : Math.min(this.maxReceive, maxReceive));
        if (!simulate)
            energy += energyReceived;
        onChanged.accept(energy);
        return energyReceived;
    }

    @Override
    public long extractEnergy(long maxExtract, boolean simulate, boolean force) {
        if (!force && !canExtract())
            return 0;

        long energyExtracted = Math.min(energy, force ? maxExtract : Math.min(this.maxExtract, maxExtract));
        if (!simulate)
            energy -= energyExtracted;
        onChanged.accept(energy);
        return energyExtracted;
    }

    @Override
    public void setEnergyStored(long energy) {
        this.energy = energy;
        onChanged.accept(null);
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

        int energyExtracted = (int) Math.min(energy, Math.min(maxExtract, this.maxExtract));
        if (!simulate)
            energy -= energyExtracted;
        onChanged.accept(null);
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
    public long getEnergy() {
        return energy;
    }

    @Override
    public long getMaxEnergy() {
        return capacity;
    }

    @Override
    public boolean canExtract() {
        return maxExtract > 0;
    }

    @Override
    public boolean canReceive() {
        return maxReceive > 0;
    }

    @Override
    public IEnergyHandler copy() {
        EnergyHandlerImpl energyHandler = new EnergyHandlerImpl(capacity, maxReceive, maxExtract, unused -> {});
        energyHandler.setEnergyStored(energy);
        return energyHandler;
    }
}
