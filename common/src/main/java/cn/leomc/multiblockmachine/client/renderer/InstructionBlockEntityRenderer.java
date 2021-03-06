package cn.leomc.multiblockmachine.client.renderer;

import cn.leomc.multiblockmachine.common.blockentity.InstructionBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;


public class InstructionBlockEntityRenderer implements BlockEntityRenderer<InstructionBlockEntity> {

    @Override
    public void render(InstructionBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int combinedLight, int combinedOverlay) {

        poseStack.scale(0.65F, 0.65F, 0.65F);
        poseStack.translate(0.269, 0.269, 0.269);

        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(
                blockEntity.getMimic(),
                poseStack,
                multiBufferSource,
                combinedLight,
                combinedOverlay
        );

    }
}
