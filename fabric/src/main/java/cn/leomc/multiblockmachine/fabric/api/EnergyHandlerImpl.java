package cn.leomc.multiblockmachine.fabric.api;

import cn.leomc.multiblockmachine.common.api.DoubleLong;
import cn.leomc.multiblockmachine.common.api.IEnergyHandler;
import team.reborn.energy.*;

public class EnergyHandlerImpl implements IEnergyHandler, EnergyStorage {

    protected EnergyHandler handler;

    protected double energy;
    protected double capacity;
    protected double maxReceive;
    protected double maxExtract;

    public EnergyHandlerImpl(DoubleLong capacity, DoubleLong maxReceive, DoubleLong maxExtract) {
        handler = Energy.of(this);
        this.capacity = capacity.doubleValue;
        this.maxReceive = maxReceive.doubleValue;
        this.maxExtract = maxExtract.doubleValue;
    }


    @Override
    public DoubleLong receiveEnergy(DoubleLong maxReceive, boolean simulate) {
        EnergyHandler handler = simulate ? Energy.of(this).simulate() : this.handler;
        return DoubleLong.of(handler.insert(maxReceive.doubleValue));
    }

    @Override
    public DoubleLong extractEnergy(DoubleLong maxExtract, boolean simulate) {
        EnergyHandler handler = simulate ? Energy.of(this).simulate() : this.handler;
        return DoubleLong.of(handler.extract(maxExtract.doubleValue));
    }

    @Override
    public void setEnergyStored(DoubleLong energy) {
        this.energy = energy.doubleValue;
    }

    @Override
    public DoubleLong getEnergy() {
        return DoubleLong.of(handler.getEnergy());
    }

    @Override
    public DoubleLong getMaxEnergy() {
        return DoubleLong.of(handler.getMaxStored());
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
    public double getStored(EnergySide face) {
        return energy;
    }

    @Override
    public void setStored(double amount) {
        this.energy = amount;
    }

    @Override
    public double getMaxStoredPower() {
        return capacity;
    }

    @Override
    public double getMaxInput(EnergySide side) {
        return maxReceive;
    }

    @Override
    public double getMaxOutput(EnergySide side) {
        return maxExtract;
    }

    @Override
    public EnergyTier getTier() {
        return EnergyTier.INFINITE;
    }
}
