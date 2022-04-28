package cn.leomc.multiblockmachine.common.block.energyslot;

import dev.architectury.registry.block.BlockProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

public abstract class EnergySlotBlock extends Block implements EntityBlock {
    public EnergySlotBlock() {
        super(
                Properties.of(Material.METAL)
                        .requiresCorrectToolForDrops()
                        .strength(3, 5)
                        .sound(SoundType.METAL)
        );
    }
}
