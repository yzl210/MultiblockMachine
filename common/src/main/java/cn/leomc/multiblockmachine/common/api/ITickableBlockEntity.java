package cn.leomc.multiblockmachine.common.api;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public interface ITickableBlockEntity {

    default void serverTick(ServerLevel level, BlockPos pos, BlockState state){
    }

    default void clientTick(ClientLevel level, BlockPos pos, BlockState state){
    }


}
