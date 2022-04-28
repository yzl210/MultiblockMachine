package cn.leomc.multiblockmachine.common.blockentity.energyslot;

import cn.leomc.multiblockmachine.common.api.IEnergyHandler;
import cn.leomc.multiblockmachine.common.api.IEnergySlot;
import cn.leomc.multiblockmachine.common.blockentity.UpgradableBlockEntity;
import cn.leomc.multiblockmachine.common.menu.energyslot.EnergySlotMenu;
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

public abstract class EnergySlotBlockEntity extends UpgradableBlockEntity implements MenuProvider, IEnergySlot {

    protected IEnergyHandler handler;

    public EnergySlotBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);
        handler = createEnergyHandler();
    }

    public IEnergyHandler getEnergyHandler() {
        return handler;
    }

    public abstract IEnergyHandler createEnergyHandler();

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new EnergySlotMenu(this, player, inventory, id);
    }

    @Override
    public void load(CompoundTag tag) {
        handler.setEnergyStored(tag.getLong("energy"));
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        tag.putLong("energy", handler.getEnergy());
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
