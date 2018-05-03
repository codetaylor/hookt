package com.codetaylor.mc.hookt.modules.hookt;

import net.minecraftforge.common.config.Config;

@Config(modid = ModuleHookt.MOD_ID, name = ModuleHookt.MOD_ID + ".module.Hookt")
public class ModuleHooktConfig {

  @Config.Comment({
      "Set to true to enable profiling output to the log file."
  })
  public static boolean ENABLE_PROFILE_LOG_OUTPUT = false;

}
