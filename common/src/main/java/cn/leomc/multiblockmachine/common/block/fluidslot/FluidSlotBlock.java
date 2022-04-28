package cn.leomc.multiblockmachine.common.block.fluidslot;

import cn.leomc.multiblockmachine.common.api.IFluidHandler;
import dev.architectury.fluid.FluidStack;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;

public abstract class FluidSlotBlock extends Block implements EntityBlock {
    public FluidSlotBlock() {
        super(
                Properties.of(Material.METAL)
                        .requiresCorrectToolForDrops()
                        .strength(3, 5)
                        .sound(SoundType.METAL)
        );
    }

    public static boolean handleBucket(Player player, InteractionHand hand, Level level, BlockPos pos, IFluidHandler handler){
        ItemStack itemStack = player.getItemInHand(hand);
        if(itemStack.getItem() instanceof BucketItem bucketItem){
            if(bucketItem.content == Fluids.EMPTY){
                FluidStack fluid = handler.getFluid(0).copy();
                if(handler.extractFluid(0, FluidStack.create(fluid.getFluid(), 1000), true, false) >= 1000
                && handler.extractFluid(0, FluidStack.create(handler.getFluid(0), 1000), false, false) >= 1000) {
                    ItemStack stack = ItemUtils.createFilledResult(itemStack, player, new ItemStack(fluid.getFluid().getBucket()));
                    if(!stack.isEmpty())
                        player.setItemInHand(hand, stack);
                    fluid.getFluid().getPickupSound().ifPresent(sound -> level.playSound(player, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F));
                    return true;
                }
            } else if(bucketItem.content != null){
                if(handler.receiveFluid(0, FluidStack.create(bucketItem.content, 1000), true, false) >= 1000) {
                    ItemStack stack = ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.BUCKET));
                    if(!stack.isEmpty())
                        player.setItemInHand(hand, stack);
                    handler.receiveFluid(0, FluidStack.create(bucketItem.content, 1000), false, false);
                    bucketItem.playEmptySound(player, level, pos);
                    return true;
                }
            }
        }
        return false;
    }

}
