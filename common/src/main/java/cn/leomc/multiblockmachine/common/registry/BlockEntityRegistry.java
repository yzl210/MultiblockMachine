package cn.leomc.multiblockmachine.common.registry;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.common.block.energyslot.EnergyInputSlotBlock;
import cn.leomc.multiblockmachine.common.block.energyslot.EnergyOutputSlotBlock;
import cn.leomc.multiblockmachine.common.block.fluidslot.FluidInputSlotBlock;
import cn.leomc.multiblockmachine.common.block.fluidslot.FluidOutputSlotBlock;
import cn.leomc.multiblockmachine.common.block.itemslot.ItemInputSlotBlock;
import cn.leomc.multiblockmachine.common.block.itemslot.ItemOutputSlotBlock;
import cn.leomc.multiblockmachine.common.blockentity.ControllerBlockEntity;
import cn.leomc.multiblockmachine.common.blockentity.energyslot.EnergyInputSlotBlockEntity;
import cn.leomc.multiblockmachine.common.blockentity.energyslot.EnergyOutputSlotBlockEntity;
import cn.leomc.multiblockmachine.common.blockentity.fluidslot.FluidInputSlotBlockEntity;
import cn.leomc.multiblockmachine.common.blockentity.fluidslot.FluidOutputSlotBlockEntity;
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
            () -> BlockEntityType.Builder.of(ItemInputSlotBlock::getBlockEntity, BlockRegistry.ITEM_INPUT_SLOT.get()).build(null));

    public static final RegistrySupplier<BlockEntityType<ItemOutputSlotBlockEntity>> ITEM_OUTPUT_SLOT = BLOCK_ENTITIES.register("item_output_slot",
            () -> BlockEntityType.Builder.of(ItemOutputSlotBlock::getBlockEntity, BlockRegistry.ITEM_OUTPUT_SLOT.get()).build(null));

    public static final RegistrySupplier<BlockEntityType<EnergyInputSlotBlockEntity>> ENERGY_INPUT_SLOT = BLOCK_ENTITIES.register("energy_input_slot",
            () -> BlockEntityType.Builder.of(EnergyInputSlotBlock::getBlockEntity, BlockRegistry.ENERGY_INPUT_SLOT.get()).build(null));

    public static final RegistrySupplier<BlockEntityType<EnergyOutputSlotBlockEntity>> ENERGY_OUTPUT_SLOT = BLOCK_ENTITIES.register("energy_output_slot",
            () -> BlockEntityType.Builder.of(EnergyOutputSlotBlock::getBlockEntity, BlockRegistry.ENERGY_OUTPUT_SLOT.get()).build(null));

    public static final RegistrySupplier<BlockEntityType<FluidInputSlotBlockEntity>> FLUID_INPUT_SLOT = BLOCK_ENTITIES.register("fluid_input_slot",
            () -> BlockEntityType.Builder.of(FluidInputSlotBlock::getBlockEntity, BlockRegistry.FLUID_INPUT_SLOT.get()).build(null));

    public static final RegistrySupplier<BlockEntityType<FluidOutputSlotBlockEntity>> FLUID_OUTPUT_SLOT = BLOCK_ENTITIES.register("fluid_output_slot",
            () -> BlockEntityType.Builder.of(FluidOutputSlotBlock::getBlockEntity, BlockRegistry.FLUID_OUTPUT_SLOT.get()).build(null));

    public static void register() {
        BLOCK_ENTITIES.register();
    }


}
