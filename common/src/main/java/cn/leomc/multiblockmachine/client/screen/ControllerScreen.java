package cn.leomc.multiblockmachine.client.screen;

import cn.leomc.multiblockmachine.common.menu.ControllerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ControllerScreen extends BaseScreen<ControllerMenu>{
    public ControllerScreen(ControllerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }
}
