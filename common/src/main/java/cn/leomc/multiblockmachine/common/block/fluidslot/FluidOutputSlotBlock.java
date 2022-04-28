package cn.leomc.multiblockmachine.common.block.fluidslot;

import cn.leomc.multiblockmachine.common.api.IFluidHandler;
import cn.leomc.multiblockmachine.common.blockentity.fluidslot.FluidOutputSlotBlockEntity;
import cn.leomc.multiblockmachine.common.utils.PlatformSpecific;
import dev.architectury.fluid.FluidStack;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class FluidOutputSlotBlock extends FluidSlotBlock implements EntityBlock {

    public static FluidOutputSlotBlockEntity getBlockEntity(BlockPos pos, BlockState state) {
        return (FluidOutputSlotBlockEntity) PlatformSpecific.getBlockEntity(PlatformSpecific.BlockEntities.FLUID_OUTPUT_SLOT, pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (level.isClientSide || hand != InteractionHand.MAIN_HAND)
            return InteractionResult.CONSUME;

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof FluidOutputSlotBlockEntity be) {
            if(handleBucket(player, hand, level, pos, be.getFluidHandler()))
                return InteractionResult.SUCCESS;
            if (player instanceof ServerPlayer)
                MenuRegistry.openExtendedMenu((ServerPlayer) player, be, packetBuffer -> packetBuffer.writeBlockPos(blockEntity.getBlockPos()));
            return InteractionResult.SUCCESS;
        } else
            throw new IllegalStateException("Block entity is not an instance of FluidOutputSlotBlockEntity!");
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return getBlockEntity(pos, state);
    }
}
