package net.mehvahdjukaar.supplementaries.compat.inspirations;

import knightminer.inspirations.library.recipe.cauldron.CauldronContentTypes;
import knightminer.inspirations.library.recipe.cauldron.contents.CauldronContentType;
import knightminer.inspirations.library.recipe.cauldron.contents.ICauldronContents;
import knightminer.inspirations.recipes.recipe.cauldron.contents.CauldronContents;
import knightminer.inspirations.recipes.tileentity.CauldronTileEntity;
import net.mehvahdjukaar.selene.fluids.SoftFluid;
import net.mehvahdjukaar.selene.fluids.SoftFluidHolder;
import net.mehvahdjukaar.selene.fluids.SoftFluidRegistry;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.function.Supplier;

public class CauldronPlugin {

    public static boolean doStuff(TileEntity cauldronTile, SoftFluidHolder faucetFluidHolder, boolean doTransfer, Supplier<Boolean> transferBelow) {
        if (cauldronTile instanceof CauldronTileEntity) {
            CauldronTileEntity te = (CauldronTileEntity) cauldronTile;

            ICauldronContents contents = te.getContents();

            CauldronContentType<?> contentType = contents.getType();
            SoftFluid equivalent;
            CompoundNBT nbt = new CompoundNBT();
            int levelToBottleEquivalent;
            if (contentType == CauldronContentTypes.FLUID) {
                //ugly. might not work
                Fluid f = (Fluid) contents.get(contentType).get();
                equivalent = SoftFluidRegistry.fromForgeFluid(f);
                levelToBottleEquivalent = 3;
                if(f == Fluids.WATER)levelToBottleEquivalent = 4;
                //fix so won't create infinite soup
                //if(equivalent==SoftFluidRegistry.BEETROOT_SOUP||equivalent==SoftFluidRegistry.RABBIT_STEW||
                        //equivalent==SoftFluidRegistry.MUSHROOM_STEW||f.getRegistryName().getPath().equals("potato_soup")){}
            } else if (contentType == CauldronContentTypes.POTION) {
                Potion potion = (Potion) contents.get(contentType).get();
                equivalent = SoftFluidRegistry.POTION;
                nbt.putString("Potion", potion.getRegistryName().toString());
                nbt.putString("Bottle","REGULAR");
                levelToBottleEquivalent = 4;
            } else if (contentType == CauldronContentTypes.DYE) {
                DyeColor dye = (DyeColor) contents.get(contentType).get();
                equivalent = SoftFluidRegistry.get("inspirations:" + dye.getSerializedName() + "_dye");
                levelToBottleEquivalent = 4;
            } else return false;

            faucetFluidHolder.fill(equivalent, nbt);

            if (doTransfer) {
                int level = te.getLevel();
                if (level >= levelToBottleEquivalent && transferBelow.get()) {
                    te.updateStateAndBlock(contents, level - levelToBottleEquivalent);
                    te.setChanged();
                }
            }
            if (!doTransfer) return !faucetFluidHolder.isEmpty();
            return true;
        }
        return false;
    }

    //TODO: find workaround for soups
    public static boolean tryAddFluid(TileEntity cauldronTile, SoftFluidHolder faucetFluidHolder) {
        if (cauldronTile instanceof CauldronTileEntity) {
            CauldronTileEntity te = (CauldronTileEntity) cauldronTile;

            SoftFluid s = faucetFluidHolder.getFluid();
            CompoundNBT com = faucetFluidHolder.getNbt();
            ResourceLocation name = s.getRegistryName();

            int level = te.getLevel();
            ICauldronContents contents = te.getContents();
            CauldronContentType<?> contentType = contents.getType();

            int levelToBottleEquivalent = 4;
            //dye
            if(name.getNamespace().equals("inspirations") && name.getPath().contains("_dye")){
                DyeColor color = DyeColor.byName(name.getPath().replace("_dye",""),DyeColor.WHITE);
                if(level==0){
                    te.updateStateAndBlock(new CauldronContents<>(CauldronContentTypes.DYE, color),levelToBottleEquivalent);
                    te.setChanged();
                    return true;
                }
                else if(level + levelToBottleEquivalent <= 12 && contentType == CauldronContentTypes.DYE
                        && contents.get(CauldronContentTypes.DYE).get() == color){
                    te.updateStateAndBlock(contents,level + levelToBottleEquivalent);
                    te.setChanged();
                    return true;
                }
            }
            else if(s == SoftFluidRegistry.POTION){
                if(com.getString("Bottle").equals("REGULAR")){
                    Potion potion = PotionUtils.getPotion(com);
                    if(level==0){
                        te.updateStateAndBlock(new CauldronContents<>(CauldronContentTypes.POTION, potion),levelToBottleEquivalent);
                        te.setChanged();
                        return true;
                    }
                    else if(level + levelToBottleEquivalent <= 12 && contentType == CauldronContentTypes.POTION
                            && contents.get(CauldronContentTypes.POTION).get() == potion){
                        te.updateStateAndBlock(contents,level + levelToBottleEquivalent);
                        te.setChanged();
                        return true;
                    }
                }
            }
            else{
                List<Fluid> fluids = s.getEquivalentFluids();
                if(!fluids.isEmpty()){
                    if(s != SoftFluidRegistry.WATER)levelToBottleEquivalent = 3;

                    if(level==0){
                        te.updateStateAndBlock(new CauldronContents<>(CauldronContentTypes.FLUID,
                                fluids.get(0)),levelToBottleEquivalent);
                        te.setChanged();
                        return true;
                    }
                    else if(level + levelToBottleEquivalent <= 12 && contentType == CauldronContentTypes.FLUID
                            && s.isEquivalent(contents.get(CauldronContentTypes.FLUID).get())){
                        te.updateStateAndBlock(contents,level + levelToBottleEquivalent);
                        te.setChanged();
                        return true;
                    }
                }
            }
        }
        return false;
    }
}