package dev.bh.sp.logger.logger.intfs;

public interface Mask <T> {

  public boolean isAccessibleBy (T t);

  public final Mask<Object> allowAll = new Mask<Object>() {
    @Override
    public boolean isAccessibleBy(Object t) {
      return true;
    }
  }; 

  public static <T> T allowAll () {
    return (T) allowAll;
  }
}
