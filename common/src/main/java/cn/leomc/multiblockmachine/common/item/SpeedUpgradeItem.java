package cn.leomc.multiblockmachine.common.item;

import cn.leomc.multiblockmachine.common.api.UpgradeType;

public class SpeedUpgradeItem extends UpgradeItem{

    private int level;

    public SpeedUpgradeItem(int level){
        this.level = level;
    }

    @Override
    public UpgradeType getUpgradeType() {
        return UpgradeType.SPEED;
    }

    @Override
    public int getMultiplier() {
        return level + 1;
    }

    @Override
    public int getLevel() {
        return level;
    }

}
