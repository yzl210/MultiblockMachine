package cn.leomc.multiblockmachine.common.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;


public class BaseMenu extends AbstractContainerMenu {

    public final Player player;
    protected final BlockEntity blockEntity;
    protected final Inventory playerInventory;

    protected BaseMenu(MenuType<?> type, BlockEntity blockEntity, Player player, Inventory inventory, int windowId) {
        super(type, windowId);
        this.blockEntity = blockEntity;
        this.player = player;
        this.playerInventory = inventory;
        addPlayerInventorySlots(8, 84);
    }


    protected int addSlot(Container inventory, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            int finalIndex = index;
            addSlot(new Slot(inventory, finalIndex, x, y) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return container.canPlaceItem(finalIndex, stack);
                }
            });
            x += dx;
            index++;
        }
        return index;
    }

    protected int addSlot(Container inventory, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlot(inventory, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    protected void addPlayerInventorySlots(int leftCol, int topRow) {
        addSlot(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);
        topRow += 58;
        addSlot(playerInventory, 0, leftCol, topRow, 9, 18);
    }


    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return false;
    }

    public BlockEntity getBlockEntity() {
        return blockEntity;
    }
}
