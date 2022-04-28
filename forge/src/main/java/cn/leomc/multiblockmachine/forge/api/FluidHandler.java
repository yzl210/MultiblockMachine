package cn.leomc.multiblockmachine.forge.api;

import cn.leomc.multiblockmachine.common.api.IFluidHandler;
import dev.architectury.hooks.fluid.forge.FluidStackHooksForge;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import org.jetbrains.annotations.NotNull;


public class FluidHandler implements net.minecraftforge.fluids.capability.IFluidHandler, IFluidTank {

    private IFluidHandler handler;

    public FluidHandler(IFluidHandler handler) {
        this.handler = handler;
    }

    @Override
    public int getTanks() {
        return handler.getSize();
    }

    @NotNull
    @Override
    public FluidStack getFluidInTank(int i) {
        return FluidStackHooksForge.toForge(handler.getFluid(i));
    }

    @Override
    public int getTankCapacity(int i) {
        return (int) handler.getCapacity(i);
    }

    @Override
    public boolean isFluidValid(int i, @NotNull FluidStack fluidStack) {
        return handler.canAddFluid(i, FluidStackHooksForge.fromForge(fluidStack));
    }

    @NotNull
    @Override
    public FluidStack getFluid() {
        return FluidStackHooksForge.toForge(handler.getFluid(0));
    }

    @Override
    public int getFluidAmount() {
        return (int) handler.getFluid(0).getAmount();
    }

    @Override
    public int getCapacity() {
        return (int) handler.getCapacity(0);
    }

    @Override
    public boolean isFluidValid(FluidStack fluidStack) {
        return handler.canAddFluid(0, FluidStackHooksForge.fromForge(fluidStack));
    }

    @Override
    public int fill(FluidStack fluidStack, FluidAction fluidAction) {
        return (int) handler.receiveFluid(0, FluidStackHooksForge.fromForge(fluidStack), fluidAction.simulate(), false);
    }

    @NotNull
    @Override
    public FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
        return new FluidStack(handler.getFluid(0).getFluid(), (int) handler.extractFluid(0, FluidStackHooksForge.fromForge(fluidStack), fluidAction.simulate(), false));
    }

    @NotNull
    @Override
    public FluidStack drain(int i, FluidAction fluidAction) {
        return new FluidStack(handler.getFluid(0).getFluid(), (int) handler.extractFluid(0, dev.architectury.fluid.FluidStack.create(handler.getFluid(0),
                i), fluidAction.simulate(), false));
    }
}
