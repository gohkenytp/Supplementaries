package net.mehvahdjukaar.supplementaries.compat.farmersdelight;

import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.setup.ModRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

public class FDCompatRegistry {

    public static final String PLANTER_RICH_NAME = "planter_rich";
    @ObjectHolder(Supplementaries.MOD_ID + ":" + PLANTER_RICH_NAME)
    public static final Block PLANTER_RICH = null;

    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> reg = event.getRegistry();
        reg.register(new PlanterRichBlock(AbstractBlock.Properties.copy(ModRegistry.PLANTER.get()).randomTicks())
                .setRegistryName(PLANTER_RICH_NAME));

    }

    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> reg = event.getRegistry();
        reg.register(new BlockItem(PLANTER_RICH,
                new Item.Properties().tab(ModRegistry.getTab(ItemGroup.TAB_DECORATIONS, PLANTER_RICH_NAME))
        ).setRegistryName(PLANTER_RICH_NAME));

    }


}
