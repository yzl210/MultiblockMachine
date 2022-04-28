package cn.leomc.multiblockmachine.fabric.common.blockentity.energyslot;

import cn.leomc.multiblockmachine.common.blockentity.energyslot.EnergyOutputSlotBlockEntity;
import cn.leomc.multiblockmachine.fabric.api.IEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.EnergyStorageUtil;

public class FabricEnergyOutputSlotBlockEntity extends EnergyOutputSlotBlockEntity implements IEnergyStorage {

    public FabricEnergyOutputSlotBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

}
