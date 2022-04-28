package cn.leomc.multiblockmachine.fabric.common.blockentity.itemslot;

import cn.leomc.multiblockmachine.common.blockentity.itemslot.ItemInputSlotBlockEntity;
import cn.leomc.multiblockmachine.fabric.api.IItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;


public class FabricItemInputSlotBlockEntity extends ItemInputSlotBlockEntity implements IItemStorage {

    private Storage<ItemVariant> handler = InventoryStorage.of(container, null);

    public FabricItemInputSlotBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public Storage<ItemVariant> getItemStorage() {
        return handler;
    }
}
