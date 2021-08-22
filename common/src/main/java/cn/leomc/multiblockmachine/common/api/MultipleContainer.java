package cn.leomc.multiblockmachine.common.api;

import com.google.common.collect.Lists;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MultipleContainer implements Container {

    private List<Container> containers;

    public MultipleContainer(Container... containers) {
        this(Lists.newArrayList(containers));
    }

    public MultipleContainer(List<Container> containers) {
        this.containers = containers;
    }

    @Override
    public int getContainerSize() {
        return containers.stream().mapToInt(Container::getContainerSize).sum();
    }

    @Override
    public boolean isEmpty() {
        for (Container container : containers)
            if (!container.isEmpty())
                return false;
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        for (Container container : containers)
            if (index < container.getContainerSize())
                return container.getItem(index);
            else
                index -= container.getContainerSize();
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        int i = index;
        for (Container container : containers)
            if (i < container.getContainerSize())
                return container.removeItem(i, count);
            else
                i -= container.getContainerSize();
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        for (Container container : containers)
            if (index < container.getContainerSize())
                return container.removeItemNoUpdate(index);
            else
                index -= container.getContainerSize();
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        for (Container container : containers)
            if (index < container.getContainerSize()) {
                container.setItem(index, stack);
                return;
            } else
                index -= container.getContainerSize();

    }

    @Override
    public void setChanged() {
        containers.forEach(Container::setChanged);
    }

    @Override
    public boolean stillValid(Player player) {
        for (Container container : containers)
            if (!container.stillValid(player))
                return false;
        return true;
    }

    @Override
    public void clearContent() {
        containers.forEach(Clearable::clearContent);
    }

    public List<ItemStack> removeAllItems() {
        List<ItemStack> list = new ArrayList<>();
        for (Container container : containers)
            for (int i = 0; i < container.getContainerSize(); i++) {
                ItemStack itemStack = container.getItem(i);
                if (itemStack != ItemStack.EMPTY && itemStack != null)
                    list.add(itemStack);
            }
        clearContent();
        return list;
    }

    public ItemStack addItem(ItemStack stack) {
        ItemStack itemStack = stack.copy();
        this.moveItemToOccupiedSlotsWithSameType(itemStack);
        if (itemStack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.moveItemToEmptySlots(itemStack);
            return itemStack.isEmpty() ? ItemStack.EMPTY : itemStack;
        }
    }

    public ItemStack hasItem(ItemStack itemStack) {
        for (int i = 0; i < getContainerSize(); i++) {
            ItemStack itemStack1 = getItem(i);
            if (isSameItem(itemStack, itemStack1) && itemStack1.getCount() >= itemStack.getCount()) {
                return itemStack1;
            }
        }
        return ItemStack.EMPTY;
    }

    public ItemStack removeItemType(Item item, int i) {
        ItemStack itemStack = new ItemStack(item, 0);

        for (int j = getContainerSize() - 1; j >= 0; --j) {
            ItemStack itemStack2 = getItem(j);
            if (itemStack2.getItem().equals(item)) {
                int k = i - itemStack.getCount();
                ItemStack itemStack3 = itemStack2.split(k);
                itemStack.grow(itemStack3.getCount());
                if (itemStack.getCount() == i)
                    break;
            }
        }

        if (!itemStack.isEmpty())
            this.setChanged();

        return itemStack;
    }


    public boolean canAddItem(ItemStack stack) {
        for (int i = 0; i < getContainerSize(); i++) {
            ItemStack itemStack = getItem(i);
            if (itemStack.isEmpty() || isSameItem(itemStack, stack) && itemStack.getCount() < itemStack.getMaxStackSize())
                return true;
        }
        return false;
    }


    private void moveItemToEmptySlots(ItemStack stack) {
        for (int i = 0; i < getContainerSize(); ++i) {
            ItemStack itemStack = getItem(i);
            if (itemStack.isEmpty()) {
                setItem(i, stack.copy());
                stack.setCount(0);
                return;
            }
        }
    }

    private void moveItemToOccupiedSlotsWithSameType(ItemStack stack) {
        for (int i = 0; i < getContainerSize(); i++) {
            int index = i;
            for (Container container : containers)
                if (index < container.getContainerSize()) {
                    ItemStack itemStack = getItem(i);
                    if (this.isSameItem(itemStack, stack)) {
                        moveItemsBetweenStacks(stack, itemStack);
                        if (stack.isEmpty())
                            return;
                    }
                    break;
                } else
                    index -= container.getContainerSize();
        }
    }

    private boolean isSameItem(ItemStack itemStack, ItemStack itemStack2) {
        return itemStack.getItem() == itemStack2.getItem() && ItemStack.tagMatches(itemStack, itemStack2);
    }

    private void moveItemsBetweenStacks(ItemStack itemStack, ItemStack itemStack2) {
        int i = Math.min(getMaxStackSize(), itemStack2.getMaxStackSize());
        int j = Math.min(itemStack.getCount(), i - itemStack2.getCount());
        if (j > 0) {
            itemStack2.grow(j);
            itemStack.shrink(j);
            setChanged();
        }

    }


    public List<Container> getContainers() {
        return containers;
    }


}
