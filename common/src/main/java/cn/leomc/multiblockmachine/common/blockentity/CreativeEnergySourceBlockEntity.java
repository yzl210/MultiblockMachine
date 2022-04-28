package cn.leomc.multiblockmachine.common.blockentity;

import cn.leomc.multiblockmachine.common.api.ITickableBlockEntity;
import cn.leomc.multiblockmachine.common.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CreativeEnergySourceBlockEntity extends BlockEntity implements ITickableBlockEntity {
    public CreativeEnergySourceBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.CREATIVE_ENERGY_SOURCE.get(), pos, state);
    }

}
