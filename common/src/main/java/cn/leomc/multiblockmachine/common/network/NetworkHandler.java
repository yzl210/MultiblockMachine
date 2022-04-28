package cn.leomc.multiblockmachine.common.network;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructures;
import cn.leomc.multiblockmachine.common.network.packet.ShowInstructionMessage;
import cn.leomc.multiblockmachine.common.network.packet.SyncStructuresMessage;
import dev.architectury.networking.NetworkChannel;
import dev.architectury.networking.NetworkManager;
import dev.architectury.utils.GameInstance;
import net.minecraft.network.Connection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;


public class NetworkHandler {
    public static NetworkChannel INSTANCE;

    public static void register() {
        INSTANCE = NetworkChannel.create(new ResourceLocation(MultiblockMachine.MODID, "main"));

        INSTANCE.register(SyncStructuresMessage.class, SyncStructuresMessage::encode, SyncStructuresMessage::decode, SyncStructuresMessage::handle);
        INSTANCE.register(ShowInstructionMessage.class, ShowInstructionMessage::encode, ShowInstructionMessage::decode, ShowInstructionMessage::handle);
    }

    public static void syncStructures() {
        if (GameInstance.getServer() == null)
            return;
        INSTANCE.sendToPlayers(GameInstance.getServer().getPlayerList().getPlayers(), new SyncStructuresMessage(MultiblockStructures.INSTANCE.MACHINES.values()));
    }

    public static void syncStructures(ServerPlayer player) {
        INSTANCE.sendToPlayer(player, new SyncStructuresMessage(MultiblockStructures.INSTANCE.MACHINES.values()));
    }

    public static void syncStructures(Connection connection){
        connection.send(INSTANCE.toPacket(NetworkManager.s2c(), new SyncStructuresMessage(MultiblockStructures.INSTANCE.MACHINES.values())));
    }


}
