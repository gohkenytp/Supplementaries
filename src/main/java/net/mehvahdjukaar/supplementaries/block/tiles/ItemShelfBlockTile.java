package net.mehvahdjukaar.supplementaries.block.tiles;

import net.mehvahdjukaar.selene.blocks.ItemDisplayTile;
import net.mehvahdjukaar.supplementaries.block.blocks.ItemShelfBlock;
import net.mehvahdjukaar.supplementaries.setup.ModRegistry;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ItemShelfBlockTile extends ItemDisplayTile {

    public ItemShelfBlockTile() {
        super(ModRegistry.ITEM_SHELF_TILE.get());
    }

    @Override
    public ITextComponent getDefaultName() {
        return new TranslationTextComponent("block.supplementaries.item_shelf");
    }

    public float getYaw() {
        return -this.getDirection().getOpposite().toYRot();
    }

    public Direction getDirection() {
        return this.getBlockState().getValue(ItemShelfBlock.FACING);
    }
}

