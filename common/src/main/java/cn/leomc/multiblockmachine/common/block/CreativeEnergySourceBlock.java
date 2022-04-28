package cn.leomc.multiblockmachine.common.block;

import cn.leomc.multiblockmachine.common.blockentity.ControllerBlockEntity;
import cn.leomc.multiblockmachine.common.blockentity.CreativeEnergySourceBlockEntity;
import cn.leomc.multiblockmachine.common.blockentity.itemslot.ItemInputSlotBlockEntity;
import cn.leomc.multiblockmachine.common.utils.PlatformSpecific;
import cn.leomc.multiblockmachine.common.utils.Utils;
import dev.architectury.registry.menu.MenuRegistry;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;


public class CreativeEnergySourceBlock extends Block implements EntityBlock {
    public CreativeEnergySourceBlock() {
        super(Properties.of(Material.METAL)
                .noDrops()
                .strength(4, 5)
                .sound(SoundType.METAL)
        );
    }

    public static CreativeEnergySourceBlockEntity getBlockEntity(BlockPos pos, BlockState state) {
        return (CreativeEnergySourceBlockEntity) PlatformSpecific.getBlockEntity(PlatformSpecific.BlockEntities.CREATIVE_ENERGY_SOURCE, pos, state);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return getBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return Utils.getTicker(level.isClientSide);
    }

}
