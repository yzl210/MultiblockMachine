package cn.leomc.multiblockmachine.common.registry;

import cn.leomc.multiblockmachine.MultiblockMachine;
import me.shedaniel.architectury.registry.CreativeTabs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;


public class ModRegistry {


    public static final CreativeModeTab TAB = CreativeTabs.create(new ResourceLocation(MultiblockMachine.MODID, "default"), () -> new ItemStack(ItemRegistry.CONTROLLER.get()));

    public static void register() {
        BlockRegistry.register();
        ItemRegistry.register();
        BlockEntityRegistry.register();
        ContainerMenuRegistry.register();
    }


}
