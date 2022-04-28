package cn.leomc.multiblockmachine.common.registry;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.common.item.MachineItem;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class ItemRegistry {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MultiblockMachine.MODID, Registry.ITEM_REGISTRY);


    public static RegistrySupplier<Item> MACHINE_ITEM = register("machine_item", MachineItem::new);

    public static RegistrySupplier<Item> CREATIVE_ENERGY_SOURCE = register("creative_energy_source", () -> new BlockItem(BlockRegistry.CREATIVE_ENERGY_SOURCE.get(), new Item.Properties().tab(ModRegistry.TAB)));


    public static RegistrySupplier<Item> CONTROLLER = register("controller", () -> new BlockItem(BlockRegistry.CONTROLLER.get(), new Item.Properties().tab(ModRegistry.TAB)));

    public static RegistrySupplier<Item> ITEM_INPUT_SLOT = register("item_input_slot", () -> new BlockItem(BlockRegistry.ITEM_INPUT_SLOT.get(), new Item.Properties().tab(ModRegistry.TAB)));

    public static RegistrySupplier<Item> ITEM_OUTPUT_SLOT = register("item_output_slot", () -> new BlockItem(BlockRegistry.ITEM_OUTPUT_SLOT.get(), new Item.Properties().tab(ModRegistry.TAB)));

    public static RegistrySupplier<Item> ENERGY_INPUT_SLOT = register("energy_input_slot", () -> new BlockItem(BlockRegistry.ENERGY_INPUT_SLOT.get(), new Item.Properties().tab(ModRegistry.TAB)));

    public static RegistrySupplier<Item> ENERGY_OUTPUT_SLOT = register("energy_output_slot", () -> new BlockItem(BlockRegistry.ENERGY_OUTPUT_SLOT.get(), new Item.Properties().tab(ModRegistry.TAB)));

    public static RegistrySupplier<Item> FLUID_INPUT_SLOT = register("fluid_input_slot", () -> new BlockItem(BlockRegistry.FLUID_INPUT_SLOT.get(), new Item.Properties().tab(ModRegistry.TAB)));

    public static RegistrySupplier<Item> FLUID_OUTPUT_SLOT = register("fluid_output_slot", () -> new BlockItem(BlockRegistry.FLUID_OUTPUT_SLOT.get(), new Item.Properties().tab(ModRegistry.TAB)));


    public static void register() {
        ITEMS.register();
    }

    private static RegistrySupplier<Item> register(String name, Supplier<? extends Item> supplier) {
        return ITEMS.register(name, supplier);
    }

}
