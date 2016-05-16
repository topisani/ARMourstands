package com.topisani.armourstands.util;

import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.Rotations;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author topisani
 */
public class WrenchUtil {

    public final static Item WRENCH_ITEM = Items.WOODEN_SHOVEL;
    private final static String modeKey = "mode";
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
        float addend = (back) ? -10f : 10f;
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
        switch (RotationPoint.closest(clickPos)) {
            case HEAD:
                armorStand.setHeadRotation(WrenchUtil.calcRotation(armorStand.getHeadRotation(), player, back));
                break;
            case LEFT_ARM:
                armorStand.setLeftArmRotation(WrenchUtil.calcRotation(armorStand.getLeftArmRotation(), player, back));
                break;
            case RIGHT_ARM:
                armorStand.setRightArmRotation(WrenchUtil.calcRotation(armorStand.getRightArmRotation(), player, back));
                break;
            case LEFT_LEG:
                armorStand.setLeftLegRotation(WrenchUtil.calcRotation(armorStand.getLeftLegRotation(), player, back));
                break;
            case RIGHT_LEG:
                armorStand.setRightLegRotation(WrenchUtil.calcRotation(armorStand.getRightLegRotation(), player, back));
                break;

        }
    }
}
