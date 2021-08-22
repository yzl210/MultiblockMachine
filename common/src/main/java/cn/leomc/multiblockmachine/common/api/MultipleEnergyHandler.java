package cn.leomc.multiblockmachine.common.api;

import com.google.common.collect.Lists;

import java.util.List;

public class MultipleEnergyHandler implements IEnergyHandler {

    private List<IEnergyHandler> handlers;

    public MultipleEnergyHandler(IEnergyHandler... handlers) {
        this(Lists.newArrayList(handlers));
    }

    public MultipleEnergyHandler(List<IEnergyHandler> handlers) {
        this.handlers = handlers;
    }

    @Override
    // FIXME: 2021/7/24
    public DoubleLong receiveEnergy(DoubleLong maxReceive, boolean simulate) {
        if (!canReceive())
            return DoubleLong.of(0);

        DoubleLong totalReceived = DoubleLong.of(0);
        for (IEnergyHandler handler : handlers) {
            totalReceived.add(handler.receiveEnergy(maxReceive, simulate));
            if (totalReceived.doubleValue >= maxReceive.doubleValue)
                break;
        }
        return totalReceived;
    }

    @Override
    // FIXME: 2021/7/24 
    public DoubleLong extractEnergy(DoubleLong maxExtract, boolean simulate) {
        if (!canExtract())
            return DoubleLong.of(0);

        DoubleLong totalExtracted = DoubleLong.of(0);
        for (IEnergyHandler handler : handlers) {
            totalExtracted.add(handler.extractEnergy(maxExtract, simulate));
            if (totalExtracted.doubleValue >= maxExtract.doubleValue)
                break;
        }
        return totalExtracted;
    }

    @Override
    public void setEnergyStored(DoubleLong energy) {
        for (IEnergyHandler handler : handlers)
            handler.setEnergyStored(DoubleLong.of(0));

        DoubleLong remain = DoubleLong.of(energy);
        for (IEnergyHandler handler : handlers) {
            remain.subtract(handler.receiveEnergy(remain, false));
            if (remain.doubleValue <= 0)
                break;
        }
    }

    @Override
    public DoubleLong getEnergy() {
        DoubleLong energy = DoubleLong.of(0);
        for (IEnergyHandler handler : handlers)
            energy.add(handler.getEnergy());
        return energy;
    }

    @Override
    public DoubleLong getMaxEnergy() {
        DoubleLong maxEnergy = DoubleLong.of(0);
        for (IEnergyHandler handler : handlers)
            maxEnergy.add(handler.getEnergy());
        return maxEnergy;
    }

    @Override
    public boolean canExtract() {
        for (IEnergyHandler handler : handlers)
            if (handler.canExtract())
                return true;
        return false;
    }

    @Override
    public boolean canReceive() {
        for (IEnergyHandler handler : handlers)
            if (handler.canReceive())
                return true;
        return false;
    }

}
