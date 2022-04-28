package cn.leomc.multiblockmachine.common.blockentity.fluidslot;

import cn.leomc.multiblockmachine.common.api.SingleFluidHandler;
import cn.leomc.multiblockmachine.common.api.IFluidSlot;
import cn.leomc.multiblockmachine.common.blockentity.UpgradableBlockEntity;
import cn.leomc.multiblockmachine.common.menu.fluidslot.FluidSlotMenu;
import dev.architectury.hooks.block.BlockEntityHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class FluidSlotBlockEntity extends UpgradableBlockEntity implements MenuProvider, IFluidSlot {

    protected SingleFluidHandler fluid;

    public FluidSlotBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);
        fluid = new SingleFluidHandler(10000, 1000, 1000, unused -> {
            if(!level.isClientSide)
                BlockEntityHooks.syncData(this);
        });
    }

    public SingleFluidHandler getFluidHandler() {
        return fluid;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new FluidSlotMenu(this, player, inventory, id);
    }

    @Override
    public void load(CompoundTag tag) {
        this.fluid.load(tag.getCompound("fluid"));
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        tag.put("fluid", fluid.save(new CompoundTag()));
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }
}
