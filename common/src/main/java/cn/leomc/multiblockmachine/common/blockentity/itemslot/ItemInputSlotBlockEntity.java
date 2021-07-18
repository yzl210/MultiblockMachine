package cn.leomc.multiblockmachine.common.blockentity.itemslot;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.common.menu.itemslot.ItemSlotMenu;
import cn.leomc.multiblockmachine.common.registry.BlockEntityRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ItemInputSlotBlockEntity extends BlockEntity implements MenuProvider, IContainerProvider {

    protected SimpleContainer container;

    public ItemInputSlotBlockEntity() {
        super(BlockEntityRegistry.ITEM_INPUT_SLOT.get());
        this.container = new SimpleContainer(2);
    }


    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        super.save(compoundTag);
        compoundTag.put("container", container.createTag());
        return compoundTag;
    }

    @Override
    public void load(BlockState blockState, CompoundTag compoundTag) {
        super.load(blockState, compoundTag);
        container.fromTag(compoundTag.getList("container", 10));
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("container." + MultiblockMachine.MODID + ".item_input_slot");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new ItemSlotMenu(this, player, inventory, id);
    }

    public void dropAllItems() {
        Containers.dropContents(level, worldPosition, container);
        container.clearContent();
    }

    @Override
    public Container getContainer() {
        return container;
    }
}
