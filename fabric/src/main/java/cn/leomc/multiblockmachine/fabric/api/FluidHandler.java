package cn.leomc.multiblockmachine.fabric.api;

import cn.leomc.multiblockmachine.common.api.IFluidHandler;
import dev.architectury.fluid.FluidStack;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.world.level.material.Fluid;

import java.util.Iterator;

public class FluidHandler implements Storage<FluidVariant> {

    private IFluidHandler handler;

    public FluidHandler(IFluidHandler handler){
        this.handler = handler;
    }


    @Override
    public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        return handler.receiveFluid(0, FluidStack.create(resource.getFluid(), maxAmount), false, false);
    }

    @Override
    public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        return handler.extractFluid(0, FluidStack.create(resource.getFluid(), maxAmount), false, false);
    }

    @Override
    public Iterator<? extends StorageView<FluidVariant>> iterator(TransactionContext transaction) {
        return new Iterator<>() {
            int i = 0;
            @Override
            public boolean hasNext() {
                return i < handler.getSize();
            }

            @Override
            public StorageView<FluidVariant> next() {
                return new FluidStorageView(i++, handler);
            }
        };

    }

    @Override
    public Iterable<? extends StorageView<FluidVariant>> iterable(TransactionContext transaction) {
        return Storage.super.iterable(transaction);
    }

    class FluidStorageView implements StorageView<FluidVariant> {
        private final int i;
        private final IFluidHandler handler;
        FluidStorageView(int i, IFluidHandler handler){
            this.i = i;
            this.handler = handler;
        }

        @Override
        public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
            return handler.extractFluid(i, FluidStack.create(resource.getFluid(), maxAmount), false, false);
        }

        @Override
        public boolean isResourceBlank() {
            return handler.getFluid(i).isEmpty();
        }

        @Override
        public FluidVariant getResource() {
            FluidStack fluidStack = handler.getFluid(i);
            return FluidVariant.of(fluidStack.getFluid(), fluidStack.getTag());
        }

        @Override
        public long getAmount() {
            return handler.getFluid(i).getAmount();
        }

        @Override
        public long getCapacity() {
            return handler.getCapacity(i);
        }
    }

}
