package cn.leomc.multiblockmachine.common.blockentity.energyslot;

import cn.leomc.multiblockmachine.common.api.DoubleLong;
import cn.leomc.multiblockmachine.common.api.IEnergyHandler;
import cn.leomc.multiblockmachine.common.api.IEnergySlot;
import cn.leomc.multiblockmachine.common.api.SlotType;
import cn.leomc.multiblockmachine.common.menu.energyslot.EnergySlotMenu;
import cn.leomc.multiblockmachine.common.utils.PlatformSpecific;
import me.shedaniel.architectury.extensions.BlockEntityExtension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class EnergySlotBlockEntity extends BlockEntity implements MenuProvider, IEnergySlot, BlockEntityExtension, TickableBlockEntity {

    protected IEnergyHandler handler;

    @Environment(EnvType.CLIENT)
    private DoubleLong clientEnergy;

    public EnergySlotBlockEntity(BlockEntityType<?> blockEntityType) {
        super(blockEntityType);
        handler = createEnergyHandler();
    }

    @Override
    public void tick() {
        if (level.isClientSide)
            return;
        syncData();
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
    public void load(BlockState state, CompoundTag tag) {
        super.load(state, tag);
        handler.setEnergyStored(DoubleLong.of(tag.getDouble("energy")));
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putDouble("energy", handler.getEnergy().doubleValue);
        return super.save(tag);
    }

    @Override
    public void loadClientData(BlockState state, CompoundTag tag) {
        clientEnergy = DoubleLong.of(tag.getDouble("energy"));
    }

    @Override
    public CompoundTag saveClientData(CompoundTag tag) {
        tag.putDouble("energy", handler.getEnergy().doubleValue);
        return tag;
    }

    @Environment(EnvType.CLIENT)
    public DoubleLong getEnergyClient() {
        return clientEnergy;
    }
}
