package com.codetaylor.mc.hookt.modules.hookt.rule;

import com.codetaylor.mc.hookt.modules.hookt.ModuleHooktConfig;
import com.codetaylor.mc.hookt.modules.hookt.ModuleHookt;
import com.codetaylor.mc.hookt.modules.hookt.rule.data.Rule;
import com.codetaylor.mc.hookt.modules.hookt.rule.data.RuleList;
import com.codetaylor.mc.hookt.modules.hookt.rule.log.DebugFileWrapper;
import com.codetaylor.mc.hookt.modules.hookt.rule.match.RuleMatcher;
import com.codetaylor.mc.hookt.modules.hookt.rule.match.RuleMatcherFactory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class RuleLocator {

  private final RuleMatcherFactory ruleMatcherFactory;

  public RuleLocator(
      RuleMatcherFactory ruleMatcherFactory
  ) {

    this.ruleMatcherFactory = ruleMatcherFactory;
  }

  public Rule locate(
      World world,
      EntityPlayer angler,
      List<ItemStack> drops
  ) {

    return this.matchRule(world, angler, drops, ModuleHookt.RULE_LISTS);
  }

  private Rule matchRule(
      World world,
      EntityPlayer angler,
      List<ItemStack> drops,
      List<RuleList> ruleLists
  ) {

    DebugFileWrapper debugFileWrapper = null;
    RuleMatcher ruleMatcher = this.ruleMatcherFactory.create(world, angler, angler.getPosition(), drops);

    long start = System.currentTimeMillis();
    int checkedRuleCount = 0;
    Rule matchedRule = null;

    for (RuleList ruleList : ruleLists) {

      for (Rule rule : ruleList.rules) {
        boolean debug = rule.debug;
        checkedRuleCount += 1;

        if (debug) {

          if (debugFileWrapper == null) {
            debugFileWrapper = new DebugFileWrapper(ModuleHookt.LOG_FILE_WRITER_PROVIDER.createLogFileWriter());
          }
          this.printDebugEventInfoToFile(world, angler, angler.getPosition(), drops, debugFileWrapper);
        }

        if (ruleMatcher.matches(rule.match, debugFileWrapper, debug)) {
          matchedRule = rule;
          break;
        }
      }
    }

    if (ModuleHooktConfig.ENABLE_PROFILE_LOG_OUTPUT) {

      if (debugFileWrapper == null) {
        debugFileWrapper = new DebugFileWrapper(ModuleHookt.LOG_FILE_WRITER_PROVIDER.createLogFileWriter());
      }
      debugFileWrapper.info(String.format(
          "Searched %d rules in %d ms",
          checkedRuleCount,
          (System.currentTimeMillis() - start)
      ));
    }

    if (debugFileWrapper != null) {
      debugFileWrapper.close();
    }

    return matchedRule;
  }

  private void printDebugEventInfoToFile(
      World world,
      EntityPlayer angler,
      BlockPos pos,
      List<ItemStack> drops,
      DebugFileWrapper debugFileWrapper
  ) {

    debugFileWrapper.debug("--------------------------------------------------------------------------------------");
    debugFileWrapper.debug("[EVENT] Angler: " + angler);
    debugFileWrapper.debug("[EVENT] Drops: " + drops);
    debugFileWrapper.debug("[EVENT] Position: " + pos);

    if (world != null) {

      if (world.provider != null) {
        debugFileWrapper.debug("[EVENT] Dimension: " + world.provider.getDimension());
      }

      debugFileWrapper.debug("[EVENT] Biome: " + world.getBiome(pos).getRegistryName());
    }
  }
}
