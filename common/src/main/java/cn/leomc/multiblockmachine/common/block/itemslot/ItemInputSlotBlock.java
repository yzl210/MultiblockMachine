package cn.leomc.multiblockmachine.common.block.itemslot;

import cn.leomc.multiblockmachine.common.blockentity.ControllerBlockEntity;
import cn.leomc.multiblockmachine.common.blockentity.itemslot.ItemInputSlotBlockEntity;
import cn.leomc.multiblockmachine.common.utils.Utils;
import me.shedaniel.architectury.platform.Platform;
import me.shedaniel.architectury.registry.MenuRegistry;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class ItemInputSlotBlock extends ItemSlotBlock{

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (level.isClientSide || hand != InteractionHand.MAIN_HAND)
            return InteractionResult.CONSUME;
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ItemInputSlotBlockEntity) {
            if (player instanceof ServerPlayer)
                MenuRegistry.openExtendedMenu((ServerPlayer) player, (MenuProvider) blockEntity, packetBuffer -> packetBuffer.writeBlockPos(blockEntity.getBlockPos()));
                return InteractionResult.SUCCESS;
        } else
            throw new IllegalStateException("Block entity is not an instance of ItemInputSlotBlockEntity!");
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockGetter blockGetter) {
        return Utils.getBlockEntity("ItemInputSlot");
    }
}
