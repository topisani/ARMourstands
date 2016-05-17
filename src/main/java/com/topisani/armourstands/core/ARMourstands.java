package com.topisani.armourstands.core;

import com.topisani.armourstands.event.EventManager;
import com.topisani.armourstands.util.WrenchUtil;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

import static com.topisani.armourstands.core.ModInfo.MOD_ID;


@Mod(modid = MOD_ID, name = ModInfo.MOD_NAME, version = ModInfo.MOD_VERSION, dependencies = "required-after:Forge@[" + ModInfo.FORGE_DEP + ",)", acceptableRemoteVersions = "*")
public class ARMourstands {

    @Instance(MOD_ID)
    public static ARMourstands instance;
    public static Configuration config;
    public static EventManager eventManager;

    @SidedProxy(serverSide = ModInfo.PROXY_COMMON, clientSide = ModInfo.PROXY_CLIENT)
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        instance = this;

        config = new Configuration(new File(event.getModConfigurationDirectory(), MOD_ID + ".cfg"));
        eventManager = new EventManager();

        MinecraftForge.EVENT_BUS.register(eventManager);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        config.load();
        WrenchUtil.INCREMENT = config.getFloat("Rotation increment", "main", WrenchUtil.INCREMENT, 0f, 360f, "");
        String[] items = new String[WrenchUtil.WRENCH_ITEMS.length];
        for (int i = 0; i < WrenchUtil.WRENCH_ITEMS.length; i++) {
            Item item = WrenchUtil.WRENCH_ITEMS[i];
            items[i] = item.getRegistryName().toString();
        }
        items = config.getStringList("Wrench Items", "main", items, "List of items that can rotate armourstands. Mod items are supported");
        WrenchUtil.WRENCH_ITEMS = new Item[items.length];
        for (int i = 0; i < items.length; i++) {
            Item item = Item.getByNameOrId(items[i]);
            if (item == null) FMLLog.info("[ARMourstands] Config error: '%s' doesn't seem to be an item", items[i]);
            WrenchUtil.WRENCH_ITEMS[i] = item;
        }
        config.save();
    }
}
