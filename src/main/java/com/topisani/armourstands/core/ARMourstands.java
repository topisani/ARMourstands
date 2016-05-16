package com.topisani.armourstands.core;

import com.topisani.armourstands.event.EventManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;


@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, version = ModInfo.MOD_VERSION, dependencies = "required-after:Forge@[" + ModInfo.FORGE_DEP + ",)", acceptableRemoteVersions = "*")
public class ARMourstands {

    @Instance(ModInfo.MOD_ID)
    public static ARMourstands instance;
    public static String configPath;
    public static EventManager eventManager;

    @SidedProxy(serverSide = ModInfo.PROXY_COMMON, clientSide = ModInfo.PROXY_CLIENT)
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        instance = this;

        configPath = event.getModConfigurationDirectory() + "";
        eventManager = new EventManager();

        MinecraftForge.EVENT_BUS.register(eventManager);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        if (event.getSide() == Side.CLIENT) {
        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }
}
