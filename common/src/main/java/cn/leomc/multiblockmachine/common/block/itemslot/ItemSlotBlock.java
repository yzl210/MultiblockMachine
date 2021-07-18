package cn.leomc.multiblockmachine.common.block.itemslot;

import me.shedaniel.architectury.registry.BlockProperties;
import me.shedaniel.architectury.registry.ToolType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;

public abstract class ItemSlotBlock extends Block implements EntityBlock {
    public ItemSlotBlock() {
        super(
                BlockProperties.of(Material.METAL)
                        .tool(ToolType.PICKAXE, 2)
                        .requiresCorrectToolForDrops()
                        .strength(5, 6)
                        .sound(SoundType.METAL)
        );
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

}
