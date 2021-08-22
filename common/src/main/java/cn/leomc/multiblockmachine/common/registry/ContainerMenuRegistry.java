package cn.leomc.multiblockmachine.common.registry;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.common.blockentity.ControllerBlockEntity;
import cn.leomc.multiblockmachine.common.blockentity.energyslot.EnergySlotBlockEntity;
import cn.leomc.multiblockmachine.common.blockentity.fluidslot.FluidSlotBlockEntity;
import cn.leomc.multiblockmachine.common.blockentity.itemslot.ItemSlotBlockEntity;
import cn.leomc.multiblockmachine.common.menu.ControllerMenu;
import cn.leomc.multiblockmachine.common.menu.energyslot.EnergySlotMenu;
import cn.leomc.multiblockmachine.common.menu.fluidslot.FluidSlotMenu;
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
                return new ControllerMenu((ControllerBlockEntity) blockEntity, inventory.player, inventory, id);
            }));


    public static final RegistrySupplier<MenuType<ItemSlotMenu>> ITEM_SLOT = CONTAINER_MENUS.register("item_slot",
            () -> MenuRegistry.ofExtended((id, inventory, data) -> {
                BlockPos pos = data.readBlockPos();
                BlockEntity blockEntity = inventory.player.getCommandSenderWorld().getBlockEntity(pos);
                if (!(blockEntity instanceof ItemSlotBlockEntity))
                    return null;
                return new ItemSlotMenu((ItemSlotBlockEntity) blockEntity, inventory.player, inventory, id);
            }));

    public static final RegistrySupplier<MenuType<EnergySlotMenu>> ENERGY_SLOT = CONTAINER_MENUS.register("energy_slot",
            () -> MenuRegistry.ofExtended((id, inventory, data) -> {
                BlockPos pos = data.readBlockPos();
                BlockEntity blockEntity = inventory.player.getCommandSenderWorld().getBlockEntity(pos);
                if (!(blockEntity instanceof EnergySlotBlockEntity))
                    return null;
                return new EnergySlotMenu((EnergySlotBlockEntity) blockEntity, inventory.player, inventory, id);
            }));

    public static final RegistrySupplier<MenuType<FluidSlotMenu>> FLUID_SLOT = CONTAINER_MENUS.register("fluid_slot",
            () -> MenuRegistry.ofExtended((id, inventory, data) -> {
                BlockPos pos = data.readBlockPos();
                BlockEntity blockEntity = inventory.player.getCommandSenderWorld().getBlockEntity(pos);
                if (!(blockEntity instanceof FluidSlotBlockEntity))
                    return null;
                return new FluidSlotMenu((FluidSlotBlockEntity) blockEntity, inventory.player, inventory, id);
            }));

    public static void register() {
        CONTAINER_MENUS.register();
    }

}
