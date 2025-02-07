package net.mehvahdjukaar.supplementaries.block.tiles;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import net.mehvahdjukaar.selene.blocks.ItemDisplayTile;
import net.mehvahdjukaar.supplementaries.block.blocks.NoticeBoardBlock;
import net.mehvahdjukaar.supplementaries.block.blocks.StatueBlock;
import net.mehvahdjukaar.supplementaries.common.CommonUtil;
import net.mehvahdjukaar.supplementaries.common.ModTags;
import net.mehvahdjukaar.supplementaries.common.SpecialPlayers;
import net.mehvahdjukaar.supplementaries.setup.ModRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.UUID;

public class StatueBlockTile extends ItemDisplayTile {
    private static PlayerProfileCache profileCache;
    private static MinecraftSessionService sessionService;

    public GameProfile playerProfile = null;

    //clientside
    public StatuePose pose = StatuePose.STANDING;
    public boolean isWaving = false;
    public BlockState candle = null;

    public StatueBlockTile() {
        super(ModRegistry.STATUE_TILE.get());
    }

    public static void initializeSessionData(MinecraftServer server) {
        profileCache = server.getProfileCache();
        sessionService = server.getSessionService();
        //PlayerProfileCache.setOnlineMode(server.isServerInOnlineMode());
    }

    @Override
    public double getViewDistance() {
        return 60;
    }

    @Override
    public void setCustomName(ITextComponent name) {
        super.setCustomName(name);
        this.updateName();
    }

    private void updateName() {

        if (this.hasCustomName()) {

            String name = this.getCustomName().getString().toLowerCase();
            UUID id = SpecialPlayers.STATUES.get(name);
            if (id != null) {
                this.playerProfile = this.updateGameProfile(new GameProfile(id, name));
            }
            //ClientPlayNetHandler connection = Minecraft.getInstance().getConnection();
            //if(connection!=null)
            //this.playerInfo = connection.getPlayerInfo(SpecialPlayers.STATUES.get(this.getCustomName().getString().toLowerCase()));
        } else this.playerProfile = null;

    }

    @Nullable
    public GameProfile updateGameProfile(@Nullable GameProfile input) {
        if (input != null) {
            if (input.isComplete() && input.getProperties().containsKey("textures")) {
                return input;
            } else if (profileCache != null && sessionService != null) {
                GameProfile gameprofile = profileCache.get(input.getName());
                if (gameprofile != null) {
                    Property property = Iterables.getFirst(gameprofile.getProperties().get("textures"), null);
                    if (property == null) {
                        gameprofile = sessionService.fillProfileProperties(gameprofile, true);
                    }
                    return gameprofile;
                }
            }
        }
        return input;
    }

    @Override
    public void updateClientVisualsOnLoad() {
        super.updateClientVisualsOnLoad();
        this.updateName();
        ItemStack stack = this.getDisplayedItem();
        this.pose = StatuePose.getPose(stack);
        this.isWaving = this.getBlockState().getValue(StatueBlock.POWERED);
        if (this.pose == StatuePose.CANDLE) {
            this.candle = ((BlockItem) stack.getItem()).getBlock().defaultBlockState();
        }
    }

    public void updateTileOnInventoryChanged() {
        boolean flag = (StatuePose.getPose(this.getDisplayedItem()) == StatuePose.CANDLE);
        if (flag != this.getBlockState().getValue(StatueBlock.LIT)) {
            this.level.setBlockAndUpdate(this.getBlockPos(), this.getBlockState().setValue(StatueBlock.LIT, flag));
        }
    }

    @Override
    public ITextComponent getDefaultName() {
        return new TranslationTextComponent("block.supplementaries.statuette");
    }

    public Direction getDirection() {
        return this.getBlockState().getValue(NoticeBoardBlock.FACING);
    }

    public enum StatuePose {
        STANDING,
        HOLDING,
        CANDLE,
        SWORD,
        TOOL;

        public static StatuePose getPose(ItemStack stack) {
            if (stack.isEmpty()) return StatuePose.STANDING;
            Item i = stack.getItem();
            if (CommonUtil.isSword(i)) return SWORD;
            if (CommonUtil.isTool(i)) return TOOL;
            return (i.is(ModTags.CANDLES) || i == ModRegistry.CANDLE_HOLDER_ITEM.get()) ? StatuePose.CANDLE : StatuePose.HOLDING;
        }
    }
}