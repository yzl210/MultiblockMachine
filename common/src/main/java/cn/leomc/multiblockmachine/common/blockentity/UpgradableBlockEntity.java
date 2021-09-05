package cn.leomc.multiblockmachine.common.blockentity;

import cn.leomc.multiblockmachine.common.api.UpgradeItemContainer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class UpgradableBlockEntity extends BlockEntity {

    protected UpgradeItemContainer upgrades = new UpgradeItemContainer(4);

    public UpgradableBlockEntity(BlockEntityType<?> blockEntityType) {
        super(blockEntityType);
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.put("upgrades", upgrades.createTag());
        return super.save(tag);
    }

    @Override
    public void load(BlockState state, CompoundTag tag) {
        super.load(state, tag);
        upgrades.fromTag(tag.getList("upgrades", 10));
    }

    public UpgradeItemContainer getUpgrades() {
        return upgrades;
    }
}
