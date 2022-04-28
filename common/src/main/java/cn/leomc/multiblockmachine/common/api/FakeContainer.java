package cn.leomc.multiblockmachine.common.api;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class FakeContainer implements Container {

    private Container container;
    private IFluidHandler fluidHandler;
    public FakeContainer(Container container, IFluidHandler handler){
        this.container = container;
        this.fluidHandler = handler;
    }

    public Container getContainer() {
        return container;
    }

    public SimpleContainer getSimpleContainer() {
        SimpleContainer sc = new SimpleContainer(container.getContainerSize());
        for(int i = 0;i < container.getContainerSize();i++)
            sc.setItem(i, container.getItem(i).copy());
        return sc;
    }

    public IFluidHandler getFluidHandler() {
        return fluidHandler;
    }

    @Override
    public int getContainerSize() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }

    @Override
    public void clearContent() {
    }
}
