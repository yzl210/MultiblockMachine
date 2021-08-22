package cn.leomc.multiblockmachine.common.block.energyslot;

import me.shedaniel.architectury.registry.BlockProperties;
import me.shedaniel.architectury.registry.ToolType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

public abstract class EnergySlotBlock extends Block implements EntityBlock {
    public EnergySlotBlock() {
        super(
                BlockProperties.of(Material.METAL)
                        .tool(ToolType.PICKAXE, 2)
                        .requiresCorrectToolForDrops()
                        .strength(5, 6)
                        .sound(SoundType.METAL)
        );
    }
}
