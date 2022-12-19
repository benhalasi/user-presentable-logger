package dev.bh.sp.logger.logger.intfs;

public interface Level<T extends Level<T>> extends Comparable<T> {

  public int getLevel ();

  @Override
  public default int compareTo(T o) {
    return getLevel() - o.getLevel();
  }
}
