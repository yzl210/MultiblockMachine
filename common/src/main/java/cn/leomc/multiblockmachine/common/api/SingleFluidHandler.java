package cn.leomc.multiblockmachine.common.api;

import cn.leomc.multiblockmachine.MultiblockMachine;
import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.FluidStackHooks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;

public class SingleFluidHandler implements IFluidHandler{

    private long capacity;
    private long maxReceive;
    private long maxExtract;
    private Fluid fluid;
    private FluidStack fluidStack;
    private final Consumer<FluidStack> onChanged;


    public SingleFluidHandler(long capacity, long maxReceive, long maxExtract) {
        this(capacity, maxReceive, maxExtract, unused -> {});
    }

    public SingleFluidHandler(long capacity, long maxReceive, long maxExtract, Consumer<FluidStack> onChanged){
        this.fluid = Fluids.EMPTY;
        this.fluidStack = FluidStack.create(() -> fluid, 0);
        this.capacity = capacity;
        this.maxExtract = maxExtract;
        this.maxReceive = maxReceive;
        this.onChanged = onChanged;
    }

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        validateFluid();
        return fluid.isSame(Fluids.EMPTY);
    }


    @Override
    public FluidStack getFluid(int i) {
        validateFluid();
        return fluidStack;
    }

    @Override
    public FluidStack getFluidRaw(int i) {
        return fluidStack;
    }

    @Override
    public long receiveFluid(int i, FluidStack receive, boolean simulate, boolean force) {
        if (receive.isEmpty())
            return 0;

        if (simulate) {
            if (fluidStack.isEmpty())
                return Math.min(capacity, Math.min(force ? Long.MAX_VALUE : maxReceive, receive.getAmount()));

            if (!fluidStack.getFluid().isSame(receive.getFluid()))
                return 0;
            return Math.min(capacity - fluidStack.getAmount(), Math.min(force ? Long.MAX_VALUE : maxReceive, receive.getAmount()));
        }
        if (fluidStack.isEmpty()) {
            fluid = receive.getFluid();
            fluidStack.setAmount(Math.min(capacity, Math.min(force ? Long.MAX_VALUE : maxReceive, receive.getAmount())));
            fluidStack.setTag(receive.getTag());
            return fluidStack.getAmount();
        }
        if (!fluidStack.getFluid().isSame(receive.getFluid()))
            return 0;

        long received = Math.min(capacity - fluidStack.getAmount(), Math.min(force ? Long.MAX_VALUE : maxReceive, receive.getAmount()));

        fluidStack.grow(received);
        onChanged.accept(fluidStack);
        return received;
    }

    @Override
    public long extractFluid(int i, FluidStack extract, boolean simulate, boolean force) {
        if(!extract.getFluid().isSame(fluid))
            return 0;

        long extracted = extract.getAmount();

        if (fluidStack.getAmount() < extracted)
            extracted = fluidStack.getAmount();

        if (!simulate && extracted > 0)
            fluidStack.shrink(extracted);
        onChanged.accept(null);
        return extracted;
    }

    @Override
    public void setFluid(int i, FluidStack fluidStack) {
        this.fluid = fluidStack.getFluid();
        this.fluidStack.setAmount(fluidStack.getAmount());
        validateFluid();
        onChanged.accept(fluidStack);
    }

    @Override
    public long getCapacity(int i) {
        return capacity;
    }

    @Override
    public long getMaxReceive(int i) {
        return maxReceive;
    }

    @Override
    public long getMaxExtract(int i) {
        return maxExtract;
    }

    @Override
    public boolean canAddFluid(int i, FluidStack fluidStack) {
        return receiveFluid(i, fluidStack, true, false) > 0;
    }

    @Override
    public Collection<FluidStack> getFluids() {
        return Collections.singletonList(fluidStack);
    }

    @Override
    public IFluidHandler copy() {
        SingleFluidHandler handler = new SingleFluidHandler(capacity, maxReceive, maxExtract);
        handler.setFluid(0, fluidStack.copy());
        return handler;
    }

    public CompoundTag save(CompoundTag tag){
        tag.put("fluid", FluidStackHooks.write(fluidStack, new CompoundTag()));
        return tag;
    }

    public void load(CompoundTag tag){
        FluidStack fluidStack = FluidStackHooks.read(tag.getCompound("fluid"));
        this.fluid = fluidStack.getFluid();
        this.fluidStack.setAmount(fluidStack.getAmount());
        this.fluidStack.setTag(fluidStack.getTag());
    }



}
