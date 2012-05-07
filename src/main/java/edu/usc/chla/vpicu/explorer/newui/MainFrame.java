package edu.usc.chla.vpicu.explorer.newui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {

  private static final long serialVersionUID = 1L;

  public MainFrame() {
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    JPanel vocab = new VocabPanel(new VocabInputPanel(), new VocabTablePanel());

    JPanel sample = new SamplePanel(new SampleInputPanel());

    JPanel synonyms = new SynonymPanel();

    JPanel status = new StatusPanel();
    
    setLayout(new BorderLayout());
    add(vocab, BorderLayout.CENTER);
    add(sample, BorderLayout.EAST);
    add(synonyms, BorderLayout.WEST);
    add(status, BorderLayout.SOUTH);

    pack();
  }

  public static void main(String[] args) {
//    ApplicationContext ctx = new ClassPathXmlApplicationContext("META-INF/context.xml");
//    JtdsProvider jtds = (JtdsProvider) ctx.getBean("jtdsProvider");
//    MySqlProvider mysql = (MySqlProvider) ctx.getBean("mysqlProvider");
//    OracleProvider oracle  = (OracleProvider) ctx.getBean("oracleProvider");
    MainFrame m = new MainFrame();
    m.setVisible(true);
  }

}
