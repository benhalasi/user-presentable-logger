package dev.bh.sp.logger.logger.logging;

import dev.bh.sp.logger.logger.intfs.Level;

public enum ExampleLevel implements Level<ExampleLevel> {
  
  TRACE(0),
  DEBUG(1),
  INFO(2),
  WARN(3),
  ERROR(4),

  /* */;

  private final int level;

  private ExampleLevel(int level) {
    this.level = level;
  }

  @Override
  public int getLevel() {
    return level;
  }
  
}
