package edu.usc.chla.vpicu.explorer.ui;

import java.awt.BorderLayout;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.apache.commons.math3.stat.Frequency;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYBarDataset;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.usc.chla.vpicu.explorer.BaseProvider;
import edu.usc.chla.vpicu.explorer.JtdsProvider;
import edu.usc.chla.vpicu.explorer.MySqlProvider;
import edu.usc.chla.vpicu.explorer.OracleProvider;

public class MainFrame extends JFrame {

  private static final long serialVersionUID = 1L;

  public MainFrame(BaseProvider prov) {
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    InputPanel input = new InputPanel(prov);
    final ChartPanel chartPanel = new ChartPanel(null);

    JSplitPane inputChartSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, input, chartPanel);

    final DistinctValuesPanel distinctPanel = new DistinctValuesPanel(prov);
    input.addFilterTextListener(distinctPanel);

    JSplitPane chartDistinctSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inputChartSplit, new JScrollPane(distinctPanel));
    add(chartDistinctSplit, BorderLayout.CENTER);

    input.addSampleListener(new SampleListener() {

      public void samplePerformed(Frequency f) {
        System.out.println(f);
        DefaultXYDataset dataset = new DefaultXYDataset();
        double[][] data = new double[2][f.getUniqueCount()];
        int i = 0;
        double width = 10;
        Double prev = Double.NaN;
        long maxCount = -1;
        for (Iterator<Comparable<?>> iter = f.valuesIterator(); iter.hasNext();) {
          Comparable<?> value = iter.next();
          data[0][i] = (Double)value;
          data[1][i] = f.getPct(value);
          if (!prev.isNaN())
            width = Math.min(width, Math.abs(prev - data[0][i]));
          maxCount = Math.max(maxCount, f.getCount(value));
          prev = data[0][i++];
        }
        dataset.addSeries("Sample", data);
        JFreeChart chart = ChartFactory.createXYBarChart("Histogram", "Value", false, "Percent",
		        new XYBarDataset(dataset, width), PlotOrientation.VERTICAL, false, true, false);
        XYPlot plot = chart.getXYPlot();
        NumberAxis axis = new NumberAxis("Count");
        axis.setRange(0, maxCount * (1 + axis.getUpperMargin()));
        plot.setRangeAxis(1, axis);
        chartPanel.setChart(chart);
      }

      public void parameterUpdated(String name, Object value) {
      }

    });

    input.addSampleListener(distinctPanel);

    getRootPane().setDefaultButton(input.getDefaultButton());
    pack();
  }

  public static void main(String[] args) {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("META-INF/context.xml");
    JtdsProvider jtds = (JtdsProvider) ctx.getBean("jtdsProvider");
    MySqlProvider mysql = (MySqlProvider) ctx.getBean("mysqlProvider");
    OracleProvider oracle  = (OracleProvider) ctx.getBean("oracleProvider");
    new MainFrame(jtds).setVisible(true);
  }

}
