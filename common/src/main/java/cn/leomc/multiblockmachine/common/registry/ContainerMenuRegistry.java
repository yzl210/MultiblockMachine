package cn.leomc.multiblockmachine.common.registry;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.common.menu.ControllerMenu;
import cn.leomc.multiblockmachine.common.blockentity.ControllerBlockEntity;
import cn.leomc.multiblockmachine.common.menu.itemslot.ItemSlotMenu;
import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.MenuRegistry;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ContainerMenuRegistry {

    private static final DeferredRegister<MenuType<?>> CONTAINER_MENUS = DeferredRegister.create(MultiblockMachine.MODID, Registry.MENU_REGISTRY);

    public static final RegistrySupplier<MenuType<ControllerMenu>> CONTROLLER = CONTAINER_MENUS.register("controller",
            () -> MenuRegistry.ofExtended((id, inventory, data) -> {
                BlockPos pos = data.readBlockPos();
                BlockEntity blockEntity = inventory.player.getCommandSenderWorld().getBlockEntity(pos);
                if (!(blockEntity instanceof ControllerBlockEntity)) {
                    return null;
                }
                return new ControllerMenu(blockEntity, inventory.player, inventory, id);
            }));


    public static final RegistrySupplier<MenuType<ItemSlotMenu>> ITEM_SLOT = CONTAINER_MENUS.register("item_slot",
            () -> MenuRegistry.ofExtended((id, inventory, data) -> {
                BlockPos pos = data.readBlockPos();
                BlockEntity blockEntity = inventory.player.getCommandSenderWorld().getBlockEntity(pos);
                return new ItemSlotMenu(blockEntity, inventory.player, inventory, id);
            }));


    public static void register(){
        CONTAINER_MENUS.register();
    }

}
