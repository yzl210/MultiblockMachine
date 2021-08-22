package cn.leomc.multiblockmachine.forge.common.blockentity.fluidslot;

import cn.leomc.multiblockmachine.common.blockentity.fluidslot.FluidInputSlotBlockEntity;
import cn.leomc.multiblockmachine.common.blockentity.fluidslot.FluidOutputSlotBlockEntity;
import cn.leomc.multiblockmachine.forge.api.FluidTank;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ForgeFluidOutputSlotBlockEntity extends FluidOutputSlotBlockEntity {

    protected LazyOptional<IFluidHandler> optional = LazyOptional.of(() -> new FluidTank(fluid));

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return optional.cast();
        return super.getCapability(cap, side);
    }
}