package net.mehvahdjukaar.supplementaries.mixins;

import net.mehvahdjukaar.supplementaries.configs.RegistryConfigs;
import net.mehvahdjukaar.supplementaries.setup.ModRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StrongholdPieces;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(StrongholdPieces.RoomCrossing.class)
public abstract class StrongholdRoomMixin extends StructurePiece {

    protected StrongholdRoomMixin(IStructurePieceType p_i51342_1_, int p_i51342_2_) {
        super(p_i51342_1_, p_i51342_2_);
    }

    @Final
    @Shadow
    protected int type;

    private final BlockState sconce = ModRegistry.SCONCE_WALL.get().defaultBlockState();

    @Inject(method = "postProcess", at = @At("TAIL"), cancellable = true)
    public void postProcess(ISeedReader reader, StructureManager manager, ChunkGenerator generator, Random random, MutableBoundingBox bb, ChunkPos chunkPos, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        if (this.type == 0 && RegistryConfigs.reg.HAS_STRONGHOLD_SCONCE) {
            this.placeBlock(reader, sconce.setValue(WallTorchBlock.FACING, Direction.WEST), 4, 3, 5, bb);
            this.placeBlock(reader, sconce.setValue(WallTorchBlock.FACING, Direction.EAST), 6, 3, 5, bb);
            this.placeBlock(reader, sconce.setValue(WallTorchBlock.FACING, Direction.SOUTH), 5, 3, 4, bb);
            this.placeBlock(reader, sconce.setValue(WallTorchBlock.FACING, Direction.NORTH), 5, 3, 6, bb);
        }

    }
}