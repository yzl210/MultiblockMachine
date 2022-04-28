package cn.leomc.multiblockmachine.common.block;

import cn.leomc.multiblockmachine.common.api.multiblock.PositionBlock;
import cn.leomc.multiblockmachine.common.blockentity.InstructionBlockEntity;
import cn.leomc.multiblockmachine.common.utils.Utils;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.stream.Collectors;

public class InstructionBlock extends Block implements EntityBlock {


    public InstructionBlock() {
        super(Properties.of(Material.STRUCTURAL_AIR)
                .instabreak()
                .noCollission()
                .noDrops()
                .strength(0, 0)
        );
    }


    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if(!level.isClientSide ||hand != InteractionHand.MAIN_HAND)
            return InteractionResult.PASS;

        if(player.getItemInHand(hand).isEmpty()){
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if(blockEntity instanceof InstructionBlockEntity instructionBlockEntity) {
                TextComponent component = new TextComponent("");

                if(instructionBlockEntity.getSpecial() == null){
                    for (PositionBlock.Value value : instructionBlockEntity.getBlock().getValues()) {
                        component.append("\n");
                        if (value instanceof PositionBlock.TagValue tagValue) {
                            component.append(new TranslatableComponent("text.multiblockmachine.accept.tag",
                                    new TextComponent(tagValue.getId().toString()).withStyle(style -> style
                                            .withBold(false)
                                            .withUnderlined(true)
                                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                                    new TextComponent(value.getBlocks().stream().map(block -> block.getName().getString()).collect(Collectors.joining(", ")))))))
                                    .withStyle(style -> style.withBold(true)));
                        }
                        if (value instanceof PositionBlock.BlockValue){
                            component.append(block(value.getBlocks().get(0)));
                        }
                    }
                } else {
                    component.append("\n");
                    component.append(block(instructionBlockEntity.getSpecial().getBlock()));
                }
                player.sendMessage(new TranslatableComponent("text.multiblockmachine.accept", component), Util.NIL_UUID);
            }
        }

        return InteractionResult.PASS;
    }

    private Component block(Block block){
        return new TranslatableComponent("text.multiblockmachine.accept.block", block.getName().withStyle(style -> style
                .withBold(false)
                .withUnderlined(true)
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent(Registry.BLOCK.getKey(block).toString())))))
                .withStyle(style -> style.withBold(true));
    }

    @Override
    public MutableComponent getName() {
        return new TranslatableComponent(getDescriptionId());
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.box(0.175, 0.175, 0.175, 0.825, 0.825, 0.825);
    }


    @Override
    public ItemStack getCloneItemStack(BlockGetter getter, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = getter.getBlockEntity(pos);
        if(blockEntity instanceof InstructionBlockEntity)
            return new ItemStack(((InstructionBlockEntity) blockEntity).getMimic().getBlock().asItem());
        return ItemStack.EMPTY;
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.INVISIBLE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new InstructionBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return Utils.getTicker(level.isClientSide);
    }
}
