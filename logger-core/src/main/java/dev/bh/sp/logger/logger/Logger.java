package dev.bh.sp.logger.logger;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.LinkedBlockingDeque;

import dev.bh.sp.logger.logger.intfs.Level;
import dev.bh.sp.logger.logger.intfs.Mask;

public class Logger <A, L extends Level<L>> {

  private final SortedSet<L> levels = new TreeSet<>();
  private final Map<L, Deque<Line<A, L>>> lines = new HashMap<>();
  private final Map<String, Line<A, L>> groups = new HashMap<>();

  public SortedSet<L> getLevels() {
    return levels;
  }

  public Preset.Builder<A, Mask<A>, L> preset (L level, String title) {
    return preset(Mask.allowAll(), level, title);
  }

  public Preset.Builder<A, Mask<A>, L> preset (Mask<A> mask, L level, String title) {
    return new Preset.Builder<A, Mask<A>, L>(this, mask, level, title);
  }

  public void log (L level, String message, Object... datas){
    log(Mask.allowAll(), level, message, datas);
  }

  public void log (Mask<A> mask, L level, String message, Object... datas){
    log(
      groups.computeIfAbsent(message, k -> {
        Line<A, L> line = new Line<>();
        line.setMask(mask);
        line.setLevel(level);
        line.setMessage(message);
        return line;
      }),
      datas
    );

    for (var l : levels.tailSet(level)) {
      var collapsible = lines.computeIfAbsent(l, k -> new LinkedBlockingDeque<>());
      var line = collapsible.peekLast();
      if ( line == null || !line.getMessage().equals(message)){
        line = new Line<>();
        collapsible.add(line);
        line.setMask(mask);
        line.setLevel(level);
        line.setMessage(message);
      }

      log(line, datas);
    }
  }

  @SuppressWarnings("unchecked")
  private void log(Line<A, L> line, Object... datas) {
    List<Data<?, A>> e = new ArrayList<>(datas.length);
    for (Object arg : datas) {
      if ( arg instanceof Data ){
        e.add((Data<?, A>) arg);
      } else {
        e.add(Data.of(arg, Mask.allowAll()));
      }
    }
    line.getDatas().add(e);
  }

  public List<String> read (L level, A authority) {
    final List<String> read = new ArrayList<>();

    for (Line<A, L> line : lines.get(level)) {
      if ( line.getMask().isAccessibleBy(authority) ){
        read.add(readLine(line, authority));
      }
    }

    return read;
  }

  public List<String> readGroups(L level, A authority) {
    final List<String> read = new ArrayList<>();

    for (Line<A, L> line : groups.values()) {
      if ( line.getLevel().getLevel() - level.getLevel() >= 0 && line.getMask().isAccessibleBy(authority) ){
        read.add(readLine(line, authority));
      }
    }

    return read;
  }

  public String readLine (Line<A, L> line, A authority) {
    StringBuilder sb = new StringBuilder();

    final boolean endsWithVariable = line.getMessage().endsWith("{}");

    for (List<Data<?, A>> datas : line.getDatas()) {
      Deque<Data<?, A>> q = new LinkedBlockingDeque<>(datas);

      String[] split = line.getMessage().split("\\{\\}");

      if (split.length == 0) {
        if ( endsWithVariable && !q.isEmpty() ){
          sb.append(pop(q, authority));
        }
      } else if (split.length == 1) {
        sb.append(split[0]);
        if ( endsWithVariable && !q.isEmpty() ){
          sb.append(pop(q, authority));
        }
      } else {
        for (int i = 0; i < split.length; i++) {
          sb.append(split[i]);
          if ( !q.isEmpty() ){
            sb.append(pop(q, authority));
          }
        }
      }

      for (Data<?, A> dataContainer : q) {
        sb.append(" ").append(data(authority, dataContainer));
      }

      sb.append("\n");
    }

    sb.deleteCharAt(sb.length() -1); // last new line

    return sb.toString();
  }

  private String pop(Deque<Data<?, A>> q, A authority) {
    Data<?, A> data = q.pop();
    return data(authority, data);
  }

  private String data(A authority, Data<?, A> data) {
    return data.getMask().isAccessibleBy(authority) ? data.toString() : "[reducted]";
  }

}
