package cn.leomc.multiblockmachine.fabric.api;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;

public interface IFluidStorage {

    Storage<FluidVariant> getFluidStorage();

}
