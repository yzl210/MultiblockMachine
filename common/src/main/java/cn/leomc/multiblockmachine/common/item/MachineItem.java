package cn.leomc.multiblockmachine.common.item;

import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructure;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructures;
import cn.leomc.multiblockmachine.common.registry.ModRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MachineItem extends Item {

    public MachineItem() {
        super(new Properties().stacksTo(1).tab(ModRegistry.TAB));
    }


    @Override
    public InteractionResult useOn(UseOnContext context) {
        if(context.getPlayer() != null && context.getPlayer().isCreative()){
            ItemStack itemStack = context.getItemInHand();
            if(!itemStack.getOrCreateTag().contains("machine"))
                return InteractionResult.PASS;
            MultiblockStructure structure = MultiblockStructures.getStructure(new ResourceLocation(itemStack.getOrCreateTag().getString("machine")));
            if(context.getLevel().isClientSide)
                return structure == null ? InteractionResult.PASS : InteractionResult.CONSUME;

            BlockPos pos = context.getClickedPos().offset(0, 1, 0);

            if(context.getPlayer().isShiftKeyDown())
                MultiblockStructures.buildStructure(structure, (ServerLevel) context.getLevel(), pos, context.getHorizontalDirection().getOpposite());
            else
                MultiblockStructures.buildStructureInstruction(structure, (ServerLevel) context.getLevel(), pos, context.getHorizontalDirection().getOpposite());
        }

        return InteractionResult.PASS;
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> nonNullList) {
        if (tab == ModRegistry.TAB) {
            for (ResourceLocation id : MultiblockStructures.INSTANCE.MACHINES.keySet()) {
                ItemStack itemStack = new ItemStack(this);
                CompoundTag tag = itemStack.getOrCreateTag();
                tag.putString("machine", id.toString());
                nonNullList.add(itemStack);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        if (itemStack.getItem() instanceof MachineItem && itemStack.getOrCreateTag().contains("machine"))
            list.add(MultiblockStructures.getStructureName(new ResourceLocation(itemStack.getOrCreateTag().getString("machine"))).withStyle(ChatFormatting.GRAY));
    }

}
