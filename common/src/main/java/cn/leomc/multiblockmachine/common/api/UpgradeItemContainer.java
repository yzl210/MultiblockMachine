package cn.leomc.multiblockmachine.common.api;

import cn.leomc.multiblockmachine.common.item.UpgradeItem;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class UpgradeItemContainer extends SimpleContainer {

    public UpgradeItemContainer(int size) {
        super(size);
    }

    @Override
    public boolean canAddItem(ItemStack itemStack) {
        return itemStack.getItem() instanceof UpgradeItem && super.canAddItem(itemStack);
    }

    @Override
    public ItemStack addItem(ItemStack itemStack) {
        if(!canAddItem(itemStack))
            return itemStack.copy();
        return super.addItem(itemStack);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    public List<Integer> getUpgrades(UpgradeType type){
        List<Integer> upgrades = new ArrayList<>();
        for (int i = 0; i < getContainerSize(); i++) {
            ItemStack itemStack = getItem(i);
            if(itemStack.getItem() instanceof UpgradeItem && ((UpgradeItem) itemStack.getItem()).getUpgradeType() == type)
                upgrades.add(((UpgradeItem) itemStack.getItem()).getMultiplier());
        }
        return upgrades;
    }

    public int getMultiplier(UpgradeType type){
        int multiplier = 1;
        for (Integer m : getUpgrades(type))
            multiplier *= m;
        return multiplier;
    }

}
