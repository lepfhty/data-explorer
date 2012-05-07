package edu.usc.chla.vpicu.explorer.newui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

public class MainFrame extends JFrame {

  private static final long serialVersionUID = 1L;

  public MainFrame() {
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    JPanel vocab = new VocabPanel(new VocabInputPanel(), new VocabTablePanel());

    JPanel sample = new JPanel();
    sample.setLayout(new BorderLayout());
    sample.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Sample"));
    JPanel sampleInput = new JPanel();
    sampleInput.setLayout(new GridBagLayout());
    sampleInput.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    sampleInput.add(new JLabel("Value Column"), gbc(0,0,1,1,0,0,
        GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
    sampleInput.add(new JComboBox(new Object[] {"result_val","performed_dt_tm"}),
        gbc(1,0,1,1,1.0,0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
    sampleInput.add(new JLabel("Sample Percent"), gbc(0,1,1,1,0,0,
        GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
    sampleInput.add(new JTextField("20", 5), gbc(1,1,1,1,0,0,
        GridBagConstraints.LINE_START,GridBagConstraints.NONE));
    sampleInput.add(new JLabel("Sample Rows"), gbc(0,2,1,1,0,0,
        GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
    sampleInput.add(new JTextField("1000", 5), gbc(1,2,1,1,0,0,
        GridBagConstraints.LINE_START,GridBagConstraints.NONE));

    sampleInput.add(new JCheckBox("Show SQL Preview"), gbc(0,3,1,1,0,0,
        GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
    sampleInput.add(new JButton("Sample"), gbc(1,3,1,1,0,0,
        GridBagConstraints.LINE_END,GridBagConstraints.NONE));
    sampleInput.add(new JTextArea("SELECT TOP 1000 result_val\n"
        + "  FROM clinical_event TABLESAMPLE(20 PERCENT)\n"
        + "  WHERE event_cd = ?"), gbc(0,4,4,1,0,0,
            GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
    sample.add(sampleInput, BorderLayout.NORTH);
    sample.add(new JPanel(), BorderLayout.CENTER);

    JPanel synonyms = new JPanel();
    synonyms.setLayout(new BorderLayout());
    synonyms.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Synonyms"));
    Box termBar = Box.createHorizontalBox();
    termBar.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
    termBar.add(new JLabel("Term"));
    termBar.add(new JComboBox(new Object[] { "Heart Rate", "Temperature" }));
    synonyms.add(termBar, BorderLayout.NORTH);
    synonyms.add(new JScrollPane(new JList(new Object[] { "Heart Rate", "HR", "Pulse" })), BorderLayout.CENTER);

    setLayout(new BorderLayout());
    add(vocab, BorderLayout.CENTER);
    add(sample, BorderLayout.EAST);
    add(synonyms, BorderLayout.WEST);

    pack();
  }

  private static GridBagConstraints gbc(int x, int y, int gw, int gh, double wx, double wy, int anchor, int fill) {
    return new GridBagConstraints(x,y,gw,gh,wx,wy,anchor,fill,new Insets(3,3,3,3),0,0);
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
