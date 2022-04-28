package cn.leomc.multiblockmachine.common.blockentity;

import cn.leomc.multiblockmachine.common.api.UpgradeItemContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class UpgradableBlockEntity extends BlockEntity {

    protected UpgradeItemContainer upgrades = new UpgradeItemContainer(4);

    public UpgradableBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        tag.put("upgrades", upgrades.createTag());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        upgrades.fromTag(tag.getList("upgrades", 10));
    }

    public UpgradeItemContainer getUpgrades() {
        return upgrades;
    }
}
