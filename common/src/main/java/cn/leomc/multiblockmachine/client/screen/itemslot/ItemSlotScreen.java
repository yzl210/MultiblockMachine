package cn.leomc.multiblockmachine.client.screen.itemslot;

import cn.leomc.multiblockmachine.client.screen.BaseScreen;
import cn.leomc.multiblockmachine.common.menu.itemslot.ItemSlotMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ItemSlotScreen extends BaseScreen<ItemSlotMenu> {
    public ItemSlotScreen(ItemSlotMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void init() {
        super.init();
        addSlot(40,50, 2, 18);
    }
}
