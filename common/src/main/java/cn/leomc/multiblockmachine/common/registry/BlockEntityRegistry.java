package cn.leomc.multiblockmachine.common.registry;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.common.blockentity.ControllerBlockEntity;
import cn.leomc.multiblockmachine.common.blockentity.itemslot.ItemInputSlotBlockEntity;
import cn.leomc.multiblockmachine.common.blockentity.itemslot.ItemOutputSlotBlockEntity;
import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.core.Registry;

import net.minecraft.world.level.block.entity.BlockEntityType;


public class BlockEntityRegistry {

    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(MultiblockMachine.MODID, Registry.BLOCK_ENTITY_TYPE_REGISTRY);


    public static final RegistrySupplier<BlockEntityType<ControllerBlockEntity>> CONTROLLER = BLOCK_ENTITIES.register("controller",
            () -> BlockEntityType.Builder.of(ControllerBlockEntity::new, BlockRegistry.CONTROLLER.get()).build(null));

    public static final RegistrySupplier<BlockEntityType<ItemInputSlotBlockEntity>> ITEM_INPUT_SLOT = BLOCK_ENTITIES.register("item_input_slot",
            () -> BlockEntityType.Builder.of(ItemInputSlotBlockEntity::new, BlockRegistry.ITEM_INPUT_SLOT.get()).build(null));

    public static final RegistrySupplier<BlockEntityType<ItemOutputSlotBlockEntity>> ITEM_OUTPUT_SLOT = BLOCK_ENTITIES.register("item_output_slot",
            () -> BlockEntityType.Builder.of(ItemOutputSlotBlockEntity::new, BlockRegistry.ITEM_OUTPUT_SLOT.get()).build(null));

    public static void register(){
        BLOCK_ENTITIES.register();
    }



}
