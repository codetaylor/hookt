package com.codetaylor.mc.hookt.modules.hookt.rule.match;

import com.codetaylor.mc.hookt.modules.hookt.ModuleHookt;
import com.codetaylor.mc.hookt.modules.hookt.rule.data.EnumAnglerGameStageType;
import com.codetaylor.mc.hookt.modules.hookt.rule.data.EnumListType;
import com.codetaylor.mc.hookt.modules.hookt.rule.data.RuleMatchAnglerGameStage;
import com.codetaylor.mc.hookt.modules.hookt.rule.log.DebugFileWrapper;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class GameStageMatcher {

  public boolean matches(
      RuleMatchAnglerGameStage ruleMatchAnglerGameStage,
      @Nonnull EntityPlayer angler,
      DebugFileWrapper logFile,
      boolean debug
  ) {

    if (ruleMatchAnglerGameStage.stages.length == 0) {

      if (debug) {
        logFile.debug("[MATCH] [OK] No game stages defined");
      }
      return true;
    }

    if (!ModuleHookt.MOD_GAMESTAGES) {

      if (debug) {
        logFile.debug("[MATCH] [!!] Game stages are defined, but the gamestages mod is missing");
      }
      return false;
    }

    if (debug) {
      logFile.debug("[MATCH] [--] GameStages type: " + ruleMatchAnglerGameStage.require);
    }

    boolean result = false;

    if (ruleMatchAnglerGameStage.require == EnumAnglerGameStageType.ALL) {
      result = GameStageHelper.hasAllOf(angler, ruleMatchAnglerGameStage.stages);

      if (debug) {

        if (result) {
          logFile.debug("[MATCH] [OK] Player has all required stages: " + Arrays.toString(ruleMatchAnglerGameStage.stages));

        } else {
          logFile.debug("[MATCH] [!!] Player does not have all required stages: " + Arrays.toString(
              ruleMatchAnglerGameStage.stages));
        }
      }

    } else if (ruleMatchAnglerGameStage.require == EnumAnglerGameStageType.ANY) {
      result = GameStageHelper.hasAnyOf(angler, ruleMatchAnglerGameStage.stages);

      if (debug) {

        if (result) {
          logFile.debug("[MATCH] [OK] Player has one or more required stages: " + Arrays.toString(
              ruleMatchAnglerGameStage.stages));

        } else {
          logFile.debug("[MATCH] [!!] Player does not have any of the required stages: " + Arrays.toString(
              ruleMatchAnglerGameStage.stages));
        }
      }
    }

    if (ruleMatchAnglerGameStage.type == EnumListType.BLACKLIST) {
      result = !result;

      if (debug) {

        if (result) {
          logFile.debug("[MATCH] [OK] Gamestage result inverted due to blacklist type");

        } else {
          logFile.debug("[MATCH] [!!] Gamestage result inverted due to blacklist type");
        }
      }
    }

    return result;
  }

}
