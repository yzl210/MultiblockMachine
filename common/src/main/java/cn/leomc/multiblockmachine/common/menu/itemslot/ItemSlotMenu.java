package cn.leomc.multiblockmachine.common.menu.itemslot;

import cn.leomc.multiblockmachine.common.api.IItemSlot;
import cn.leomc.multiblockmachine.common.blockentity.itemslot.ItemSlotBlockEntity;
import cn.leomc.multiblockmachine.common.menu.BaseMenu;
import cn.leomc.multiblockmachine.common.registry.ContainerMenuRegistry;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ItemSlotMenu extends BaseMenu<ItemSlotBlockEntity> {
    public ItemSlotMenu(ItemSlotBlockEntity blockEntity, Player player, Inventory inventory, int windowId) {
        super(ContainerMenuRegistry.ITEM_SLOT.get(), blockEntity, player, inventory, windowId);
        addSlot(((IItemSlot) blockEntity).getContainer(), 0, 40, 50, 2, 18);
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
