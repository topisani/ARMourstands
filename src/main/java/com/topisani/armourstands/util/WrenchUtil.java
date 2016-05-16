package com.topisani.armourstands.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Rotations;
import net.minecraft.util.text.TextComponentString;

/**
 * @author topisani
 */
public class WrenchUtil {

    public final static Item WRENCH_ITEM = Items.WOODEN_SHOVEL;
    private final static String modeKey = "mode";

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

    public static WrenchMode getMode(ItemStack wrench) {
        try {
            return WrenchMode.fromByte(wrench
                .getTagCompound()
                .getByte(modeKey));
        } catch(NullPointerException e) {
            return WrenchMode.X;
        }
    }

    public static void setMode(ItemStack wrench, WrenchMode mode) {
        if (!wrench.hasTagCompound()) wrench.setTagCompound(new NBTTagCompound());
        wrench.getTagCompound().setByte(modeKey, mode.byteVal);
    }

    public static Rotations rotate(Rotations in, ItemStack wrench, EntityPlayer player, boolean back) {
        if (wrench.getItem() != WRENCH_ITEM) return in;
        float addend = (back) ? -10f : 10f;
        switch (getMode(wrench)) {
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
        player.addChatMessage(new TextComponentString(in.getX() + "," + in.getY() + "," + in.getZ()));
        return in;
    }
}
