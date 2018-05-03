package com.codetaylor.mc.hookt.modules.hookt.rule.match;

import com.codetaylor.mc.hookt.modules.hookt.rule.data.EnumAnglerType;
import com.codetaylor.mc.hookt.modules.hookt.rule.data.RuleMatchAngler;
import com.codetaylor.mc.hookt.modules.hookt.rule.log.DebugFileWrapper;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nullable;

public class AnglerMatcher {

  private GameStageMatcher gameStageMatcher;
  private HeldItemMainHandMatcher heldItemMainHandMatcher;
  private PlayerNameMatcher playerNameMatcher;

  public AnglerMatcher(
      GameStageMatcher gameStageMatcher,
      HeldItemMainHandMatcher heldItemMainHandMatcher,
      PlayerNameMatcher playerNameMatcher
  ) {

    this.gameStageMatcher = gameStageMatcher;
    this.heldItemMainHandMatcher = heldItemMainHandMatcher;
    this.playerNameMatcher = playerNameMatcher;
  }

  public boolean matches(
      RuleMatchAngler ruleMatchAngler,
      @Nullable EntityPlayer angler,
      DebugFileWrapper logFile,
      boolean debug
  ) {

    if (debug) {
      logFile.debug("[MATCH] [--] Angler type: " + ruleMatchAngler.type);
    }

    if (ruleMatchAngler.type == EnumAnglerType.ANY) {

      if (angler != null) {

        if (debug) {
          logFile.debug("[MATCH] [--] Angler detected, checking angler: " + angler);
        }

        boolean result = this.heldItemMainHandMatcher.matches(
            ruleMatchAngler.heldItemMainHand,
            angler.getHeldItemMainhand(),
            logFile,
            debug
        )
            && this.playerNameMatcher.matches(ruleMatchAngler.playerName, angler.getName(), logFile, debug)
            && this.gameStageMatcher.matches(ruleMatchAngler.gamestages, angler, logFile, debug);

        if (debug) {

          if (result) {
            logFile.debug("[MATCH] [OK] Angler matching passed");

          } else {
            logFile.debug("[MATCH] [!!] Angler matching failed");
          }
        }

        return result;

      } else {

        if (debug) {
          logFile.debug("[MATCH] [OK] No angler detected");
        }
        return true;
      }

    } else if (ruleMatchAngler.type == EnumAnglerType.NON_PLAYER) {

      boolean result = (angler == null);

      if (debug) {

        if (result) {
          logFile.debug("[MATCH] [OK] Angler is null");

        } else {
          logFile.debug("[MATCH] [!!] Angler is not null: " + angler);
        }
      }

      return result;

    } else if (ruleMatchAngler.type == EnumAnglerType.PLAYER) {

      if (angler == null) {

        if (debug) {
          logFile.debug("[MATCH] [!!] Angler is null");
        }
        return false;
      }

      if (debug) {
        logFile.debug("[MATCH] [--] Angler detected, checking angler: " + angler);
      }

      boolean result = this.heldItemMainHandMatcher.matches(
          ruleMatchAngler.heldItemMainHand,
          angler.getHeldItemMainhand(),
          logFile,
          debug
      )
          && this.playerNameMatcher.matches(ruleMatchAngler.playerName, angler.getName(), logFile, debug)
          && this.gameStageMatcher.matches(ruleMatchAngler.gamestages, angler, logFile, debug);

      if (debug) {

        if (result) {
          logFile.debug("[MATCH] [OK] Angler matching passed");

        } else {
          logFile.debug("[MATCH] [!!] Angler matching failed");
        }
      }

      return result;
    }

    return false;
  }

}
