package cn.leomc.multiblockmachine.fabric.api;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;

public interface IItemStorage {

    Storage<ItemVariant> getItemStorage();

}
