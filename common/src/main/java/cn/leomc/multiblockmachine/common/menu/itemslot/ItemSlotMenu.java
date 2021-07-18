package cn.leomc.multiblockmachine.common.menu.itemslot;

import cn.leomc.multiblockmachine.common.blockentity.itemslot.IContainerProvider;
import cn.leomc.multiblockmachine.common.menu.BaseMenu;
import cn.leomc.multiblockmachine.common.registry.BlockRegistry;
import cn.leomc.multiblockmachine.common.registry.ContainerMenuRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ItemSlotMenu extends BaseMenu {
    public ItemSlotMenu(BlockEntity blockEntity, Player player, Inventory inventory, int windowId) {
        super(ContainerMenuRegistry.ITEM_SLOT.get(), blockEntity, player, inventory, windowId);
        if(blockEntity instanceof IContainerProvider)
            addSlot(((IContainerProvider) blockEntity).getContainer(), 0, 40,50,2,18);
    }

    @Override
    public boolean stillValid(Player player) {
        BlockPos pos = blockEntity.getBlockPos();
        return player.distanceToSqr(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
    }


    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();
            if (index >= 36 && index <= 37) {
                if (!this.moveItemStackTo(stack, 0, 36, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stack, itemstack);
            } else {

                if (!this.moveItemStackTo(stack, 36, 38, false)) {
                    return ItemStack.EMPTY;
                }

            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, stack);
        }

        return itemstack;
    }

}
