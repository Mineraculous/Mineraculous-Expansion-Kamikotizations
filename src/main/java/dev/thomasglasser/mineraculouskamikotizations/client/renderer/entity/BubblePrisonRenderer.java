package dev.thomasglasser.mineraculouskamikotizations.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.thomasglasser.mineraculous.impl.client.MineraculousClientUtils;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.mineraculouskamikotizations.world.entity.BubblePrison;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class BubblePrisonRenderer extends EntityRenderer<BubblePrison> {
    public BubblePrisonRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    private static final ResourceLocation GREEN_BUBBLE_TEXTURE = MineraculousKamikotizations.modLoc("textures/entity/green_bubble.png");
    private static final ResourceLocation RED_BUBBLE_TEXTURE = MineraculousKamikotizations.modLoc("textures/entity/red_bubble.png");
    private static final ResourceLocation PURPLE_BUBBLE_TEXTURE = MineraculousKamikotizations.modLoc("textures/entity/purple_bubble.png");

    @Override
    public ResourceLocation getTextureLocation(BubblePrison entity) {
        return null;
    }

    @Override
    public void render(BubblePrison entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        Vec3 center = new Vec3(0, entity.getBbHeight() / 2d, 0);
        MineraculousClientUtils.rotateFacingCamera(poseStack, center, 0);
        ResourceLocation texture = getTexture(entity);
        VertexConsumer vertex = bufferSource.getBuffer(RenderType.entityTranslucent(texture));
        double quadSize = 2.1;
        poseStack.pushPose();
        MineraculousClientUtils.vertex(vertex, poseStack.last(), center.add(-quadSize, quadSize, 0), 0, 0, packedLight);
        MineraculousClientUtils.vertex(vertex, poseStack.last(), center.add(quadSize, quadSize, 0), 1, 0, packedLight);
        MineraculousClientUtils.vertex(vertex, poseStack.last(), center.add(quadSize, -quadSize, 0), 1, 1, packedLight);
        MineraculousClientUtils.vertex(vertex, poseStack.last(), center.add(-quadSize, -quadSize, 0), 0, 1, packedLight);
        poseStack.popPose();
        poseStack.popPose(); //TODO remove once rotateFacingCamera is fixed on Mineraculous
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    ResourceLocation getTexture(BubblePrison entity) {
        return switch (entity.getBehavior()) {
            case CAPTURING -> PURPLE_BUBBLE_TEXTURE;
            case EXPLODING -> RED_BUBBLE_TEXTURE;
            case TARGETING -> GREEN_BUBBLE_TEXTURE;
        };
    }
}
