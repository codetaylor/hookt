package com.codetaylor.mc.hookt.modules.hookt;

import net.minecraftforge.common.config.Config;

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

}
