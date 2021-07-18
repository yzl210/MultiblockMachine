package cn.leomc.multiblockmachine.common.blockentity;

import cn.leomc.multiblockmachine.common.api.MultiblockStructure;
import cn.leomc.multiblockmachine.common.api.MultiblockStructures;
import cn.leomc.multiblockmachine.common.block.ControllerBlock;
import cn.leomc.multiblockmachine.common.menu.ControllerMenu;
import cn.leomc.multiblockmachine.common.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import org.jetbrains.annotations.Nullable;

public class ControllerBlockEntity extends BlockEntity implements TickableBlockEntity, MenuProvider {
    public ControllerBlockEntity() {
        super(BlockEntityRegistry.CONTROLLER.get());
    }

    protected boolean formed = false;

    protected MultiblockStructure structure;

    @Override
    public void tick() {

        Direction direction = getBlockState().getValue(ControllerBlock.FACING);

        if(!formed && MultiblockStructures.TESTSTRUCTURE.isFormed(level, worldPosition, direction)){
            formed = true;
            structure = MultiblockStructures.TESTSTRUCTURE;
            level.setBlockAndUpdate(worldPosition.offset(new BlockPos(0, 5, 0)), Blocks.STONE.defaultBlockState());
        }

        if(formed && !MultiblockStructures.TESTSTRUCTURE.isFormed(level, worldPosition, direction)){
            formed = false;
            structure = null;
            level.setBlockAndUpdate(worldPosition.offset(new BlockPos(0, 5, 0)), Blocks.AIR.defaultBlockState());
        }
    }

    public boolean isFormed() {
        return formed;
    }

    @Nullable
    public MultiblockStructure getStructure() {
        return structure;
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("multiblockmachine." + structure.getId().getNamespace() + "." + structure.getId().getPath());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new ControllerMenu(this, player, inventory, id);
    }
}
