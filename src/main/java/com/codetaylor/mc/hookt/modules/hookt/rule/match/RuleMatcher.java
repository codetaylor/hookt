package com.codetaylor.mc.hookt.modules.hookt.rule.match;

import com.codetaylor.mc.hookt.modules.hookt.rule.data.RuleMatch;
import com.codetaylor.mc.hookt.modules.hookt.rule.log.DebugFileWrapper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.List;

public class RuleMatcher {

  private final DropMatcher dropMatcher;
  private final AnglerMatcher anglerMatcher;
  private final BiomeMatcher biomeMatcher;
  private final DimensionMatcher dimensionMatcher;

  private final EntityPlayer angler;
  private final Biome biome;
  private final int dimension;
  private final int posY;
  private final List<ItemStack> drops;

  public RuleMatcher(
      World world,
      EntityPlayer angler,
      BlockPos pos,
      List<ItemStack> drops,
      DropMatcher dropMatcher,
      AnglerMatcher anglerMatcher,
      BiomeMatcher biomeMatcher,
      DimensionMatcher dimensionMatcher
  ) {

    this.dropMatcher = dropMatcher;
    this.anglerMatcher = anglerMatcher;
    this.biomeMatcher = biomeMatcher;
    this.dimensionMatcher = dimensionMatcher;

    this.angler = angler;
    this.biome = world.getBiome(pos);
    this.dimension = world.provider.getDimension();
    this.posY = pos.getY();
    this.drops = drops;
  }

  public boolean matches(
      RuleMatch ruleMatch,
      DebugFileWrapper logFile,
      boolean debug
  ) {

    if (debug) {
      logFile.debug("");
    }

    boolean result = this.matchVerticalRange(ruleMatch, this.posY, logFile, debug)
        && this.dropMatcher.matches(ruleMatch.drops, logFile, debug, this.drops)
        && this.anglerMatcher.matches(ruleMatch.angler, this.angler, logFile, debug)
        && this.biomeMatcher.matches(ruleMatch.biomes, this.biome, logFile, debug)
        && this.dimensionMatcher.matches(ruleMatch.dimensions, this.dimension, logFile, debug);

    if (debug) {

      if (result) {
        logFile.debug("[OK] Rule matched");

      } else {
        logFile.debug("[!!] Rule not matched");
      }
    }

    return result;
  }

  private boolean matchVerticalRange(
      RuleMatch ruleMatch,
      int posY,
      DebugFileWrapper logFile,
      boolean debug
  ) {

    boolean result = (posY <= ruleMatch.verticalRange.max) && (posY >= ruleMatch.verticalRange.min);

    if (debug) {

      if (!result) {
        logFile.debug(String.format(
            "[MATCH] [!!] Vertical position out of bounds: %d <= %d <= %d",
            ruleMatch.verticalRange.min,
            posY,
            ruleMatch.verticalRange.max
        ));

      } else {
        logFile.debug(String.format(
            "[MATCH] [OK] Vertical position within bounds: %d <= %d <= %d",
            ruleMatch.verticalRange.min,
            posY,
            ruleMatch.verticalRange.max
        ));
      }
    }

    return result;
  }

}
