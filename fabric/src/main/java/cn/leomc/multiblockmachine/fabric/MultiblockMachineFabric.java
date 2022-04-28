package cn.leomc.multiblockmachine.fabric;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.common.api.recipe.MachineRecipe;
import cn.leomc.multiblockmachine.common.api.recipe.MachineRecipeType;
import cn.leomc.multiblockmachine.common.registry.BlockEntityRegistry;
import cn.leomc.multiblockmachine.fabric.api.IEnergyStorage;
import cn.leomc.multiblockmachine.fabric.api.IFluidStorage;
import cn.leomc.multiblockmachine.fabric.api.IItemStorage;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.core.Registry;
import team.reborn.energy.api.EnergyStorage;

public class MultiblockMachineFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        new MultiblockMachine();

        Registry.register(Registry.RECIPE_TYPE, MachineRecipeType.ID, MachineRecipeType.INSTANCE);
        Registry.register(Registry.RECIPE_SERIALIZER, MachineRecipeType.ID, MachineRecipe.Serializer.INSTANCE);

        ItemStorage.SIDED.registerForBlockEntities(((blockEntity, context) -> blockEntity instanceof IItemStorage iItemStorage ? iItemStorage.getItemStorage() : null),
                BlockEntityRegistry.ITEM_INPUT_SLOT.get(), BlockEntityRegistry.ITEM_OUTPUT_SLOT.get());

        FluidStorage.SIDED.registerForBlockEntities(((blockEntity, context) -> blockEntity instanceof IFluidStorage iFluidStorage ? iFluidStorage.getFluidStorage() : null),
                BlockEntityRegistry.FLUID_INPUT_SLOT.get(), BlockEntityRegistry.FLUID_OUTPUT_SLOT.get());

        EnergyStorage.SIDED.registerForBlockEntities(((blockEntity, context) -> blockEntity instanceof IEnergyStorage iEnergyStorage ? iEnergyStorage.getEnergyStorage() : null),
                BlockEntityRegistry.ENERGY_INPUT_SLOT.get(), BlockEntityRegistry.ENERGY_OUTPUT_SLOT.get());
    }
}
