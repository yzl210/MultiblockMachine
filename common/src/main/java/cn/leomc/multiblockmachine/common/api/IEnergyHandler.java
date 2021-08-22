package cn.leomc.multiblockmachine.common.api;

public interface IEnergyHandler {

    DoubleLong receiveEnergy(DoubleLong maxReceive, boolean simulate);

    DoubleLong extractEnergy(DoubleLong maxExtract, boolean simulate);

    void setEnergyStored(DoubleLong energy);

    DoubleLong getEnergy();

    DoubleLong getMaxEnergy();

    boolean canExtract();

    boolean canReceive();

}
