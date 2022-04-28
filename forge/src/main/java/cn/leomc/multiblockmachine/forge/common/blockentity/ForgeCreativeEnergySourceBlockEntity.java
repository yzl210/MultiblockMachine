package cn.leomc.multiblockmachine.forge.common.blockentity;

import cn.leomc.multiblockmachine.common.blockentity.CreativeEnergySourceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ForgeCreativeEnergySourceBlockEntity extends CreativeEnergySourceBlockEntity {

    private final IEnergyStorage storage = new IEnergyStorage() {
        @Override
        public int receiveEnergy(int i, boolean bl) {
            return 0;
        }

        @Override
        public int extractEnergy(int i, boolean bl) {
            return i;
        }

        @Override
        public int getEnergyStored() {
            return Integer.MAX_VALUE;
        }

        @Override
        public int getMaxEnergyStored() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean canExtract() {
            return true;
        }

        @Override
        public boolean canReceive() {
            return false;
        }
    };

    public ForgeCreativeEnergySourceBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public void serverTick(ServerLevel level, BlockPos pos, BlockState state) {
        for(Direction direction : Direction.values()){
            BlockEntity blockEntity = level.getBlockEntity(pos.relative(direction));
            if(blockEntity != null)
                blockEntity.getCapability(CapabilityEnergy.ENERGY).ifPresent(storage -> storage.receiveEnergy(Integer.MAX_VALUE, false));
        }
    }


    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityEnergy.ENERGY)
            return LazyOptional.of(() -> storage).cast();

        return super.getCapability(cap, side);
    }

}
