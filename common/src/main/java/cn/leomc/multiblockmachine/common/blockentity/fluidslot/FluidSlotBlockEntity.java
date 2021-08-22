package cn.leomc.multiblockmachine.common.blockentity.fluidslot;

import cn.leomc.multiblockmachine.common.api.*;
import cn.leomc.multiblockmachine.common.menu.fluidslot.FluidSlotMenu;
import cn.leomc.multiblockmachine.common.utils.Utils;
import me.shedaniel.architectury.extensions.BlockEntityExtension;
import me.shedaniel.architectury.fluid.FluidStack;
import me.shedaniel.architectury.utils.Fraction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

public abstract class FluidSlotBlockEntity extends BlockEntity implements MenuProvider, IFluidSlot, BlockEntityExtension, TickableBlockEntity {

    protected FluidHandler fluid;

    public FluidSlotBlockEntity(BlockEntityType<?> blockEntityType) {
        super(blockEntityType);
        fluid = new FluidHandler(Fluids.EMPTY, 10000, Fraction.of(0, 1));
    }

    @Override
    public void tick() {
        if (level.isClientSide)
            return;
        syncData();
    }

    public FluidHandler getFluid(){
        return fluid;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new FluidSlotMenu(this, player, inventory, id);
    }

    @Override
    public void load(BlockState state, CompoundTag tag) {
        super.load(state, tag);
        Fluid fluid = Registry.FLUID.getOptional(new ResourceLocation(tag.getString("fluid"))).orElse(Fluids.EMPTY);
        Fraction fraction = Fraction.ofWhole(tag.getLong("amount"));
        this.fluid.setFluid(fluid, fraction);
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putString("fluid", Registry.FLUID.getKey(fluid.getFluidStack().getFluid()).toString());
        tag.putLong("amount", fluid.getFluidStack().getAmount().getNumerator());
        return super.save(tag);
    }

    @Override
    public void loadClientData(BlockState state, CompoundTag tag) {
        Fluid fluid = Registry.FLUID.getOptional(new ResourceLocation(tag.getString("fluid"))).orElse(Fluids.EMPTY);
        Fraction fraction = Fraction.ofWhole(tag.getLong("amount"));
        this.fluid.setFluid(fluid, fraction);
    }

    @Override
    public CompoundTag saveClientData(CompoundTag tag) {
        tag.putString("fluid", Registry.FLUID.getKey(fluid.getFluidStack().getFluid()).toString());
        tag.putLong("amount", fluid.getFluidStack().getAmount().getNumerator());
        return tag;
    }

}
