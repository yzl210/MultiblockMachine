package cn.leomc.multiblockmachine.forge.common.blockentity.energyslot;

import cn.leomc.multiblockmachine.common.api.DoubleLong;
import cn.leomc.multiblockmachine.common.blockentity.energyslot.EnergyInputSlotBlockEntity;
import cn.leomc.multiblockmachine.common.blockentity.energyslot.EnergyOutputSlotBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ForgeEnergyOutputSlotBlockEntity extends EnergyOutputSlotBlockEntity {

    protected LazyOptional<IEnergyStorage> optional = LazyOptional.of(() -> (IEnergyStorage) handler);

    @Override
    public void tick() {
        super.tick();
        if(level.isClientSide)
            return;
        if(handler.getEnergy().longValue > 0)
            for(Direction direction : Direction.values()) {
                BlockEntity entity = level.getBlockEntity(worldPosition.relative(direction));
                if(entity != null) {
                    LazyOptional<IEnergyStorage> energyStorage = entity.getCapability(CapabilityEnergy.ENERGY);
                    energyStorage.ifPresent(e -> e.receiveEnergy((int) handler.extractEnergy(DoubleLong.of(Long.MAX_VALUE), false).longValue, false));
                }
            }
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityEnergy.ENERGY)
            return optional.cast();
        return super.getCapability(cap, side);
    }
}
