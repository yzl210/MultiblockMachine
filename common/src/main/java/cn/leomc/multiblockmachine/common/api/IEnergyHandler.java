package cn.leomc.multiblockmachine.common.api;

public interface IEnergyHandler {

    DoubleLong receiveEnergy(DoubleLong maxReceive, boolean simulate, boolean force);

    DoubleLong extractEnergy(DoubleLong maxExtract, boolean simulate, boolean force);

    void setEnergyStored(DoubleLong energy);

    DoubleLong getEnergy();

    DoubleLong getMaxEnergy();

    boolean canExtract();

    boolean canReceive();

}
