package com.codetaylor.mc.hookt.modules.hookt.rule.data;

public class RuleMatch {

  public RuleMatchDrops drops = new RuleMatchDrops();
  public RuleMatchAngler angler = new RuleMatchAngler();
  public RuleMatchBiome biomes = new RuleMatchBiome();
  public RuleMatchDimension dimensions = new RuleMatchDimension();
  public RangeInt verticalRange = new RangeInt(0, 255);

}
