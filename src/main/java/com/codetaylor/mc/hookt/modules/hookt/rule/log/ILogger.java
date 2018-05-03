package com.codetaylor.mc.hookt.modules.hookt.rule.log;

public interface ILogger {

  void warn(String message);

  void error(String message);

  void error(String message, Throwable error);

}
