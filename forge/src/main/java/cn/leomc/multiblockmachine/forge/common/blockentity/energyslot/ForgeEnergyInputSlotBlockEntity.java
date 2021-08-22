package cn.leomc.multiblockmachine.forge.common.blockentity.energyslot;

import cn.leomc.multiblockmachine.common.blockentity.energyslot.EnergyInputSlotBlockEntity;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ForgeEnergyInputSlotBlockEntity extends EnergyInputSlotBlockEntity {

    protected LazyOptional<IEnergyStorage> optional = LazyOptional.of(() -> (IEnergyStorage) handler);

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityEnergy.ENERGY)
            return optional.cast();
        return super.getCapability(cap, side);
    }
}
