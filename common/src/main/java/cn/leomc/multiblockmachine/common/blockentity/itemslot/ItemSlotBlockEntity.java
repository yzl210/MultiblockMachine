package cn.leomc.multiblockmachine.common.blockentity.itemslot;

import cn.leomc.multiblockmachine.common.api.IItemSlot;
import cn.leomc.multiblockmachine.common.blockentity.UpgradableBlockEntity;
import cn.leomc.multiblockmachine.common.menu.itemslot.ItemSlotMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class ItemSlotBlockEntity extends UpgradableBlockEntity implements MenuProvider, IItemSlot {

    protected SimpleContainer container;

    public ItemSlotBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);
        container = createContainer();
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        tag.put("container", container.createTag());
    }

    @Override
    public void load(CompoundTag tag) {
        container.fromTag(tag.getList("container", 10));
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new ItemSlotMenu(this, player, inventory, id);
    }

    @Override
    public void dropAllItems() {
        Containers.dropContents(level, worldPosition, container);
        container.clearContent();
    }

    protected abstract SimpleContainer createContainer();

    @Override
    public Container getContainer() {
        return container;
    }

}
