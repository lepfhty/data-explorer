package edu.usc.chla.vpicu.explorer.newui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

import edu.usc.chla.vpicu.explorer.BaseProvider;
import edu.usc.chla.vpicu.explorer.newui.connection.ConnectionDialog;

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
    JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, vocab, sample);
    add(sp, BorderLayout.CENTER);
    //add(synonyms, BorderLayout.WEST);
    //add(status, BorderLayout.SOUTH);
    
    /*
    JMenuBar menubar = new JMenuBar();
    JMenu conMenu = new JMenu("Connection");
    ButtonGroup group = new ButtonGroup();
    JRadioButtonMenuItem cerner = new JRadioButtonMenuItem("Cerner");
    cerner.setSelected(true);
    group.add(cerner);
    conMenu.add(cerner);
    JRadioButtonMenuItem ismnew = new JRadioButtonMenuItem("ISM New");
    group.add(ismnew);
    conMenu.add(ismnew);
    menubar.add(conMenu);
    setJMenuBar(menubar);
		*/

    pack();
  }

  public static void main(String[] args) {
    ConnectionDialog d = new ConnectionDialog();
    d.setVisible(true);
    if (d.getProvider() == null)
      System.exit(0);
    MainFrame m = new MainFrame(d.getProvider());
    m.setVisible(true);
  }

}
