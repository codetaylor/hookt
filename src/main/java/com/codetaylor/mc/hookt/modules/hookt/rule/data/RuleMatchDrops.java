package com.codetaylor.mc.hookt.modules.hookt.rule.data;

import com.codetaylor.mc.hookt.modules.hookt.rule.match.ItemMatchEntry;

import java.util.ArrayList;
import java.util.List;

public class RuleMatchDrops {

  public transient List<ItemMatchEntry> _drops = new ArrayList<>();

  public EnumListType type = EnumListType.WHITELIST;
  public String[] drops = new String[0];

}
