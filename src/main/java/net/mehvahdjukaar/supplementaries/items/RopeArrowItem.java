package net.mehvahdjukaar.supplementaries.items;

import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.mehvahdjukaar.supplementaries.configs.ServerConfigs;
import net.mehvahdjukaar.supplementaries.entities.RopeArrowEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class RopeArrowItem extends ArrowItem {
    public RopeArrowItem(Properties builder) {
        super(builder);
    }

    @Override
    public AbstractArrowEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
        CompoundNBT com = stack.getTag();
        int charges = stack.getMaxDamage();
        if (com != null) {
            if (com.contains("Damage")) {
                charges = charges - com.getInt("Damage");
            }
        }
        return new RopeArrowEntity(world, shooter, charges);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return ServerConfigs.cached.ROPE_ARROW_CAPACITY;
    }

    @Override
    public boolean isInfinite(ItemStack stack, ItemStack bow, PlayerEntity player) {
        return false;
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return false;
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return 0x6f4c36;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public int getEnchantmentValue() {
        return 0;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("message.supplementaries.rope_arrow_tooltip", stack.getMaxDamage() - stack.getDamageValue(), stack.getMaxDamage()));
        if (!ClientConfigs.cached.TOOLTIP_HINTS || !Minecraft.getInstance().options.advancedItemTooltips) return;
        if (worldIn == null) return;
        String override = ServerConfigs.cached.ROPE_ARROW_BLOCK.getRegistryName().getNamespace();
        if (!override.equals(Supplementaries.MOD_ID)) {
            tooltip.add(new TranslationTextComponent("message.supplementaries.rope_arrow", override).withStyle(TextFormatting.ITALIC).withStyle(TextFormatting.GRAY));
        }
    }
}
