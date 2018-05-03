package com.codetaylor.mc.hookt.modules.hookt.command;

import com.codetaylor.mc.hookt.modules.hookt.ModuleHookt;
import com.codetaylor.mc.hookt.modules.hookt.Util;
import com.codetaylor.mc.hookt.modules.hookt.rule.RuleLoader;
import com.codetaylor.mc.hookt.modules.hookt.rule.log.DebugFileWrapper;
import com.codetaylor.mc.hookt.modules.hookt.rule.log.LoggerWrapper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.FileWriter;

public class Command
    extends CommandBase {

  @Override
  public String getName() {

    return "hookt";
  }

  @Override
  public String getUsage(ICommandSender sender) {

    return "hookt <reload> | <hand>";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

    if (args.length > 0) {

      if ("reload".equals(args[0])) {
        sender.sendMessage(new TextComponentString("Starting rule list reload..."));

        FileWriter logFileWriter = ModuleHookt.LOG_FILE_WRITER_PROVIDER.createLogFileWriter();
        CommandLoggerWrapper wrapper = new CommandLoggerWrapper(ModuleHookt.LOGGER, sender, logFileWriter);
        ModuleHookt.RULE_LISTS.clear();
        RuleLoader.loadRuleLists(
            ModuleHookt.RULE_PATH, ModuleHookt.RULE_LISTS, wrapper,
            new DebugFileWrapper(logFileWriter)
        );
        RuleLoader.parseRuleLists(ModuleHookt.RULE_LISTS, wrapper, new DebugFileWrapper(logFileWriter));
        Util.closeSilently(logFileWriter);
        sender.sendMessage(new TextComponentString("[" + ModuleHookt.RULE_LISTS.size() + "] files processed"));

      } else if ("hand".equals(args[0])) {

        if (sender instanceof EntityPlayer) {
          ItemStack itemStack = ((EntityPlayer) sender).getHeldItemMainhand();
          ResourceLocation registryName = itemStack.getItem().getRegistryName();

          if (registryName != null) {
            String string = registryName.toString() + ":" + itemStack.getMetadata();
            NBTTagCompound tagCompound = itemStack.getTagCompound();

            if (tagCompound != null) {
              string += "#" + tagCompound.toString();
            }

            string = string.replaceAll("\"", "\\\\\"");

            sender.sendMessage(new TextComponentString(string));

            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(string), null);
          }
        }
      }
    }
  }

  @Override
  public boolean checkPermission(MinecraftServer server, ICommandSender sender) {

    if (sender instanceof EntityPlayer) {
      return server.getPlayerList().canSendCommands(((EntityPlayer) sender).getGameProfile());
    }

    return false;
  }

  private static class CommandLoggerWrapper
      extends LoggerWrapper {

    private final ICommandSender sender;

    public CommandLoggerWrapper(Logger logger, ICommandSender sender, FileWriter fileWriter) {

      super(logger, fileWriter);
      this.sender = sender;
    }

    @Override
    public void warn(String message) {

      super.warn(message);
      sender.sendMessage(new TextComponentString(message));
    }

    @Override
    public void error(String message) {

      super.error(message);
      sender.sendMessage(new TextComponentString(message));
    }

    @Override
    public void error(String message, Throwable error) {

      super.error(message, error);
      sender.sendMessage(new TextComponentString(message));
      sender.sendMessage(new TextComponentString(error.getMessage()));
    }
  }
}
