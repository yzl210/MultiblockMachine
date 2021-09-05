package cn.leomc.multiblockmachine.common.block.itemslot;

import cn.leomc.multiblockmachine.common.api.IItemSlot;
import me.shedaniel.architectury.registry.BlockProperties;
import me.shedaniel.architectury.registry.ToolType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public abstract class ItemSlotBlock extends Block implements EntityBlock {
    public ItemSlotBlock() {
        super(
                BlockProperties.of(Material.METAL)
                        .tool(ToolType.PICKAXE, 2)
                        .requiresCorrectToolForDrops()
                        .strength(3, 5)
                        .sound(SoundType.METAL)
        );
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.is(newState.getBlock()))
            return;
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof IItemSlot)
            ((IItemSlot) entity).dropAllItems();
        super.onRemove(state, level, pos, newState, isMoving);
    }
}
