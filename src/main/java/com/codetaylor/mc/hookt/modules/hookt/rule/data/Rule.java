package com.codetaylor.mc.hookt.modules.hookt.rule.data;

public class Rule {

  public boolean debug = false;
  public RuleMatch match = new RuleMatch();
  public EnumReplaceStrategy replaceStrategy = EnumReplaceStrategy.REPLACE_ALL;
  public EnumDropStrategy dropStrategy = EnumDropStrategy.REPEAT;
  public RandomLuckInt dropCount = new RandomLuckInt(1);
  public RuleDrop[] drops = new RuleDrop[0];

}
