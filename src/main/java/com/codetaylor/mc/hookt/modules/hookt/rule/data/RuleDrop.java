package com.codetaylor.mc.hookt.modules.hookt.rule.data;

public class RuleDrop {

  public RuleDropSelector selector = new RuleDropSelector();
  public RuleDropItem item = new RuleDropItem();
  public RandomLuckInt xp = new RandomLuckInt();
  public EnumXPReplaceStrategy xpReplaceStrategy = EnumXPReplaceStrategy.ADD;

}
