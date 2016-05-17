package com.topisani.armourstands.event;

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
import net.minecraftforge.fml.relauncher.Side;

import static net.minecraft.entity.item.EntityArmorStand.STATUS;

/**
 * @author topisani
 */
public class EventManager {

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onPlayerClick(PlayerInteractEvent.EntityInteractSpecific event) {
        if (event.getSide() == Side.CLIENT) return;
        if (event.getTarget() instanceof EntityArmorStand) {
            EntityArmorStand armorStand = (EntityArmorStand) event.getTarget();
            ItemStack item = event.getItemStack();
            EntityPlayer player = event.getEntityPlayer();
            Vec3d pos = event.getLocalPos();
            if (item == null) return;
            // Add Arms
            if (!armorStand.getShowArms()) {
                if (!player.isSneaking()) {
                    if (item.getItem() == Items.STICK && (item.stackSize >= 2 || player.isCreative())) {
                        armorStand.getDataManager().set(STATUS, this.setBit(armorStand.getDataManager().get(STATUS), 4, true));
                        if (!player.isCreative()) item.stackSize -= 2;
                        event.setCanceled(true);
                    }
                }
            }
            // Rotate parts
            if (WrenchUtil.isWrench(item.getItem())) {
                WrenchUtil.rotate(armorStand, player, pos, player.isSneaking());
                event.setCanceled(true);
            }
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
        if (event.getSide() == Side.CLIENT) return;
        if (WrenchUtil.isWrench(event.getItemStack().getItem()) && event.getEntityPlayer().isSneaking()) {
            WrenchUtil.setMode(event.getEntityPlayer(), WrenchUtil.WrenchMode.fromByte((byte) (WrenchUtil.getMode(event.getEntityPlayer()).byteVal + 1)));
            event.getEntityPlayer().addChatMessage(new TextComponentString("ARMourstands wrench mode: " + WrenchUtil.getMode(event.getEntityPlayer()).name()));
            event.setCanceled(true);
        }
    }

    // Does not get called for Armourstands due to a forge/vanilla issue
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onLivingDropsEvent(LivingDropsEvent event) {
        if (event.getEntity() instanceof EntityArmorStand) {
            EntityArmorStand armorStand = (EntityArmorStand) event.getEntity();
            if (armorStand.getShowArms()) {
                event.getEntityLiving().dropItem(Items.STICK, 2);
            }
        }
    }
}
