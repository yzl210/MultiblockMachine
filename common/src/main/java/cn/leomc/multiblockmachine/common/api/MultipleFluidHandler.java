package cn.leomc.multiblockmachine.common.api;

import cn.leomc.multiblockmachine.MultiblockMachine;
import com.google.common.collect.Lists;
import dev.architectury.fluid.FluidStack;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MultipleFluidHandler implements IFluidHandler{


    private final List<IFluidHandler> handlers;

    public MultipleFluidHandler(IFluidHandler... handlers) {
        this(Lists.newArrayList(handlers));
    }

    public MultipleFluidHandler(List<IFluidHandler> handlers) {
        this.handlers = handlers;
    }

    @Override
    public int getSize() {
        return handlers
                .stream()
                .mapToInt(IFluidHandler::getSize)
                .sum();
    }

    @Override
    public boolean isEmpty() {
        return handlers
                .stream()
                .allMatch(IFluidHandler::isEmpty);
    }

    @Override
    public FluidStack getFluid(int i) {
        for (IFluidHandler handler : handlers)
            if (i < handler.getSize())
                return handler.getFluid(i);
            else
                i -= handler.getSize();
        return FluidStack.empty();
    }

    @Override
    public FluidStack getFluidRaw(int i) {
        return getFluid(i);
    }

    @Override
    public long receiveFluid(int i, FluidStack fluidStack, boolean simulate, boolean force) {
        if (!force && !canAddFluid(0, fluidStack))
            return 0;

        long totalReceived = 0;
        long max = fluidStack.getAmount();
        for (IFluidHandler handler : handlers) {
            long received = handler.receiveFluid(0, FluidStack.create(fluidStack, max), simulate, force);
            totalReceived += received;
            max -= received;
            if (totalReceived >= fluidStack.getAmount() || max <= 0)
                break;
        }
        return totalReceived;
    }

    @Override
    public long extractFluid(int i, FluidStack fluidStack, boolean simulate, boolean force) {
        long totalExtracted = 0;
        long max = fluidStack.getAmount();
        for (IFluidHandler handler : handlers) {
            long extracted = handler.extractFluid(0, FluidStack.create(fluidStack, max), simulate, force);
            totalExtracted += extracted;
            max -= extracted;
            if (totalExtracted >= fluidStack.getAmount() || max <= 0)
                break;
        }
        return totalExtracted;
    }

    @Override
    public void setFluid(int i, FluidStack fluidStack) {
        handlers.get(i).setFluid(0, fluidStack);
    }

    @Override
    public long getCapacity(int i) {
        return handlers.get(i).getCapacity(0);
    }

    @Override
    public long getMaxReceive(int i) {
        for (IFluidHandler handler : handlers)
            if (i < handler.getSize())
                return handler.getMaxReceive(i);
            else
                i -= handler.getSize();
        return 0;
    }

    @Override
    public long getMaxExtract(int i) {
        for (IFluidHandler handler : handlers)
            if (i < handler.getSize())
                return handler.getMaxExtract(i);
            else
                i -= handler.getSize();
        return 0;
    }

    @Override
    public boolean canAddFluid(int i, FluidStack fluidStack) {
        for (IFluidHandler handler : handlers)
            if (i < handler.getSize())
                return handler.canAddFluid(i, fluidStack);
            else
                i -= handler.getSize();
        return false;
    }

    @Override
    public Collection<FluidStack> getFluids() {
        return handlers
                .stream()
                .flatMap(handler -> handler.getFluids().stream())
                .collect(Collectors.toList());
    }

    @Override
    public MultipleFluidHandler copy() {
        return new MultipleFluidHandler(handlers
                .stream()
                .map(IFluidHandler::copy)
                .toList());
    }


}
