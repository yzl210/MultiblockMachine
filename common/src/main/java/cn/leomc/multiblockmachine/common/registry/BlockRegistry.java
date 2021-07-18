package cn.leomc.multiblockmachine.common.registry;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.common.block.ControllerBlock;
import cn.leomc.multiblockmachine.common.block.itemslot.ItemInputSlotBlock;
import cn.leomc.multiblockmachine.common.block.itemslot.ItemOutputSlotBlock;
import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;


import java.util.function.Supplier;

public class BlockRegistry {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(MultiblockMachine.MODID, Registry.BLOCK_REGISTRY);

    public static final RegistrySupplier<Block> CONTROLLER = register("controller", ControllerBlock::new);

    public static final RegistrySupplier<Block> ITEM_INPUT_SLOT = register("item_input_slot", ItemInputSlotBlock::new);

    public static final RegistrySupplier<Block> ITEM_OUTPUT_SLOT = register("item_output_slot", ItemOutputSlotBlock::new);


    public static void register(){
        BLOCKS.register();
    }

    private static RegistrySupplier<Block> register(String name, Supplier<? extends Block> supplier){
        return BLOCKS.register(name, supplier);
    }

}
