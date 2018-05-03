package com.codetaylor.mc.hookt.modules.hookt.rule.data;

import java.util.Random;

public class RandomLuckInt {

  public int fixed;
  public int min;
  public int max;
  public int luckModifier;

  public RandomLuckInt() {
    //
  }

  public RandomLuckInt(int fixed) {

    this.fixed = fixed;
  }

  public int get(Random random, int luckLevel) {

    if (this.fixed > 0) {
      return this.fixed + Math.max(0, luckLevel * this.luckModifier);
    }

    // Add one to the range to make the upper bound inclusive.
    int range = Math.abs(this.max - this.min) + 1;
    return random.nextInt(range) + Math.max(0, this.min + luckLevel * this.luckModifier);
  }

  @Override
  public String toString() {

    return "RandomLuckInt{" +
        "fixed=" + fixed +
        ", min=" + min +
        ", max=" + max +
        ", luckModifier=" + luckModifier +
        '}';
  }
}
