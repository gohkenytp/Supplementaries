package net.mehvahdjukaar.supplementaries.block.tiles;

import com.mojang.datafixers.util.Pair;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.block.blocks.FlagBlock;
import net.mehvahdjukaar.supplementaries.setup.ModRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.INameable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class FlagBlockTile extends TileEntity implements INameable {

    //client side param
    public final float offset = 3f * (MathHelper.sin(this.worldPosition.getX()) + MathHelper.sin(this.worldPosition.getZ()));
    public float counter = 0;
    @Nullable
    private ITextComponent name;
    @Nullable
    private DyeColor baseColor = null;
    @Nullable
    private ListNBT itemPatterns;
    private boolean receivedData;
    @Nullable
    private List<Pair<BannerPattern, DyeColor>> patterns;

    public FlagBlockTile() {
        super(ModRegistry.FLAG_TILE.get());
    }

    public FlagBlockTile(DyeColor color) {
        this();
        this.baseColor = color;
    }

    public static ResourceLocation getFlagLocation(BannerPattern pattern){
        return new ResourceLocation(Supplementaries.MOD_ID, "textures/entity/flags/"+ pattern.getFilename()+".png");
    }

    public void setCustomName(ITextComponent p_213136_1_) {
        this.name = p_213136_1_;
    }

    @OnlyIn(Dist.CLIENT)
    public void fromItem(ItemStack stack, DyeColor color) {
        this.itemPatterns = BannerTileEntity.getItemPatterns(stack);
        this.baseColor = color;
        this.patterns = null;
        this.receivedData = true;
    }

    @OnlyIn(Dist.CLIENT)
    public List<Pair<BannerPattern, DyeColor>> getPatterns() {
        if (this.patterns == null && this.receivedData) {
            this.patterns = BannerTileEntity.createPatterns(this.getBaseColor(this::getBlockState), this.itemPatterns);
        }
        return this.patterns;
    }


    public ItemStack getItem(BlockState state) {
        ItemStack itemstack = new ItemStack(FlagBlock.byColor(this.getBaseColor(() -> state)));
        if (this.itemPatterns != null && !this.itemPatterns.isEmpty()) {
            itemstack.getOrCreateTagElement("BlockEntityTag").put("Patterns", this.itemPatterns.copy());
        }
        if (this.name != null) {
            itemstack.setHoverName(this.name);
        }
        return itemstack;
    }

    public DyeColor getBaseColor(Supplier<BlockState> state) {
        if (this.baseColor == null) {
            this.baseColor = ((FlagBlock)state.get().getBlock()).getColor();
        }
        return this.baseColor;
    }

    @Override
    public CompoundNBT save(CompoundNBT compoundNBT) {
        super.save(compoundNBT);
        if (this.itemPatterns != null) {
            compoundNBT.put("Patterns", this.itemPatterns);
        }
        if (this.name != null) {
            compoundNBT.putString("CustomName", ITextComponent.Serializer.toJson(this.name));
        }
        return compoundNBT;
    }

    @Override
    public void load(BlockState state, CompoundNBT compoundNBT) {
        super.load(state, compoundNBT);
        if (compoundNBT.contains("CustomName", 8)) {
            this.name = ITextComponent.Serializer.fromJson(compoundNBT.getString("CustomName"));
        }
        if (this.hasLevel()) {
            this.baseColor = ((FlagBlock)this.getBlockState().getBlock()).getColor();
        } else {
            this.baseColor = null;
        }
        this.itemPatterns = compoundNBT.getList("Patterns", 10);
        this.patterns = null;
        this.receivedData = true;
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, 0, this.getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.save(new CompoundNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.load(this.getBlockState(), pkt.getTag());
    }

    @Override
    public double getViewDistance() {
        return 128;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        Direction dir = this.getDirection();
        return new AxisAlignedBB(0.25,0, 0.25, 0.75, 1, 0.75).expandTowards(
                dir.getStepX()*1.35f,0,dir.getStepZ()*1.35f).move(this.worldPosition);
    }

    public Direction getDirection() {
        return this.getBlockState().getValue(FlagBlock.FACING);
    }

    @Override
    public ITextComponent getName() {
        return this.name != null ? this.name : new TranslationTextComponent("block.supplementaries.flag_"+this.getBaseColor(this::getBlockState).getName());
    }

    @Nullable
    public ITextComponent getCustomName() {
        return this.name;
    }
}