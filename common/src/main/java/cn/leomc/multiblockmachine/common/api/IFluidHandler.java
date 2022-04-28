package cn.leomc.multiblockmachine.common.api;

import dev.architectury.fluid.FluidStack;
import net.minecraft.world.level.material.Fluids;

import java.util.Collection;

public interface IFluidHandler {

    int getSize();

    boolean isEmpty();

    FluidStack getFluid(int i);

    FluidStack getFluidRaw(int i);

    long receiveFluid(int i, FluidStack fluidStack, boolean simulate, boolean force);

    long extractFluid(int i, FluidStack fluidStack, boolean simulate, boolean force);

    void setFluid(int i, FluidStack fluidStack);

    long getCapacity(int i);

    long getMaxReceive(int i);

    long getMaxExtract(int i);

    boolean canAddFluid(int i, FluidStack fluidStack);

    Collection<FluidStack> getFluids();

    default void validateFluid(){
        for(int i = 0;i < getSize();i++) {
            FluidStack fluidStack = getFluidRaw(i);
            if (fluidStack.getFluid() != Fluids.EMPTY && fluidStack.getAmount() <= 0)
                setFluid(i, FluidStack.create(Fluids.EMPTY, 0));
            if(fluidStack.getAmount() > getCapacity(i))
                fluidStack.setAmount(getCapacity(i));
        }
    }

    IFluidHandler copy();

}
