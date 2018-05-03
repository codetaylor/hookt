package com.codetaylor.mc.hookt.modules.hookt;

import com.codetaylor.mc.athenaeum.module.ModuleBase;
import com.codetaylor.mc.hookt.ModHookt;
import com.codetaylor.mc.hookt.modules.hookt.command.Command;
import com.codetaylor.mc.hookt.modules.hookt.events.EventHandler;
import com.codetaylor.mc.hookt.modules.hookt.rule.RuleLoader;
import com.codetaylor.mc.hookt.modules.hookt.rule.RuleLocator;
import com.codetaylor.mc.hookt.modules.hookt.rule.data.RuleList;
import com.codetaylor.mc.hookt.modules.hookt.rule.drop.DropModifier;
import com.codetaylor.mc.hookt.modules.hookt.rule.log.DebugFileWrapper;
import com.codetaylor.mc.hookt.modules.hookt.rule.log.LoggerWrapper;
import com.codetaylor.mc.hookt.modules.hookt.rule.match.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModuleHookt
    extends ModuleBase {

  public static final String MOD_ID = ModHookt.MOD_ID;

  public static final List<RuleList> RULE_LISTS = new ArrayList<>();

  public static Logger LOGGER;
  public static boolean MOD_GAMESTAGES;
  public static Path RULE_PATH;
  public static Path LOG_PATH;
  public static LogFileWriterProvider LOG_FILE_WRITER_PROVIDER;

  public ModuleHookt() {

    super(0, MOD_ID);

    MinecraftForge.EVENT_BUS.register(
        new EventHandler(
            new RuleLocator(
                new RuleMatcherFactory(
                    new DropMatcher(),
                    new AnglerMatcher(
                        new GameStageMatcher(),
                        new HeldItemMainHandMatcher(),
                        new PlayerNameMatcher()
                    ),
                    new BiomeMatcher(),
                    new DimensionMatcher()
                )
            ),
            new DropModifier()
        )
    );
  }

  @Override
  public void onPreInitializationEvent(FMLPreInitializationEvent event) {

    super.onPreInitializationEvent(event);

    MOD_GAMESTAGES = Loader.isModLoaded("gamestages");
    LOGGER = LogManager.getLogger(MOD_ID + "." + this.getClass().getSimpleName());

    File configDir = event.getModConfigurationDirectory();
    RULE_PATH = configDir.toPath().resolve(MOD_ID);

    try {
      Files.createDirectories(RULE_PATH);

    } catch (IOException e) {
      LOGGER.error("", e);
    }

    LOG_PATH = configDir.toPath().resolve("../hookt.log");

    if (Files.exists(LOG_PATH) && Files.isRegularFile(LOG_PATH)) {

      try {
        Files.delete(LOG_PATH);

      } catch (IOException e) {
        LOGGER.error("", e);
      }
    }

    LOG_FILE_WRITER_PROVIDER = new LogFileWriterProvider(LOG_PATH, LOGGER);
    FileWriter logFileWriter = LOG_FILE_WRITER_PROVIDER.createLogFileWriter();
    DebugFileWrapper debugFileWrapper = new DebugFileWrapper(logFileWriter);
    RuleLoader.loadRuleLists(
        RULE_PATH,
        RULE_LISTS,
        new LoggerWrapper(LOGGER, logFileWriter),
        debugFileWrapper
    );
    debugFileWrapper.close();
  }

  @Override
  public void onLoadCompleteEvent(FMLLoadCompleteEvent event) {

    super.onLoadCompleteEvent(event);

    FileWriter logFileWriter = LOG_FILE_WRITER_PROVIDER.createLogFileWriter();
    DebugFileWrapper debugFileWrapper = new DebugFileWrapper(logFileWriter);

    RuleLoader.parseRuleLists(RULE_LISTS, new LoggerWrapper(LOGGER, logFileWriter), debugFileWrapper);
    Util.closeSilently(logFileWriter);
  }

  @Override
  public void onServerStartingEvent(FMLServerStartingEvent event) {

    super.onServerStartingEvent(event);

    event.registerServerCommand(new Command());
  }
}
