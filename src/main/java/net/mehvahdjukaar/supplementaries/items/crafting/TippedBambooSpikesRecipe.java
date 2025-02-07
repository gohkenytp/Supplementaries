package net.mehvahdjukaar.supplementaries.items.crafting;

import net.mehvahdjukaar.supplementaries.items.BambooSpikesTippedItem;
import net.mehvahdjukaar.supplementaries.setup.ModRegistry;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class TippedBambooSpikesRecipe extends SpecialRecipe {
    public TippedBambooSpikesRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    private boolean isEmptySpike(ItemStack stack){
        if (stack.getItem() == ModRegistry.BAMBOO_SPIKES_TIPPED_ITEM.get()) {
            CompoundNBT compoundnbt = stack.getTag();
            return compoundnbt != null && compoundnbt.getInt("Damage")!=0;
        }
        else return stack.getItem() == ModRegistry.BAMBOO_SPIKES_ITEM.get();
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {

        ItemStack itemstack = null;
        ItemStack itemstack1 = null;

        for(int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack stack = inv.getItem(i);
            if (isEmptySpike(stack)) {
                if (itemstack != null) {
                    return false;
                }
                itemstack = stack;
            }
            if(stack.getItem() == Items.LINGERING_POTION) {

                if (itemstack1 != null) {
                    return false;
                }
                itemstack1 = stack;

            }
        }
        return itemstack != null && itemstack1 != null;
    }



    @Override
    public ItemStack assemble(CraftingInventory inv) {
        Potion potion = Potions.EMPTY;
        for(int i = 0; i < inv.getContainerSize(); ++i) {
            Potion p = PotionUtils.getPotion(inv.getItem(i));
            if(p!=Potions.EMPTY){
                potion=p;
                break;
            }
        }
        return BambooSpikesTippedItem.makeSpikeItem(potion);

    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv) {
        return NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRegistry.BAMBOO_SPIKES_TIPPED_RECIPE.get();
    }


}
