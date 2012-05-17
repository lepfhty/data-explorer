package edu.usc.chla.vpicu.explorer;

import java.util.HashMap;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

public class Histogram extends HashMap<Comparable<?>, Integer> {

  private static final long serialVersionUID = -1439228478252799490L;

  public Frequency getFrequency() {
    Frequency f = new Frequency();
    for (Comparable<?> key : keySet()) {
      int count = get(key);
      for (int i = 0; i < count; i++)
        f.addValue(key);
    }
    return f;
  }

  public SummaryStatistics getSummaryStatistics() throws NumberFormatException {
    SummaryStatistics s = new SummaryStatistics();
    for (Comparable<?> key : keySet()) {
      int count = get(key);
      for (int i = 0; i < count; i++)
        s.addValue(Double.valueOf(key.toString()));
    }
    return s;
  }

  public DescriptiveStatistics getDescriptiveStatistics() throws NumberFormatException {
    DescriptiveStatistics s = new DescriptiveStatistics();
    for (Comparable<?> key : keySet()) {
      int count = get(key);
      for (int i = 0; i < count; i++)
        s.addValue(Double.valueOf(key.toString()));
    }
    return s;
  }
}
