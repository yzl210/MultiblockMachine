package cn.leomc.multiblockmachine.common.registry;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.common.block.ControllerBlock;
import cn.leomc.multiblockmachine.common.block.CreativeEnergySourceBlock;
import cn.leomc.multiblockmachine.common.block.InstructionBlock;
import cn.leomc.multiblockmachine.common.block.energyslot.EnergyInputSlotBlock;
import cn.leomc.multiblockmachine.common.block.energyslot.EnergyOutputSlotBlock;
import cn.leomc.multiblockmachine.common.block.fluidslot.FluidInputSlotBlock;
import cn.leomc.multiblockmachine.common.block.fluidslot.FluidOutputSlotBlock;
import cn.leomc.multiblockmachine.common.block.itemslot.ItemInputSlotBlock;
import cn.leomc.multiblockmachine.common.block.itemslot.ItemOutputSlotBlock;
import cn.leomc.multiblockmachine.common.blockentity.CreativeEnergySourceBlockEntity;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class BlockRegistry {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(MultiblockMachine.MODID, Registry.BLOCK_REGISTRY);


    public static final RegistrySupplier<Block> CREATIVE_ENERGY_SOURCE = register("creative_energy_source", CreativeEnergySourceBlock::new);


    public static final RegistrySupplier<Block> CONTROLLER = register("controller", ControllerBlock::new);

    public static final RegistrySupplier<Block> INSTRUCTION_BLOCK = register("instruction_block", InstructionBlock::new);

    public static final RegistrySupplier<Block> ITEM_INPUT_SLOT = register("item_input_slot", ItemInputSlotBlock::new);

    public static final RegistrySupplier<Block> ITEM_OUTPUT_SLOT = register("item_output_slot", ItemOutputSlotBlock::new);

    public static final RegistrySupplier<Block> ENERGY_INPUT_SLOT = register("energy_input_slot", EnergyInputSlotBlock::new);

    public static final RegistrySupplier<Block> ENERGY_OUTPUT_SLOT = register("energy_output_slot", EnergyOutputSlotBlock::new);

    public static final RegistrySupplier<Block> FLUID_INPUT_SLOT = register("fluid_input_slot", FluidInputSlotBlock::new);

    public static final RegistrySupplier<Block> FLUID_OUTPUT_SLOT = register("fluid_output_slot", FluidOutputSlotBlock::new);


    public static void register() {
        BLOCKS.register();
    }

    private static RegistrySupplier<Block> register(String name, Supplier<? extends Block> supplier) {
        return BLOCKS.register(name, supplier);
    }

}
