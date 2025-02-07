package net.mehvahdjukaar.supplementaries.client.renderers.color;

import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.PotionUtils;

public class CrossbowColor implements IItemColor {


    @Override
    public int getColor(ItemStack stack, int tint) {
        CompoundNBT compoundnbt = stack.getTag();
        if(tint==1 && ClientConfigs.cached.COLORED_ARROWS) {
            if (compoundnbt != null && compoundnbt.contains("ChargedProjectiles", 9)) {
                ListNBT listnbt = compoundnbt.getList("ChargedProjectiles", 10);
                if (listnbt != null && listnbt.size() > 0) {
                    CompoundNBT compoundnbt1 = listnbt.getCompound(0);
                    ItemStack arrow = ItemStack.of(compoundnbt1);
                    Item i = arrow.getItem();
                    if(i == Items.TIPPED_ARROW) return PotionUtils.getColor(arrow);
                    else if(i == Items.SPECTRAL_ARROW) return 0xFFAA00;
                }
            }
        }
        return -1;

    }

}

