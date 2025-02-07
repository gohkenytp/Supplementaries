package net.mehvahdjukaar.supplementaries.block.tiles;

import net.mehvahdjukaar.supplementaries.block.BlockProperties;
import net.mehvahdjukaar.supplementaries.block.blocks.WallLanternBlock;
import net.mehvahdjukaar.supplementaries.block.util.IBlockHolder;
import net.mehvahdjukaar.supplementaries.setup.ModRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.util.Constants;

import java.util.Objects;


public class WallLanternBlockTile extends EnhancedLanternBlockTile implements ITickableTileEntity, IBlockHolder {

    public BlockState mimic = Blocks.LANTERN.defaultBlockState();
    public static final ModelProperty<BlockState> MIMIC = BlockProperties.MIMIC;
    public static final ModelProperty<Boolean> FANCY = BlockProperties.FANCY;

    //for charm compat
    public boolean isRedstoneLantern = false;

    static {
        maxSwingAngle = 45f;
        minSwingAngle = 1.9f;
        maxPeriod = 28f;
        angleDamping = 80f;
        periodDamping = 70f;
    }

    public WallLanternBlockTile() {
        super(ModRegistry.WALL_LANTERN_TILE.get());
    }

    @Override
    public boolean hasAnimation() {
        return this.oldRendererState;
    }

    @Override
    public IModelData getModelData() {
        //return data;
        return new ModelDataMap.Builder()
                .withInitial(MIMIC, this.getHeldBlock())
                .withInitial(FANCY, this.fancyRenderer)
                .build();
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        BlockState oldMimic = this.mimic;
        CompoundNBT tag = pkt.getTag();
        //this calls load
        handleUpdateTag(this.getBlockState(), tag);
        if (!Objects.equals(oldMimic, this.mimic)) {
            //not needed cause model data doesn't create new obj. updating old one instead
            ModelDataManager.requestModelDataRefresh(this);
            //this.data.setData(MIMIC, this.getHeldBlock());
            this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
        }
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        super.load(state, compound);
        this.mimic = NBTUtil.readBlockState(compound.getCompound("Lantern"));
        this.isRedstoneLantern = compound.getBoolean("IsRedstone");
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        compound.put("Lantern", NBTUtil.writeBlockState(mimic));
        compound.putBoolean("IsRedstone",this.isRedstoneLantern);
        return compound;
    }

    @Override
    public BlockState getHeldBlock(int index) {
        return this.mimic;
    }

    @Override
    public boolean setHeldBlock(BlockState state, int index) {
        this.mimic = state;

        int light = state.getLightEmission();
        boolean lit = true;
        if(this.mimic.getBlock().getRegistryName().toString().equals("charm:redstone_lantern")) {
            this.isRedstoneLantern = true;
            light = 15;
            lit = false;
        }
        if(this.getBlockState().getValue(WallLanternBlock.LIGHT_LEVEL)!=light)
            this.getLevel().setBlock(this.worldPosition, this.getBlockState().setValue(WallLanternBlock.LIT,lit)
                    .setValue(WallLanternBlock.LIGHT_LEVEL,light),4|16);

        return true;
    }

}