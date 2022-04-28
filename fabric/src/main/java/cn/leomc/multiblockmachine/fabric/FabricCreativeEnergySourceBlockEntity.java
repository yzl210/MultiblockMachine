package cn.leomc.multiblockmachine.fabric;

import cn.leomc.multiblockmachine.common.blockentity.CreativeEnergySourceBlockEntity;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.EnergyStorageUtil;

public class FabricCreativeEnergySourceBlockEntity extends CreativeEnergySourceBlockEntity {

    private final EnergyStorage storage = new EnergyStorage() {
        @Override
        public boolean supportsInsertion() {
            return false;
        }

        @Override
        public long insert(long maxAmount, TransactionContext transaction) {
            return 0;
        }

        @Override
        public boolean supportsExtraction() {
            return true;
        }

        @Override
        public long extract(long maxAmount, TransactionContext transaction) {
            return maxAmount;
        }

        @Override
        public long getAmount() {
            return Long.MAX_VALUE;
        }

        @Override
        public long getCapacity() {
            return Long.MAX_VALUE;
        }
    };

    public FabricCreativeEnergySourceBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public void serverTick(ServerLevel level, BlockPos pos, BlockState state) {
        for(Direction direction : Direction.values()){
            EnergyStorage storage = EnergyStorage.SIDED.find(level, pos.relative(direction), direction.getOpposite());
            if(storage != null)
                EnergyStorageUtil.move(
                        this.storage,
                        storage,
                        Long.MAX_VALUE,
                        null
                );
        }
    }

    public EnergyStorage getEnergyStorage(){
        return storage;
    }


}
