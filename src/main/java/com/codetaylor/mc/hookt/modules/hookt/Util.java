package com.codetaylor.mc.hookt.modules.hookt;

import java.io.Closeable;
import java.io.IOException;
import java.util.Random;

public class Util {

  public static final Random RANDOM = new Random();

  public static void closeSilently(Closeable closeable) {

    if (closeable == null) {
      return;
    }

    try {
      closeable.close();

    } catch (IOException e) {
      //
    }

  }

}
