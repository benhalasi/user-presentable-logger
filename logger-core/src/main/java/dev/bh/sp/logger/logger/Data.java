package dev.bh.sp.logger.logger;

import dev.bh.sp.logger.logger.intfs.Mask;

public class Data <T, A> {

  private Mask<A> mask = Mask.allowAll();
  private T data;

  public static final <T, A> Data<T, A> of (T data, Mask<A> mask) {
    Data<T, A> d = new Data<>();
    d.setData(data);
    d.setMask(mask);
    return d;
  }

  public static final <T, A> Data<T, A> of (Mask<A> mask) {
    Data<T, A> d = new Data<>();
    d.setData(null);
    d.setMask(mask);
    return d;
  }

  public final Data<T, A> with (T data) {
    Data<T, A> d = new Data<>();
    d.setMask(this.mask);
    d.setData(data);
    return d;
  }

  public Object withObject(Object object) {
    return with((T) object);
  }

  public Mask<A> getMask() {
    return mask;
  }
  public void setMask(Mask<A> mask) {
    this.mask = mask;
  }
  public T getData() {
    return data;
  }
  public void setData(T data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return getData().toString();
  }
}
