package edu.usc.chla.vpicu.explorer;

import java.util.TreeMap;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYBarDataset;

public class Histogram extends TreeMap<Comparable<?>, Long> {

  private static final long serialVersionUID = -1439228478252799490L;
  
  private boolean init = false;
  private boolean numeric = false;
  
  @Override
  public Long put(Comparable<?> key, Long value) {
    // don't handle empty strings
    if (key == null || key.toString().trim().isEmpty())
      return null;
    
    if (init) {
      // put numeric
      if (numeric) {
        try {
          double d = Double.valueOf(key.toString());
          return super.put(d, value);
        } catch (NumberFormatException e) {
          // do nothing
          return null;
        }
      }
      // put other
      return super.put(key, value);
    }
    
    // first put attempt
    Double d = Double.NaN;
    try {
      d = Double.valueOf(key.toString());
    } catch (NumberFormatException e) {
      // put other
      init = true;
      numeric = false;
      return super.put(key, value);
    }
    // put numeric
    init = true;
    numeric = true;
    return super.put(d, value);
  }
  
  private long getMaxCount() {
    long max = -1;
    for (Comparable<?> key : keySet()) {
      max = Math.max(max, get(key));
    }
    return max;
  }
  
  private long getTotalCount() {
    long sum = 0;
    for (Comparable<?> key : keySet()) {
      sum += get(key);
    }
    return sum;
  }

  public Frequency getFrequency() {
    Frequency f = new Frequency();
    for (Comparable<?> key : keySet()) {
      long count = get(key);
      for (long i = 0; i < count; i++)
        f.addValue(key);
    }
    return f;
  }

  public SummaryStatistics getSummaryStatistics() throws NumberFormatException {
    SummaryStatistics s = new SummaryStatistics();
    for (Comparable<?> key : keySet()) {
      long count = get(key);
      for (long i = 0; i < count; i++)
        s.addValue(Double.valueOf(key.toString()));
    }
    return s;
  }

  public DescriptiveStatistics getDescriptiveStatistics() throws NumberFormatException {
    DescriptiveStatistics s = new DescriptiveStatistics();
    for (Comparable<?> key : keySet()) {
      long count = get(key);
      for (long i = 0; i < count; i++)
        s.addValue(Double.valueOf(key.toString()));
    }
    return s;
  }
  
  public JFreeChart getChart(String title) {
    JFreeChart chart = null;
    if (numeric) {
      DefaultXYDataset xyset = new DefaultXYDataset();
      int i = 0;
      double[][] data = new double[2][size()];
      Double prev = Double.NaN;
      double width = 10;
      for (Comparable<?> key : keySet()) {
        data[0][i] = (Double)key;
        data[1][i] = get(key);
        if (!prev.isNaN())
          width = Math.min(width, Math.abs(prev - data[0][i]));
        prev = data[0][i++];
      }
      xyset.addSeries("sample", data);
      chart = ChartFactory.createXYBarChart(title,
          "Value", false, "Count", new XYBarDataset(xyset, width),
          PlotOrientation.VERTICAL, false, true, false);
    }
    else {
      DefaultCategoryDataset dataset = new DefaultCategoryDataset();
      for (Comparable<?> key : keySet()) {
        dataset.addValue(get(key), "sample", key);
      }
      chart = ChartFactory.createBarChart(title,
          "Values", "Count", dataset, PlotOrientation.VERTICAL,
          false, true, false);
      // rotate x-axis labels 45 degrees
      CategoryAxis xAxis = chart.getCategoryPlot().getDomainAxis();
      xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
    }

    // add percentage y-axis
    long max = getMaxCount();
    long sum = getTotalCount();
    NumberAxis axis = new NumberAxis("Percent");
    axis.setRange(0, max * (1 + axis.getUpperMargin()) / sum * 100.0);
    if (chart.getPlot() instanceof XYPlot) {
      chart.getXYPlot().setRangeAxis(1, axis);
    } else if (chart.getPlot() instanceof CategoryPlot) {
      chart.getCategoryPlot().setRangeAxis(1, axis);
    }
    return chart;
  }
}
