package cn.leomc.multiblockmachine.common.blockentity;

import cn.leomc.multiblockmachine.common.api.multiblock.PositionBlock;
import cn.leomc.multiblockmachine.common.registry.BlockEntityRegistry;
import me.shedaniel.architectury.extensions.BlockEntityExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class InstructionBlockEntity extends BlockEntity implements TickableBlockEntity, BlockEntityExtension {

    private PositionBlock block;

    private BlockState special;

    private List<BlockState> states;

    private int ticks;

    public InstructionBlockEntity() {
        super(BlockEntityRegistry.INSTRUCTION_BLOCK.get());
        this.block = PositionBlock.of();
        states = new ArrayList<>();
    }

    @Override
    public void setLevelAndPosition(Level level, BlockPos blockPos) {
        super.setLevelAndPosition(level, blockPos);
        if(!level.isClientSide)
            syncData();
    }

    @Override
    public void tick() {
        if(level.isClientSide){
            ticks++;
            if(ticks >= states.size() * 40)
                ticks = 0;
        }
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
    }


    public void setSpecial(BlockState state) {
        this.special = state;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        if(special != null)
            tag.putInt("state", Block.BLOCK_STATE_REGISTRY.getId(special));
        else
            tag.put("block", block.save());
        return super.save(tag);
    }

    @Override
    public void load(BlockState state, CompoundTag tag) {
        super.load(state, tag);
        if(tag.contains("state"))
            special = Block.BLOCK_STATE_REGISTRY.byId(tag.getInt("state"));
        else
            setBlock(PositionBlock.of(tag.getCompound("block")));
    }

    @Override
    public void loadClientData(BlockState state, CompoundTag tag) {
        if(tag.contains("state"))
            special = Block.BLOCK_STATE_REGISTRY.byId(tag.getInt("state"));
        else
            setBlock(PositionBlock.of(tag.getCompound("block")));    }

    @Override
    public CompoundTag saveClientData(CompoundTag tag) {
        if(special != null)
            tag.putInt("state", Block.BLOCK_STATE_REGISTRY.getId(special));
        else
            tag.put("block", block.save());
        return tag;
    }


}
