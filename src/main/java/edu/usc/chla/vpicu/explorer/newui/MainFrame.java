package edu.usc.chla.vpicu.explorer.newui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.usc.chla.vpicu.explorer.BaseProvider;
import edu.usc.chla.vpicu.explorer.JtdsProvider;

public class MainFrame extends JFrame {

  private static final long serialVersionUID = 1L;

  public MainFrame(BaseProvider provider) {
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    OccurrencePanel vocab = new OccurrencePanel(provider);

    SamplePanel sample = new SamplePanel(provider);
    vocab.getInputPanel().addOccurrenceListener(sample.getInputPanel());
    vocab.getTablePanel().addOccurrenceListener(sample.getInputPanel());
    
    //JPanel synonyms = new SynonymPanel();

    //JPanel status = new StatusPanel();

    setLayout(new BorderLayout());
    add(vocab, BorderLayout.CENTER);
    add(sample, BorderLayout.EAST);
    //add(synonyms, BorderLayout.WEST);
    //add(status, BorderLayout.SOUTH);

    pack();
  }

  public static void main(String[] args) {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("META-INF/context.xml");
    JtdsProvider jtds = (JtdsProvider) ctx.getBean("jtdsProvider");
//    MySqlProvider mysql = (MySqlProvider) ctx.getBean("mysqlProvider");
//    OracleProvider oracle  = (OracleProvider) ctx.getBean("oracleProvider");
    MainFrame m = new MainFrame(jtds);
    m.setVisible(true);
  }

}
