package edu.usc.chla.vpicu.explorer.newui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;

public class SynonymPanel extends JPanel {

  private static final long serialVersionUID = 1L;
  
  private JButton loadButton;
  private JButton saveButton;
  private JComboBox terms;
  private JList synonyms;
  
  public SynonymPanel() {
    setLayout(new BorderLayout());
    setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Synonyms"));
    
    Box groupBar = Box.createVerticalBox();
    Box buttonBar = Box.createHorizontalBox();
    loadButton = new JButton("Load File");
    buttonBar.add(loadButton);
    saveButton = new JButton("Save File");
    buttonBar.add(saveButton);
    groupBar.add(buttonBar);
    Box termBar = Box.createHorizontalBox();
    termBar.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
    termBar.add(new JLabel("Term"));
    terms = new JComboBox(new Object[] { "Heart Rate", "Temperature" });
    termBar.add(terms);
    groupBar.add(termBar);
    add(groupBar, BorderLayout.NORTH);
    synonyms = new JList(new Object[] { "Heart Rate", "HR", "Pulse" });
    add(new JScrollPane(synonyms), BorderLayout.CENTER);
  }

}
