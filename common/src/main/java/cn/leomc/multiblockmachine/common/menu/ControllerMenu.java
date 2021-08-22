package cn.leomc.multiblockmachine.common.menu;

import cn.leomc.multiblockmachine.common.blockentity.ControllerBlockEntity;
import cn.leomc.multiblockmachine.common.registry.BlockRegistry;
import cn.leomc.multiblockmachine.common.registry.ContainerMenuRegistry;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;

public class ControllerMenu extends BaseMenu<ControllerBlockEntity> {


    public ControllerMenu(ControllerBlockEntity tileEntity, Player player, Inventory playerInventory, int windowId) {
        super(ContainerMenuRegistry.CONTROLLER.get(), tileEntity, player, playerInventory, windowId);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), player, BlockRegistry.CONTROLLER.get());
    }

}
