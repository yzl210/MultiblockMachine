package cn.leomc.multiblockmachine.forge.api;

import cn.leomc.multiblockmachine.common.api.FluidHandler;
import me.shedaniel.architectury.hooks.forge.FluidStackHooksForge;
import me.shedaniel.architectury.utils.Fraction;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;


public class FluidTank implements IFluidHandler {

    private FluidHandler handler;

    public FluidTank(FluidHandler handler) {
        this.handler = handler;
    }

    @Override
    public int getTanks() {
        return handler.getFluidStack().getAmount().intValue();
    }

    @NotNull
    @Override
    public FluidStack getFluidInTank(int i) {
        return new FluidStack(handler.getFluidStack().getFluid(), handler.getFluidStack().getAmount().intValue()){
            @Override
            public void setAmount(int amount) {
                handler.getFluidStack().setAmount(Fraction.ofWhole(amount));
                super.setAmount(amount);
            }

            @Override
            public int getAmount() {
                return handler.getFluidStack().getAmount().intValue();
            }

        };
    }

    @Override
    public int getTankCapacity(int i) {
        return (int) handler.getCapacity();
    }

    @Override
    public boolean isFluidValid(int i, @NotNull FluidStack fluidStack) {
        return true;
    }

    @Override
    public int fill(FluidStack stack, FluidAction action) {
        if (stack.isEmpty() || !isFluidValid(0, stack)) {
            return 0;
        }
        if (action.simulate()) {
            if (handler.getFluidStack().isEmpty()) {
                return (int) Math.min(handler.getCapacity(), stack.getAmount());
            }
            if (!isFluidEqual(FluidStackHooksForge.toForge(handler.getFluidStack()), stack)) {
                return 0;
            }
            return (int) Math.min(handler.getCapacity() - handler.getFluidStack().getAmount().intValue(), stack.getAmount());
        }
        if (handler.getFluidStack().isEmpty()) {
            handler.setFluid(stack.getFluid(), Fraction.ofWhole(Math.min(handler.getCapacity(), stack.getAmount())), stack.getTag());
            return handler.getFluidStack().getAmount().intValue();
        }
        if (!isFluidEqual(FluidStackHooksForge.toForge(handler.getFluidStack()), stack)) {
            return 0;
        }
        int filled = (int) (handler.getCapacity() - handler.getFluidStack().getAmount().intValue());

        if (stack.getAmount() < filled) {
            handler.getFluidStack().grow(Fraction.ofWhole(stack.getAmount()));
            filled = stack.getAmount();
        } else {
            handler.getFluidStack().setAmount(Fraction.ofWhole(handler.getCapacity()));
        }

        return filled;
    }

    @NotNull
    @Override
    public FluidStack drain(FluidStack stack, FluidAction action) {
        if (stack.isEmpty() || !stack.isFluidEqual(FluidStackHooksForge.toForge(handler.getFluidStack())))
            return FluidStack.EMPTY;
        return drain(stack.getAmount(), action);
    }

    @NotNull
    @Override
    public FluidStack drain(int amount, FluidAction action) {
        int drained = amount;
        if (handler.getFluidStack().getAmount().intValue() < drained)
            drained = handler.getFluidStack().getAmount().intValue();

        FluidStack stack = new FluidStack(handler.getFluidStack().getFluid(), drained);
        if (action.execute() && drained > 0)
            handler.getFluidStack().shrink(Fraction.ofWhole(drained));

        return stack;
    }

    private boolean isFluidEqual(FluidStack fluidStack, FluidStack fluidStack1){
        return fluidStack.getFluid() == fluidStack1.getFluid() && isFluidTagEqual(fluidStack, fluidStack1);
    }

    private boolean isFluidTagEqual(FluidStack fluidStack, FluidStack fluidStack1){
        return fluidStack.getTag() == null ? fluidStack1.getTag() == null : fluidStack1.getTag() != null && fluidStack.getTag().equals(fluidStack1.getTag());
    }

}
