package com.topisani.armourstands.event;

import com.topisani.armourstands.util.RotationPoint;
import com.topisani.armourstands.util.WrenchUtil;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static net.minecraft.entity.item.EntityArmorStand.STATUS;

/**
 * @author topisani
 */
public class EventManager {

    public static final Vec3d RIGHT_ARM = new Vec3d(-5.0, 2.0, 0.0);
    public static final Vec3d LEFT_ARM = new Vec3d(5.0, 2.0, 0.0);

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onPlayerClick(PlayerInteractEvent.EntityInteractSpecific event) {
        try {
            if (event.getTarget() instanceof EntityArmorStand) {
                EntityArmorStand armorStand = (EntityArmorStand) event.getTarget();
                ItemStack item = event.getItemStack();
                EntityPlayer player = event.getEntityPlayer();
                Vec3d pos = event.getLocalPos();
                if (item == null) return;
                // Add Arms
                if (!armorStand.getShowArms()) {
                    if (!player.isSneaking()) {
                        if (item.getItem() == Items.STICK && event.getItemStack().stackSize >= 2) {
                            armorStand.getDataManager().set(STATUS, this.setBit(armorStand.getDataManager().get(STATUS), 4, true));
                            event.getItemStack().stackSize -= 2;
                            event.setCanceled(true);
                        }
                    }
                }
                // Rotate parts
                if (armorStand.getShowArms()) {
                    if (item.getItem() == WrenchUtil.WRENCH_ITEM) {
                        player.addChatMessage(new TextComponentString("Clicked at: " + pos.toString()));
                        switch (RotationPoint.closest(pos)) {
                            case HEAD:
                                armorStand.setHeadRotation(WrenchUtil.rotate(armorStand.getHeadRotation(), item, player, player.isSneaking()));
                                break;
                            case LEFT_ARM:
                                armorStand.setLeftArmRotation(WrenchUtil.rotate(armorStand.getLeftArmRotation(), item, player, player.isSneaking()));
                                break;
                            case RIGHT_ARM:
                                armorStand.setRightArmRotation(WrenchUtil.rotate(armorStand.getRightArmRotation(), item, player, player.isSneaking()));
                                break;
                            case LEFT_LEG:
                                armorStand.setLeftLegRotation(WrenchUtil.rotate(armorStand.getLeftLegRotation(), item, player, player.isSneaking()));
                                break;
                            case RIGHT_LEG:
                                armorStand.setRightLegRotation(WrenchUtil.rotate(armorStand.getRightLegRotation(), item, player, player.isSneaking()));
                                break;

                        }
                        event.setCanceled(true);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte setBit(byte p_184797_1_, int p_184797_2_, boolean p_184797_3_) {
        if (p_184797_3_) {
            p_184797_1_ = (byte) (p_184797_1_ | p_184797_2_);
        } else {
            p_184797_1_ = (byte) (p_184797_1_ & ~p_184797_2_);
        }

        return p_184797_1_;
    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickItem event) {
        try {
            if (event.getItemStack().getItem() == WrenchUtil.WRENCH_ITEM && event.getEntityPlayer().isSneaking()) {
                WrenchUtil.setMode(event.getItemStack(), WrenchUtil.WrenchMode.fromByte((byte) (WrenchUtil.getMode(event.getItemStack()).byteVal + 1)));
                event.getEntityPlayer().addChatMessage(new TextComponentString("Mode: " + WrenchUtil.getMode(event.getItemStack()).name()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Does not get called for Armourstands due to a forge/vanilla issue
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onLivingDropsEvent(LivingDropsEvent event) {
        try {
            if (event.getEntity() instanceof EntityArmorStand) {
                EntityArmorStand armorStand = (EntityArmorStand) event.getEntity();
                if (armorStand.getShowArms()) {
                    event.getEntityLiving().dropItem(Items.STICK, 2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
