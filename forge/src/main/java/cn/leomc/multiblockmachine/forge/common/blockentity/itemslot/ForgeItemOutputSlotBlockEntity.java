package cn.leomc.multiblockmachine.forge.common.blockentity.itemslot;

import cn.leomc.multiblockmachine.common.blockentity.itemslot.ItemInputSlotBlockEntity;
import cn.leomc.multiblockmachine.common.blockentity.itemslot.ItemOutputSlotBlockEntity;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ForgeItemOutputSlotBlockEntity extends ItemOutputSlotBlockEntity {

    protected LazyOptional<IItemHandler> optional = LazyOptional.of(() -> new InvWrapper(container));


    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return optional.cast();
        return super.getCapability(cap, side);
    }
}
