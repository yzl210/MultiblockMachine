package cn.leomc.multiblockmachine.fabric.common.blockentity.fluidslot;

import cn.leomc.multiblockmachine.common.blockentity.fluidslot.FluidInputSlotBlockEntity;
import cn.leomc.multiblockmachine.fabric.api.FluidHandler;
import cn.leomc.multiblockmachine.fabric.api.IFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;


public class FabricFluidInputSlotBlockEntity extends FluidInputSlotBlockEntity implements IFluidStorage {

    private Storage<FluidVariant> handler = new FluidHandler(fluid);

    public FabricFluidInputSlotBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public Storage<FluidVariant> getFluidStorage() {
        return handler;
    }
}
