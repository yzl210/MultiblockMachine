package cn.leomc.multiblockmachine.fabric.common.blockentity.energyslot;

import cn.leomc.multiblockmachine.common.blockentity.energyslot.EnergyInputSlotBlockEntity;
import cn.leomc.multiblockmachine.fabric.api.IEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class FabricEnergyInputSlotBlockEntity extends EnergyInputSlotBlockEntity implements IEnergyStorage {
    public FabricEnergyInputSlotBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }
}
