package cn.leomc.multiblockmachine.common.menu.energyslot;

import cn.leomc.multiblockmachine.common.blockentity.energyslot.EnergySlotBlockEntity;
import cn.leomc.multiblockmachine.common.menu.BaseMenu;
import cn.leomc.multiblockmachine.common.registry.ContainerMenuRegistry;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class EnergySlotMenu extends BaseMenu<EnergySlotBlockEntity> {
    public EnergySlotMenu(EnergySlotBlockEntity blockEntity, Player player, Inventory inventory, int windowId) {
        super(ContainerMenuRegistry.ENERGY_SLOT.get(), blockEntity, player, inventory, windowId);
    }


}
