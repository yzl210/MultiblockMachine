package cn.leomc.multiblockmachine.common.blockentity;

import cn.leomc.multiblockmachine.common.api.ITickableBlockEntity;
import cn.leomc.multiblockmachine.common.api.multiblock.PositionBlock;
import cn.leomc.multiblockmachine.common.registry.BlockEntityRegistry;
import dev.architectury.hooks.block.BlockEntityHooks;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InstructionBlockEntity extends BlockEntity implements ITickableBlockEntity {

    private PositionBlock block;

    private BlockState special;

    private List<BlockState> states;

    private int ticks;

    public InstructionBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.INSTRUCTION_BLOCK.get(), pos, state);
        block = PositionBlock.of();
        states = new ArrayList<>();
        ticks = 0;
    }


    @Override
    public void clientTick(ClientLevel level, BlockPos pos, BlockState state) {
        ticks++;
        if(ticks >= states.size() * 40)
            ticks = 0;
    }


    public BlockState getMimic(){
        return special != null ? special : states.size() == 0 ? Blocks.AIR.defaultBlockState() : states.get(Math.floorDiv(ticks, 40));
    }

    public PositionBlock getBlock() {
        return block;
    }

    public BlockState getSpecial(){
        return special;
    }

    public void setBlock(PositionBlock block) {
        this.block = block;
        states = block.getAllowedBlocks()
                .stream()
                .map(Block::defaultBlockState)
                .collect(Collectors.toList());
        if(level != null && !level.isClientSide)
            BlockEntityHooks.syncData(this);
    }


    public void setSpecial(BlockState state) {
        this.special = state;
        if(level != null && !level.isClientSide)
            BlockEntityHooks.syncData(this);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        if(special != null)
            tag.putInt("state", Block.BLOCK_STATE_REGISTRY.getId(special));
        else
            tag.put("block", block.save());
    }

    @Override
    public void load(CompoundTag tag) {
        if(tag.contains("state"))
            special = Block.BLOCK_STATE_REGISTRY.byId(tag.getInt("state"));
        else
            setBlock(PositionBlock.of(tag.getCompound("block")));
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        if(special != null)
            tag.putInt("state", Block.BLOCK_STATE_REGISTRY.getId(special));
        else
            tag.put("block", block.save());
        return tag;
    }
}
