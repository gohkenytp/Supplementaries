package net.mehvahdjukaar.supplementaries.client.renderers.color;

import net.mehvahdjukaar.supplementaries.block.blocks.CogBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockDisplayReader;

public class CogBlockColor implements  IBlockColor {

    private static final int[] COLORS = new int[16];

    static {
        for(int i = 0; i <= 15; ++i) {
            float f = (float)i / 15.0F;
            float f1 = f * 0.5F + (f > 0.0F ? 0.5F : 0.3F);
            float f2 = MathHelper.clamp(f * f * 0.5F - 0.3F, 0.0F, 1.0F);
            float f3 = MathHelper.clamp(f * f * 0.6F - 0.7F, 0.0F, 1.0F);
            COLORS[i] = MathHelper.color(f1, f2, f3);
        }
    }

    @Override
    public int getColor(BlockState state, IBlockDisplayReader reader, BlockPos pos, int color) {
        return COLORS[state.getValue(CogBlock.POWER)];
    }
}