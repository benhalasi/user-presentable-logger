package dev.bh.sp.logger.logger.logging;

import dev.bh.sp.logger.logger.intfs.Mask;

public enum DummyRole implements Mask<DummyRole> {

  ADMIN, USER;

  /* */;

  @Override
  public boolean isAccessibleBy(DummyRole role) {
    return role == ADMIN || this == role;
  }

}
