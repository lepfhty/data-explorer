package edu.usc.chla.vpicu.explorer.newui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

public class SamplePanel extends JPanel {
  
  private static final long serialVersionUID = 1L;
  
  private SampleInputPanel input;
  
  public SamplePanel(SampleInputPanel input) {
    this.input = input;
        
    setLayout(new BorderLayout());
    setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
        "Sample"));
        
    add(this.input, BorderLayout.NORTH);
  }
}
