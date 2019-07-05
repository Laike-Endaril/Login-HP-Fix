package com.fantasticsource.loginhpfix;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.Set;

@Mod(modid = LoginHPFix.MODID, name = LoginHPFix.NAME, version = LoginHPFix.VERSION, dependencies = "required-after:fantasticlib@[1.12.2.020,)", acceptableRemoteVersions = "*")
public class LoginHPFix
{
    public static final String MODID = "loginhpfix";
    public static final String NAME = "Login HP Fix";
    public static final String VERSION = "1.12.2.000";

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(LoginHPFix.class);
    }

    @SubscribeEvent
    public static void saveConfig(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(MODID)) ConfigManager.sync(MODID, Config.Type.INSTANCE);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void playerLogon(PlayerEvent.PlayerLoggedInEvent event)
    {
        //TODO need to do this a bit later, in a more flexible way...maybe on max hp change within first x ticks if there's an event for that, otherwise just check after x ticks
        EntityPlayer player = event.player;
        Set<String> strings = player.getTags();
        for (String s : strings.toArray(new String[0]))
        {
            if (s.contains("loginhp"))
            {
                player.setHealth(player.getMaxHealth() * Float.parseFloat(s.replace("loginhp", "")));
                strings.remove(s);
                break;
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void playerLogoff(PlayerEvent.PlayerLoggedOutEvent event)
    {
        EntityPlayer player = event.player;
        Set<String> strings = player.getTags();
        for (String s : strings.toArray(new String[0]))
        {
            if (s.contains("loginhp"))
            {
                strings.remove(s);
                break;
            }
        }
        strings.add("loginhp" + String.format("%.2f", player.getHealth() / player.getMaxHealth()));
        System.out.println("loginhp" + String.format("%.2f", player.getHealth() / player.getMaxHealth()));
    }
}
