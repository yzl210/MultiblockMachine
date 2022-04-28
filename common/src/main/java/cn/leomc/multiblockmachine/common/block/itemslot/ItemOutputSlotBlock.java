package cn.leomc.multiblockmachine.common.block.itemslot;

import cn.leomc.multiblockmachine.common.blockentity.itemslot.ItemOutputSlotBlockEntity;
import cn.leomc.multiblockmachine.common.utils.PlatformSpecific;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class ItemOutputSlotBlock extends ItemSlotBlock {

    public static ItemOutputSlotBlockEntity getBlockEntity(BlockPos pos, BlockState state) {
        return (ItemOutputSlotBlockEntity) PlatformSpecific.getBlockEntity(PlatformSpecific.BlockEntities.ITEM_OUTPUT_SLOT, pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (level.isClientSide || hand != InteractionHand.MAIN_HAND)
            return InteractionResult.CONSUME;
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ItemOutputSlotBlockEntity) {
            if (player instanceof ServerPlayer)
                MenuRegistry.openExtendedMenu((ServerPlayer) player, (MenuProvider) blockEntity, packetBuffer -> packetBuffer.writeBlockPos(blockEntity.getBlockPos()));
            return InteractionResult.SUCCESS;
        } else {
            throw new IllegalStateException("Block entity is not an instance of ItemOutputSlotBlockEntity!");
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return getBlockEntity(pos, state);
    }
}
