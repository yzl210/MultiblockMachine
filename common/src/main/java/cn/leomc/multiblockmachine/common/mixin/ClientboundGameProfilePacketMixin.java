package cn.leomc.multiblockmachine.common.mixin;

import cn.leomc.multiblockmachine.MultiblockMachine;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructure;
import cn.leomc.multiblockmachine.common.api.multiblock.MultiblockStructures;
import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.login.ClientLoginPacketListener;
import net.minecraft.network.protocol.login.ClientboundGameProfilePacket;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientboundGameProfilePacket.class)
public abstract class ClientboundGameProfilePacketMixin {

    public CompoundTag tag;

    @Inject(
            method = "<init>(Lcom/mojang/authlib/GameProfile;)V",
            at = @At("RETURN")
    )
    public void onInit(GameProfile gameProfile, CallbackInfo ci) {
        this.tag = new CompoundTag();
        ListTag list = new ListTag();
        for (MultiblockStructure structure : MultiblockStructures.INSTANCE.MACHINES.values())
            list.add(structure.save());
        tag.put("structures", list);
    }

    @Inject(
            method = "write",
            at = @At("HEAD")
    )
    public void onWrite(FriendlyByteBuf buf, CallbackInfo ci) {
        buf.writeNbt(this.tag);
    }

    @Inject(
            method = "read",
            at = @At("HEAD")
    )
    public void onRead(FriendlyByteBuf buf, CallbackInfo ci) {
        this.tag = buf.readAnySizeNbt();
    }

    @Inject(
            method = "handle",
            at = @At("HEAD")
    )
    public void onHandle(ClientLoginPacketListener listener, CallbackInfo ci) {
        if (tag == null)
            return;
        MultiblockMachine.LOGGER.info(tag.toString());
        MultiblockStructures.INSTANCE.MACHINES.clear();
        for (Tag t : this.tag.getList("structures", 10)) {
            CompoundTag tag = (CompoundTag) t;
            MultiblockStructure structure = MultiblockStructure.of(tag);
            MultiblockStructures.INSTANCE.MACHINES.put(new ResourceLocation(tag.getString("id")), structure);
        }
    }

}
