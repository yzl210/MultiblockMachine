package cn.leomc.multiblockmachine.forge.common.blockentity.itemslot;

import cn.leomc.multiblockmachine.common.blockentity.itemslot.ItemInputSlotBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ForgeItemInputSlotBlockEntity extends ItemInputSlotBlockEntity {

    protected LazyOptional<IItemHandler> optional = LazyOptional.of(() -> new InvWrapper(container));

    public ForgeItemInputSlotBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return optional.cast();
        return super.getCapability(cap, side);
    }
}
