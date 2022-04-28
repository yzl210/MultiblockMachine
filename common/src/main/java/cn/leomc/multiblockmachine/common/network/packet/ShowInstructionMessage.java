package cn.leomc.multiblockmachine.common.network.packet;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructure;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructures;
import dev.architectury.event.EventResult;
import dev.architectury.networking.NetworkManager;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class ShowInstructionMessage {

    private static final Map<UUID, Long> INTERVALS = new HashMap<>();
    private static final Map<Player, MultiblockStructure> REQUESTS = new HashMap<>();

    public ResourceLocation machine;

    public ShowInstructionMessage(ResourceLocation machine){
        this.machine = machine;
    }

    public static void encode(ShowInstructionMessage message, FriendlyByteBuf buf) {
        buf.writeResourceLocation(message.machine);
    }

    public static ShowInstructionMessage decode(FriendlyByteBuf buf) {
        return new ShowInstructionMessage(buf.readResourceLocation());
    }


    public static void handle(ShowInstructionMessage message, Supplier<NetworkManager.PacketContext> supplier) {
        ServerPlayer player = (ServerPlayer) supplier.get().getPlayer();
        UUID uuid = player.getUUID();
        long interval = MultiblockMachine.CONFIG.common.miscellaneous.show_instruction_interval;
        if(interval < 1 || !INTERVALS.containsKey(uuid) || System.currentTimeMillis() - INTERVALS.get(uuid) > interval){
            if(interval > 0)
                INTERVALS.put(uuid, System.currentTimeMillis());
            MultiblockStructure structure = MultiblockStructures.getStructure(message.machine);
            if(structure != null) {
                player.sendMessage(new TranslatableComponent("text.multiblockmachine.show_instruction"), Util.NIL_UUID);
                REQUESTS.put(player, structure);
            }
        }
    }


    public static void onPlayerQuit(ServerPlayer player) {
        REQUESTS.remove(player);
    }

    public static EventResult onRightClickBlock(Player player, InteractionHand hand, BlockPos pos, Direction direction) {
        if(player.getLevel() instanceof ServerLevel level && REQUESTS.containsKey(player)) {
            MultiblockStructures.buildStructureInstruction(REQUESTS.get(player), level, pos, player.getDirection().getOpposite());
            REQUESTS.remove(player);
            return EventResult.interruptFalse();
        }
        return EventResult.pass();
    }
}

