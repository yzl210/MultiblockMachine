package cn.leomc.multiblockmachine.common.block;

import cn.leomc.multiblockmachine.common.api.multiblock.PositionBlock;
import cn.leomc.multiblockmachine.common.blockentity.InstructionBlockEntity;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

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
        if(!level.isClientSide || hand != InteractionHand.MAIN_HAND)
            return InteractionResult.PASS;

        if(player.getItemInHand(hand).isEmpty()){
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if(blockEntity instanceof InstructionBlockEntity) {
                InstructionBlockEntity instructionBlockEntity = ((InstructionBlockEntity) blockEntity);

                MutableComponent component = new TextComponent("\n");

                if(instructionBlockEntity.getSpecial() != null)
                    component.append(new TranslatableComponent("text.multiblockmachine.accept.block", instructionBlockEntity.getSpecial().getBlock().getName()));
                else {

                    Iterator<PositionBlock.Value> valueIterator = instructionBlockEntity.getBlock().getValues().iterator();

                    while (valueIterator.hasNext()) {
                        PositionBlock.Value value = valueIterator.next();
                        if (value instanceof PositionBlock.TagValue) {
                            MutableComponent blocks = new TextComponent("");
                            Iterator<Block> iterator = value.getBlocks().iterator();
                            while (iterator.hasNext()) {
                                blocks.append(iterator.next().getName().getString());
                                if (iterator.hasNext())
                                    blocks.append(", ");
                            }

                            component.append(new TranslatableComponent("text.multiblockmachine.accept.tag",
                                    new TextComponent(((PositionBlock.TagValue) value).getTag().getName().toString()))
                                    .withStyle(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, blocks))));
                        }
                        if (value instanceof PositionBlock.BlockValue)
                            component.append(new TranslatableComponent("text.multiblockmachine.accept.block", value.getBlocks().get(0).getName()));

                        if (valueIterator.hasNext())
                            component.append("\n");
                    }
                }

                player.sendMessage(new TranslatableComponent("text.multiblockmachine.accept", component), Util.NIL_UUID);
            }
        }

        return InteractionResult.PASS;
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
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.INVISIBLE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockGetter blockGetter) {
        return new InstructionBlockEntity();
    }
}
