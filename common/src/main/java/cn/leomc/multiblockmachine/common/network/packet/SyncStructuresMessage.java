package cn.leomc.multiblockmachine.common.network.packet;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructure;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructures;
import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.function.Supplier;

public class SyncStructuresMessage {

    public CompoundTag tag;

    public SyncStructuresMessage(Collection<MultiblockStructure> structures) {
        this.tag = new CompoundTag();
        ListTag list = new ListTag();
        for (MultiblockStructure structure : structures)
            list.add(structure.save());
        tag.put("structures", list);
    }

    public SyncStructuresMessage(CompoundTag tag) {
        this.tag = tag;
    }

    public static void encode(SyncStructuresMessage message, FriendlyByteBuf buf) {
        buf.writeNbt(message.tag);
    }

    public static SyncStructuresMessage decode(FriendlyByteBuf buf) {
        return new SyncStructuresMessage(buf.readAnySizeNbt());
    }


    public static void handle(SyncStructuresMessage message, Supplier<NetworkManager.PacketContext> supplier) {
        MultiblockMachine.LOGGER.info(message.tag.toString());
        MultiblockStructures.INSTANCE.MACHINES.clear();
        for (Tag t : message.tag.getList("structures", 10)) {
            CompoundTag tag = (CompoundTag) t;
            MultiblockStructure structure = MultiblockStructure.of(tag);
            MultiblockStructures.INSTANCE.MACHINES.put(new ResourceLocation(tag.getString("id")), structure);
            MultiblockMachine.LOGGER.info("Added: " + structure.getId());
        }
        MultiblockMachine.LOGGER.info("total: " + MultiblockStructures.INSTANCE.MACHINES.size());
    }
}
