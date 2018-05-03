package com.codetaylor.mc.hookt.modules.hookt.rule.parse;

import com.codetaylor.mc.athenaeum.parser.recipe.item.MalformedRecipeItemException;
import com.codetaylor.mc.athenaeum.parser.recipe.item.ParseResult;
import com.codetaylor.mc.athenaeum.parser.recipe.item.RecipeItemParser;
import com.codetaylor.mc.hookt.modules.hookt.rule.data.Rule;
import com.codetaylor.mc.hookt.modules.hookt.rule.data.RuleList;
import com.codetaylor.mc.hookt.modules.hookt.rule.log.DebugFileWrapper;
import com.codetaylor.mc.hookt.modules.hookt.rule.log.ILogger;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ParserRuleMatchAnglerHeldItemMainHand
    implements IRuleListParser {

  @Override
  public void parse(
      RecipeItemParser parser, RuleList ruleList, Rule rule, ILogger logger, DebugFileWrapper debugFileWrapper
  ) {

    if (rule.match == null) {

      if (rule.debug) {
        debugFileWrapper.debug("[PARSE] Match object not defined, skipped parsing heldItemMainHand match");
      }
      return;
    }

    for (String string : rule.match.angler.heldItemMainHand.items) {

      if (string == null) {
        logger.error("[PARSE] Null item in list");
        continue;
      }

      if ("empty".equals(string.toLowerCase())) {
        rule.match.angler.heldItemMainHand._items.add(ItemStack.EMPTY);
        debugFileWrapper.debug("[PARSE] Parsed empty item");
        continue;
      }

      ParseResult parse;

      try {
        parse = parser.parse(string);

      } catch (MalformedRecipeItemException e) {
        logger.error("[PARSE] Unable to parse item <" + string + "> in file: " + ruleList._filename, e);
        continue;
      }

      if (rule.debug) {
        debugFileWrapper.debug("[PARSE] Parsed item match: " + parse);
      }

      Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(parse.getDomain(), parse.getPath()));

      if (item == null) {
        logger.error("[PARSE] Unable to find registered item: " + parse.toString());
        continue;
      }

      if (rule.debug) {
        debugFileWrapper.debug("[PARSE] Found registered item: " + item);
      }

      ItemStack itemStack = new ItemStack(item, 1, parse.getMeta());
      rule.match.angler.heldItemMainHand._items.add(itemStack);

      if (rule.debug) {
        debugFileWrapper.debug("[PARSE] Added itemStack to match: " + itemStack);
      }
    }
  }
}
