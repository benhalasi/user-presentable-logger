package dev.bh.sp.logger.logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import dev.bh.sp.logger.logger.intfs.Level;
import dev.bh.sp.logger.logger.intfs.Mask;

public class Preset <A, M extends Mask<A>, L extends Level<L>> {
  
  private final Logger<A, L> logger;
  private final M mask;
  private final L level;  

  private final String title;
  private final String message;
  private final List<Data<?, A>> variables;

  private Preset(Logger<A, L> logger, M mask, L level, String title, String message, List<Data<?, A>> variables) {
    this.logger = logger;
    this.mask = mask;
    this.level = level;
    this.title = title;
    this.message = message;
    this.variables = variables;
  }

  public static final class Builder <A, M extends Mask<A>, L extends Level<L>> {
    private static final Pattern VARIABLE = Pattern.compile(Pattern.quote("{}"));
    private final Logger<A, L> logger;
    private final M mask;
    private final L level;  

    private final String title;
    private final StringBuilder message = new StringBuilder();
    private final List<Data<?, A>> variables = new ArrayList<>();

    public Builder(Logger<A, L> logger, M mask, L level, String title) {
      this.logger = logger;
      this.mask = mask;
      this.level = level;
      this.title = title;
    }

    public Builder<A, M, L> m (String message) {
      this.message.append(message);
      VARIABLE.matcher(message).results().forEach((matchResult) -> {
        variables.add(new Data<>());
      });
      return this;
    }

    public Builder<A, M, L> v () {
      this.message.append("{}");
      variables.add(new Data<>());
      return this;
    }

    public Builder<A, M, L> v (Data<?, A> data) {
      this.message.append("{}");
      variables.add(data);
      return this;
    }

    public Preset<A, M, L> build () {
      return new Preset<A, M, L>(logger, mask, level, title, message.toString(), variables);
    }
  }

  public void log (Object... datas) {
    for (int i = 0; i < variables.size() && i < datas.length; i++) {
      datas[i] = (Object) variables.get(i).withObject(datas[i]);
    }
    logger.log(mask, level, message, datas);
  }
}
