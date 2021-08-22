package cn.leomc.multiblockmachine.common.menu.fluidslot;

import cn.leomc.multiblockmachine.common.blockentity.fluidslot.FluidSlotBlockEntity;
import cn.leomc.multiblockmachine.common.menu.BaseMenu;
import cn.leomc.multiblockmachine.common.registry.ContainerMenuRegistry;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class FluidSlotMenu extends BaseMenu<FluidSlotBlockEntity> {
    public FluidSlotMenu(FluidSlotBlockEntity blockEntity, Player player, Inventory inventory, int windowId) {
        super(ContainerMenuRegistry.FLUID_SLOT.get(), blockEntity, player, inventory, windowId);
    }
}
