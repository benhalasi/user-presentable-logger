package dev.bh.sp.logger.logger;

import java.util.ArrayList;
import java.util.List;

import dev.bh.sp.logger.logger.intfs.Level;
import dev.bh.sp.logger.logger.intfs.Mask;

public class Line<A, L extends Level<L>> {

  private Mask<A> mask = Mask.allowAll();
  private Level<L> level;  

  private String title;
  private String message;
  private final List<List<Data<?, A>>> datas = new ArrayList<>();

  public Mask<A> getMask() {
    return mask;
  }
  public void setMask(Mask<A> mask) {
    this.mask = mask;
  }
  public Level<L> getLevel() {
    return level;
  }
  public void setLevel(Level<L> level) {
    this.level = level;
  }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public String getMessage() {
    return message;
  }
  public void setMessage(String message) {
    this.message = message;
  }
  public List<List<Data<?, A>>> getDatas() {
    return datas;
  }
}
