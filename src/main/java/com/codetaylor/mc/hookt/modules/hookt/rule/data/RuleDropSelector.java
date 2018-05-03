package com.codetaylor.mc.hookt.modules.hookt.rule.data;

import com.codetaylor.mc.hookt.modules.hookt.rule.log.DebugFileWrapper;

public class RuleDropSelector {

  public RuleDropSelectorWeight weight = new RuleDropSelectorWeight();
  public int luckLevelRequired = 0;

  public boolean isValidCandidate(
      int luckLevel,
      DebugFileWrapper logFile,
      boolean debug
  ) {

    if (luckLevel < this.luckLevelRequired) {

      if (debug) {
        logFile.debug(String.format(
            "[DROP] [SELECTOR] [!!] Required luck level not met: (required) %d > %d (candidate)",
            this.luckLevelRequired,
            luckLevel
        ));
      }
      return false;

    } else if (debug) {
      logFile.debug(String.format(
          "[DROP] [SELECTOR] [OK] Required luck level met: (required) %d <= %d (candidate)",
          this.luckLevelRequired,
          luckLevel
      ));
    }

    return true;
  }
}
