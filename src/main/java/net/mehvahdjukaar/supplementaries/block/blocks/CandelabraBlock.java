package net.mehvahdjukaar.supplementaries.block.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.PushReaction;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.Random;

public class CandelabraBlock extends LightUpWaterBlock {
    protected static final VoxelShape SHAPE_FLOOR = Block.box(5D, 0D, 5D, 11D, 14D, 11D);
    protected static final VoxelShape SHAPE_WALL_NORTH = Block.box(5D, 0D, 11D, 11D, 14D, 16D);
    protected static final VoxelShape SHAPE_WALL_SOUTH = Block.box(5D, 0D, 0D, 11D, 14D, 5D);
    protected static final VoxelShape SHAPE_WALL_WEST = Block.box(11D, 0D, 5D, 16D, 14D, 11D);
    protected static final VoxelShape SHAPE_WALL_EAST = Block.box(0D, 0D, 5D, 5D, 14D, 11D);
    protected static final VoxelShape SHAPE_CEILING = Block.box(5D, 3D, 5D, 11D, 16D, 11D);

    public static final DirectionProperty FACING = HorizontalBlock.FACING;
    public static final EnumProperty<AttachFace> FACE = BlockStateProperties.ATTACH_FACE;

    public CandelabraBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false).setValue(LIT, true)
                .setValue(FACE, AttachFace.FLOOR).setValue(FACING, Direction.NORTH));
    }

    @Override
    public boolean isPathfindable(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        boolean flag = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        for (Direction direction : context.getNearestLookingDirections()) {
            BlockState blockstate;
            if (direction.getAxis() == Direction.Axis.Y) {
                blockstate = this.defaultBlockState().setValue(FACE, direction == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR).setValue(FACING, context.getHorizontalDirection());
            } else {
                blockstate = this.defaultBlockState().setValue(FACE, AttachFace.WALL).setValue(FACING, direction.getOpposite());
            }

            if (blockstate.canSurvive(context.getLevel(), context.getClickedPos())) {
                return blockstate.setValue(WATERLOGGED, flag).setValue(LIT, !flag);
            }
        }
        return null;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACE, FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch (state.getValue(FACE)) {
            default:
            case FLOOR:
                return SHAPE_FLOOR;
            case WALL:
                switch (state.getValue(FACING)) {
                    default:
                    case NORTH:
                        return SHAPE_WALL_NORTH;
                    case SOUTH:
                        return SHAPE_WALL_SOUTH;
                    case WEST:
                        return SHAPE_WALL_WEST;
                    case EAST:
                        return SHAPE_WALL_EAST;
                }
            case CEILING:
                return SHAPE_CEILING;
        }
    }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
        if (state.getValue(FACE) == AttachFace.FLOOR) {
            return canSupportCenter(worldIn, pos.below(), Direction.UP);
        } else if (state.getValue(FACE) == AttachFace.CEILING) {
            return RopeBlock.isSupportingCeiling(pos.above(), worldIn);
        }
        return isSideSolidForDirection(worldIn, pos, state.getValue(FACING).getOpposite());
    }



    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (!stateIn.getValue(LIT)) return;
        Direction dir1 = stateIn.getValue(FACING);
        double xm, ym, zm, xl, yl, zl, xr, zr;
        Direction dir = dir1.getClockWise();
        double xOff = dir.getStepX() * 0.3125D;
        double zOff = dir.getStepZ() * 0.3125D;
        switch (stateIn.getValue(FACE)) {
            default:
            case FLOOR:
                xm = pos.getX() + 0.5D;
                ym = pos.getY() + 1D;
                zm = pos.getZ() + 0.5D;
                xl = pos.getX() + 0.5D - xOff;
                yl = pos.getY() + 0.9375D;
                zl = pos.getZ() + 0.5D - zOff;
                xr = pos.getX() + 0.5D + xOff;
                zr = pos.getZ() + 0.5D + zOff;
                break;
            case WALL:
                double xo1 = -dir1.getStepX() * 0.3125;
                double zo2 = -dir1.getStepZ() * 0.3125;
                xm = pos.getX() + 0.5D + xo1;
                ym = pos.getY() + 1;
                zm = pos.getZ() + 0.5D + zo2;
                xl = pos.getX() + 0.5D + xo1 - xOff;
                yl = pos.getY() + 0.9375;
                zl = pos.getZ() + 0.5D + zo2 - zOff;
                xr = pos.getX() + 0.5D + xo1 + xOff;
                zr = pos.getZ() + 0.5D + zo2 + zOff;
                break;
            case CEILING:
                //high
                xm = pos.getX() + 0.5D + zOff;
                zm = pos.getZ() + 0.5D - xOff;
                ym = pos.getY() + 0.875;//0.9375D;
                //2 medium
                xl = pos.getX() + 0.5D + xOff;
                zl = pos.getZ() + 0.5D + zOff;
                xr = pos.getX() + 0.5D - zOff;
                zr = pos.getZ() + 0.5D + xOff;
                yl = pos.getY() + 0.8125;

                double xs = pos.getX() + 0.5D - xOff;
                double zs = pos.getZ() + 0.5D - zOff;
                double ys = pos.getY() + 0.75;
                worldIn.addParticle(ParticleTypes.FLAME, xs, ys, zs, 0, 0, 0);
                break;

        }
        worldIn.addParticle(ParticleTypes.FLAME, xm, ym, zm, 0, 0, 0);
        worldIn.addParticle(ParticleTypes.FLAME, xl, yl, zl, 0, 0, 0);
        worldIn.addParticle(ParticleTypes.FLAME, xr, yl, zr, 0, 0, 0);

    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        //return facing == stateIn.getValue(FACING).getOpposite() && !stateIn.canSurvive(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
        return getFacing(stateIn).getOpposite() == facing && !stateIn.canSurvive(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }


    protected static Direction getFacing(BlockState state) {
        switch (state.getValue(FACE)) {
            case CEILING:
                return Direction.DOWN;
            case FLOOR:
                return Direction.UP;
            default:
                return state.getValue(FACING);
        }
    }

    public static boolean isSideSolidForDirection(IWorldReader reader, BlockPos pos, Direction direction) {
        BlockPos blockpos = pos.relative(direction);
        return reader.getBlockState(blockpos).isFaceSturdy(reader, blockpos, direction.getOpposite());
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState p_152047_) {
        return PushReaction.DESTROY;
    }
}
