package net.mehvahdjukaar.supplementaries.client.renderers.tiles;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.mehvahdjukaar.supplementaries.block.util.IBellConnections;
import net.mehvahdjukaar.supplementaries.client.renderers.Const;
import net.mehvahdjukaar.supplementaries.client.renderers.RendererUtil;
import net.mehvahdjukaar.supplementaries.common.Textures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.tileentity.BellTileEntity;

public class BellTileMixinRenderer {

    public static final ModelRenderer chain = new ModelRenderer(16, 16, 0, 0);
    public static final ModelRenderer link = new ModelRenderer(16, 16, 0, 0);
    public static final ModelRenderer rope = new ModelRenderer(16, 16, 0, 0);

        static {
            rope.texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);
            rope.setPos(0, 6, 0);
            chain.setPos(0.0F, 0F, 0.0F);
            chain.texOffs(0, 10).addBox(-1.5F, -6.0F, 0.0F, 3.0F, 6.0F, 0.0F, 0.0F, false);
            link.setPos(0.0F, 0.0F, 0.0F);
            chain.addChild(link);
            link.yRot=-1.5708F;
            chain.xRot= (float) Math.PI;
            link.texOffs(6, 10).addBox(-1.5F, -6.0F, 0.0F, 3.0F, 6.0F, 0.0F, 0.0F, false);
        }


    public static void render(BellTileEntity tile, float partialTicks, MatrixStack matrixStackIn,
                              IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if(tile instanceof IBellConnections){

            IBellConnections.BellConnection connection = ((IBellConnections) tile).getConnected();
            if(connection==null)return;

            matrixStackIn.pushPose();
            matrixStackIn.translate(0.5, 0, 0.5);

            if(connection.isRope()) {
                //TODO: fix lighting since none of these methods are shaded properly
                IVertexBuilder builder2 = bufferIn.getBuffer(RenderType.entityCutout(Textures.BELL_ROPE_TEXTURE));

                rope.render(matrixStackIn, builder2, combinedLightIn, combinedOverlayIn, 1, 1, 1, 1);
            }
            else if(connection.isChain()){

                //combinedLightIn = WorldRenderer.getCombinedLight(tile.getWorld(), tile.getPos().down());
                int lu = combinedLightIn & '\uffff';
                int lv = combinedLightIn >> 16 & '\uffff'; // ok

                TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(AtlasTexture.LOCATION_BLOCKS).apply(Textures.CHAIN_TEXTURE);
                IVertexBuilder builder = bufferIn.getBuffer(RenderType.cutout());

                float sMinU = sprite.getU0();
                float sMinV = sprite.getV0();
                float sMaxU = sprite.getU1();
                float sMaxV = sprite.getV1();

                float atlasscaleU = sMaxU - sMinU;
                float atlasscaleV = sMaxV - sMinV;
                float minu1 = sMinU;
                float minu2 = sMinU + (3f / 16f) * atlasscaleU;
                float minv = sMinV + (11f / 16f) * atlasscaleV;
                float maxu1 = minu2;
                float maxu2 = minu2 + (3f / 16f) * atlasscaleU;
                float maxv = sMaxV;

                float w = 1.5f / 16f;
                float h = 5f / 16f;
                //Minecraft.getInstance().gameSettings.ambientOcclusionStatus.
                float col = 1f;

                matrixStackIn.mulPose(Const.Y45);

                RendererUtil.addQuadSide(builder, matrixStackIn, -w, -0, 0, w, h, 0, minu1, minv, maxu1, maxv, col, col, col, 1, lu, lv, 0, 0, 1);
                RendererUtil.addQuadSide(builder, matrixStackIn, w, -0, 0, -w, h, 0, minu1, minv, maxu1, maxv, col, col, col, 1, lu, lv, 0, 0, 1);
                matrixStackIn.mulPose(Const.YN90);
                RendererUtil.addQuadSide(builder, matrixStackIn, -w, -0, 0, w, h, 0, minu2, minv, maxu2, maxv, col, col, col, 1, lu, lv, 0, 0, 1);
                RendererUtil.addQuadSide(builder, matrixStackIn, w, -0, 0, -w, h, 0, minu2, minv, maxu2, maxv, col, col, col, 1, lu, lv, 0, 0, 1);
            }
            matrixStackIn.popPose();
        }
    }

}
