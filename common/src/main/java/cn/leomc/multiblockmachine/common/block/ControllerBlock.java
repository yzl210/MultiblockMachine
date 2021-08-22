package cn.leomc.multiblockmachine.common.block;

import cn.leomc.multiblockmachine.common.blockentity.ControllerBlockEntity;
import me.shedaniel.architectury.registry.BlockProperties;
import me.shedaniel.architectury.registry.MenuRegistry;
import me.shedaniel.architectury.registry.ToolType;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;


public class ControllerBlock extends Block implements EntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public ControllerBlock() {
        super(
                BlockProperties.of(Material.METAL)
                        .tool(ToolType.PICKAXE, 2)
                        .requiresCorrectToolForDrops()
                        .strength(5, 6)
                        .sound(SoundType.METAL)
        );
    }


    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (level.isClientSide || hand != InteractionHand.MAIN_HAND)
            return InteractionResult.CONSUME;

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ControllerBlockEntity) {
            if (player instanceof ServerPlayer) {
                if(((ControllerBlockEntity) blockEntity).isFormed())
                    MenuRegistry.openExtendedMenu((ServerPlayer) player, (MenuProvider) blockEntity, packetBuffer -> packetBuffer.writeBlockPos(blockEntity.getBlockPos()));
                else
                    ((ServerPlayer) player).sendMessage(new TranslatableComponent("text.multiblockmachine.not_formed"), ChatType.SYSTEM, Util.NIL_UUID);
            }
            return InteractionResult.SUCCESS;
        } else
            throw new IllegalStateException("Block entity is not an instance of ControllerBlockEntity!");
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockGetter blockGetter) {
        return new ControllerBlockEntity();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
