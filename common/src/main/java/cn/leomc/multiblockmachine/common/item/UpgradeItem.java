package cn.leomc.multiblockmachine.common.item;

import cn.leomc.multiblockmachine.common.api.UpgradeType;
import cn.leomc.multiblockmachine.common.registry.ModRegistry;
import net.minecraft.world.item.Item;

public abstract class UpgradeItem extends Item {
    public UpgradeItem() {
        super(new Properties().tab(ModRegistry.TAB));
    }

    public abstract UpgradeType getUpgradeType();

    public abstract int getMultiplier();

    public abstract int getLevel();

}
