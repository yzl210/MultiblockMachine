package cn.leomc.multiblockmachine.fabric.api;

import cn.leomc.multiblockmachine.common.api.IEnergyHandler;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import team.reborn.energy.api.EnergyStorage;

import java.util.function.Consumer;

public class EnergyHandlerImpl implements IEnergyHandler, EnergyStorage {

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
        StoragePreconditions.notNegative(maxReceive);

        long inserted = Math.min(force ? Long.MAX_VALUE : this.maxReceive, Math.min(maxReceive, capacity - energy));

        if (inserted > 0) {
            energy += inserted;
            return inserted;
        }
        onChanged.accept(energy);
        return 0;
    }

    @Override
    public long extractEnergy(long maxExtract, boolean simulate, boolean force) {
        StoragePreconditions.notNegative(maxExtract);

        long extracted = Math.min(force ? Long.MAX_VALUE : this.maxExtract, Math.min(maxExtract, energy));

        if (extracted > 0) {
            energy -= extracted;
            return extracted;
        }
        onChanged.accept(energy);

        return 0;
    }


    @Override
    public void setEnergyStored(long energy) {
        this.energy = energy;
        onChanged.accept(null);
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
    public long insert(long maxAmount, TransactionContext transaction) {
        return receiveEnergy(maxAmount, false, false);
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        return extractEnergy(maxAmount, false, false);
    }

    @Override
    public long getAmount() {
        return energy;
    }

    @Override
    public long getCapacity() {
        return capacity;
    }

    @Override
    public IEnergyHandler copy() {
        EnergyHandlerImpl energyHandler = new EnergyHandlerImpl(capacity, maxReceive, maxExtract, unused -> {});
        energyHandler.setEnergyStored(energy);
        return energyHandler;
    }

}
