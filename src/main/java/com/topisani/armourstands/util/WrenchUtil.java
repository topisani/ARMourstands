package com.topisani.armourstands.util;

import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.Rotations;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author topisani
 */
public class WrenchUtil {

    public static Item[] WRENCH_ITEMS = {Item.getItemFromBlock(Blocks.LEVER)};
    public static float INCREMENT = 7.5f;
    private final static HashMap<UUID, Byte> playerModes = new HashMap<>();

    public enum WrenchMode {
        X(0),
        Y(1),
        Z(2);

        public final byte byteVal;
        WrenchMode(int i) {
            byteVal = (byte) i;
        }

        public static WrenchMode fromByte(byte b) {
            b = (byte) (b % 3);
            switch (b) {
                case 0:
                    return X;
                case 1:
                    return Y;
                case 2:
                    return Z;
            }
            return X;
        }
    }

    public static WrenchMode getMode(EntityPlayer player) {
        try {
            return WrenchMode.fromByte(playerModes.get(player.getUniqueID()));
        } catch(NullPointerException e) {
            return WrenchMode.X;
        }
    }

    public static void setMode(EntityPlayer player, WrenchMode mode) {
        playerModes.put(player.getUniqueID(), mode.byteVal);
    }

    private static Rotations calcRotation(Rotations in, EntityPlayer player, boolean back) {
        float addend = (back) ? -INCREMENT : INCREMENT;
        switch (getMode(player)) {
            case X:
                in = new Rotations(in.getX() + addend, in.getY(), in.getZ());
                break;
            case Y:
                in = new Rotations(in.getX(), in.getY() + addend, in.getZ());
                break;
            case Z:
                in = new Rotations(in.getX(), in.getY(), in.getZ() + addend);
                break;
        }
        return in;
    }

    public static void rotate(EntityArmorStand armorStand, EntityPlayer player, Vec3d clickPos, boolean back) {
        RotationPoint closest = RotationPoint.closest(clickPos, armorStand.rotationYaw);
        switch (closest) {
            case HEAD:
                armorStand.setHeadRotation(WrenchUtil.calcRotation(armorStand.getHeadRotation(), player, back));
                break;
            case LEFT_ARM:
                armorStand.setLeftArmRotation(WrenchUtil.calcRotation(armorStand.getDataManager().get(EntityArmorStand.LEFT_ARM_ROTATION), player, back));
                break;
            case RIGHT_ARM:
                armorStand.setRightArmRotation(WrenchUtil.calcRotation(armorStand.getDataManager().get(EntityArmorStand.RIGHT_ARM_ROTATION), player, back));
                break;
            case LEFT_LEG:
                armorStand.setLeftLegRotation(WrenchUtil.calcRotation(armorStand.getDataManager().get(EntityArmorStand.LEFT_LEG_ROTATION), player, back));
                break;
            case RIGHT_LEG:
                armorStand.setRightLegRotation(WrenchUtil.calcRotation(armorStand.getDataManager().get(EntityArmorStand.RIGHT_LEG_ROTATION), player, back));
                break;
            case BASE:
                float addend = (back) ? -INCREMENT : INCREMENT;
                armorStand.rotationYaw += addend;
        }
    }

    public static boolean isWrench(Item item) {
        for (Item wrench : WRENCH_ITEMS) {
            if(item.equals(wrench)) return true;
        }
        return false;
    }
}
