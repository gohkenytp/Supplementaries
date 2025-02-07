package net.mehvahdjukaar.supplementaries.common.capabilities;

import net.mehvahdjukaar.supplementaries.api.ICatchableMob;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import org.jetbrains.annotations.Nullable;

public class CapabilitiesHandler {

    public static void register() {
        CapabilityManager.INSTANCE.register(ICatchableMob.class, new DummyStorage(), DummyCatchableMobCap::new);
    }

    //don't need to store anything
    private static class DummyStorage implements Capability.IStorage<ICatchableMob> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<ICatchableMob> capability, ICatchableMob instance, Direction side) {
            return null;
        }

        @Override
        public void readNBT(Capability<ICatchableMob> capability, ICatchableMob instance, Direction side, INBT nbt) {
        }
    }

    public static class DummyCatchableMobCap implements ICatchableMob{

        @Override
        public boolean canBeCaughtWithJar() {
            return false;
        }

        @Override
        public boolean canBeCaughtWithTintedJar() {
            return false;
        }

        @Override
        public boolean canBeCaughtWithCage() {
            return false;
        }

        @Override
        public Entity getEntity() {
            return null;
        }
    }

}