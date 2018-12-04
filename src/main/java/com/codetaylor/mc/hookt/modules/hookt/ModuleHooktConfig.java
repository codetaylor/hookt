package com.codetaylor.mc.hookt.modules.hookt;

import com.codetaylor.mc.hookt.ModHookt;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = ModHookt.MOD_ID)
@Config(modid = ModuleHookt.MOD_ID, name = ModuleHookt.MOD_ID + ".module.Hookt")
public class ModuleHooktConfig {

  @Config.Comment({
      "Set to true to enable profiling output to the log file."
  })
  public static boolean ENABLE_PROFILE_LOG_OUTPUT = false;

  @Config.Comment({
      "If true, fished-up items will go directly into the player's inventory instead of spawning in the world."
  })
  public static boolean ENABLE_DIRECT_TO_INVENTORY = false;

  @SubscribeEvent
  public static void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {

    if (event.getModID().equals(ModHookt.MOD_ID)) {
      ConfigManager.sync(ModHookt.MOD_ID, Config.Type.INSTANCE);
    }
  }
}
