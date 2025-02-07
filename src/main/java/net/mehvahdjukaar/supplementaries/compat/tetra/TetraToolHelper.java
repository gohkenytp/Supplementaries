package net.mehvahdjukaar.supplementaries.compat.tetra;

import net.minecraft.item.Item;
import se.mickelus.tetra.items.modular.impl.ModularBladedItem;
import se.mickelus.tetra.items.modular.impl.ModularDoubleHeadedItem;
import se.mickelus.tetra.items.modular.impl.ModularSingleHeadedItem;

import javax.annotation.Nullable;

public class TetraToolHelper {
    public static boolean isTetraSword(Item i) {
        return i instanceof ModularBladedItem;
    }

    public static boolean isTetraTool(Item i) {
        return (i instanceof ModularDoubleHeadedItem || i instanceof ModularSingleHeadedItem);
    }

}
