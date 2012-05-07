package edu.usc.chla.vpicu.explorer.newui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

public class VocabPanel extends JPanel {

  private static final long serialVersionUID = 1L;

  private final VocabInputPanel input;
  private final VocabTablePanel table;

  public VocabPanel(VocabInputPanel input, VocabTablePanel table) {
    this.input = input;
    this.table = table;

    setLayout(new BorderLayout());
    setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
        "Vocabulary"));

    add(this.input, BorderLayout.NORTH);
    add(this.table, BorderLayout.CENTER);
  }

}
