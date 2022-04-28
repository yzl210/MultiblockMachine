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
    public long receiveEnergy(long maxReceive, boolean simulate, boolean force) {
        if (!force && !canReceive())
            return 0;

        long totalReceived = 0;
        long max = maxReceive;
        for (IEnergyHandler handler : handlers) {
            long received = handler.receiveEnergy(max, simulate, force);
            totalReceived += received;
            max -= received;
            if (totalReceived >= maxReceive || max <= 0)
                break;
        }
        return totalReceived;
    }

    @Override
    public long extractEnergy(long maxExtract, boolean simulate, boolean force) {
        if (!force && !canExtract())
            return 0;

        long totalExtracted = 0;
        long max = maxExtract;
        for (IEnergyHandler handler : handlers) {
            long extracted = handler.extractEnergy(max, simulate, force);
            totalExtracted += extracted;
            max -= extracted;
            if (totalExtracted >= maxExtract || max <= 0)
                break;
        }
        return totalExtracted;
    }

    @Override
    public void setEnergyStored(long energy) {
        for (IEnergyHandler handler : handlers)
            handler.setEnergyStored(0);

        long remain = energy;
        for (IEnergyHandler handler : handlers) {
            remain -= handler.receiveEnergy(remain, false, true);
            if (remain <= 0)
                break;
        }
    }

    @Override
    public long getEnergy() {
        long energy = 0;
        for (IEnergyHandler handler : handlers)
            energy += handler.getEnergy();
        return energy;
    }

    @Override
    public long getMaxEnergy() {
        long maxEnergy = 0;
        for (IEnergyHandler handler : handlers)
            maxEnergy += handler.getEnergy();
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

    @Override
    public MultipleEnergyHandler copy() {
        return new MultipleEnergyHandler(handlers
                .stream()
                .map(IEnergyHandler::copy)
                .toList());
    }

}
