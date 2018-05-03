package com.codetaylor.mc.hookt.modules.hookt.rule.match;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class RuleMatcherFactory {

  private DropMatcher dropMatcher;
  private AnglerMatcher anglerMatcher;
  private BiomeMatcher biomeMatcher;
  private DimensionMatcher dimensionMatcher;

  public RuleMatcherFactory(
      DropMatcher dropMatcher, AnglerMatcher anglerMatcher,
      BiomeMatcher biomeMatcher,
      DimensionMatcher dimensionMatcher
  ) {

    this.dropMatcher = dropMatcher;
    this.anglerMatcher = anglerMatcher;
    this.biomeMatcher = biomeMatcher;
    this.dimensionMatcher = dimensionMatcher;
  }

  public RuleMatcher create(
      World world,
      EntityPlayer angler,
      BlockPos pos,
      List<ItemStack> drops
  ) {

    return new RuleMatcher(
        world,
        angler,
        pos,
        drops,
        this.dropMatcher,
        this.anglerMatcher,
        this.biomeMatcher,
        this.dimensionMatcher
    );
  }

}
