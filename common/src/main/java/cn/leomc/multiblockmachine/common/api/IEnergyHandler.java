package cn.leomc.multiblockmachine.common.api;

public interface IEnergyHandler {

    long receiveEnergy(long maxReceive, boolean simulate, boolean force);

    long extractEnergy(long maxExtract, boolean simulate, boolean force);

    void setEnergyStored(long energy);

    long getEnergy();

    long getMaxEnergy();

    boolean canExtract();

    boolean canReceive();

    IEnergyHandler copy();

}
