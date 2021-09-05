package cn.leomc.multiblockmachine.common.api;

import me.shedaniel.architectury.fluid.FluidStack;
import me.shedaniel.architectury.utils.Fraction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;

public class FluidHandler {

    private FluidStack stack;
    private Fluid fluid;
    private long capacity;

    public FluidHandler(Fluid fluid, long capacity, Fraction amount) {
        this.fluid = fluid;
        this.capacity = capacity;
        this.stack = FluidStack.create(() -> fluid, amount);
    }

    public FluidStack getFluidStack() {
        return stack;
    }

    public long getCapacity() {
        return capacity;
    }

    public void setFluid(Fluid fluid) {
        this.fluid = fluid;
    }

    public void setFluid(Fluid fluid, Fraction amount) {
        this.fluid = fluid;
        stack.setAmount(amount);
    }

    public void setFluid(Fluid fluid, Fraction amount, CompoundTag tag) {
        this.fluid = fluid;
        stack.setAmount(amount);
        stack.setTag(tag);
    }

}
