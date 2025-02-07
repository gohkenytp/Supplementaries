package net.mehvahdjukaar.supplementaries.mixins;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.mehvahdjukaar.supplementaries.client.renderers.items.SlingshotRendererHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.math.vector.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Shadow @Final private Minecraft minecraft;

    @Inject(
            method = "renderLevel",
            at = @At(value = "FIELD",
                    target = "Lnet/minecraft/client/Minecraft;hitResult:Lnet/minecraft/util/math/RayTraceResult;",
                    ordinal = 1
            ),
            cancellable = true
    )
    private void renderOutline(MatrixStack matrixStack, float p_228426_2_, long p_228426_3_, boolean blockOutlines, ActiveRenderInfo camera, GameRenderer renderer, LightTexture p_228426_8_, Matrix4f p_228426_9_, CallbackInfo ci) {
        if(blockOutlines) SlingshotRendererHelper.renderBlockOutline(matrixStack, camera, this.minecraft);
    }
}
