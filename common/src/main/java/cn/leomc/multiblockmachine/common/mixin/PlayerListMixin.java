package cn.leomc.multiblockmachine.common.mixin;

import cn.leomc.multiblockmachine.common.network.NetworkHandler;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {

    @Inject(
            method = "reloadResources",
            at = @At("HEAD")
    )
    public void onReloadResources(CallbackInfo ci){
        NetworkHandler.syncStructures();
    }

    @Inject(
            method = "placeNewPlayer",
            at = @At("HEAD")
    )
    public void onPlaceNewPlayer(Connection netManager, ServerPlayer player, CallbackInfo ci){
        NetworkHandler.syncStructures(netManager);
    }

}
