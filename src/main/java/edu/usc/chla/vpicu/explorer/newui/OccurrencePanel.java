package edu.usc.chla.vpicu.explorer.newui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import edu.usc.chla.vpicu.explorer.BaseProvider;

public class OccurrencePanel extends JPanel {

  private static final long serialVersionUID = 1L;

  private final BaseProvider provider;
  private final OccurrenceInputPanel input;
  private final OccurrenceTablePanel table;

  public OccurrencePanel(BaseProvider prov) {
    this.provider = prov;
    this.input = new OccurrenceInputPanel(provider);
    this.table = new OccurrenceTablePanel(provider);
    input.addOccurrenceListener(table);

    setLayout(new BorderLayout());
    setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
        "Vocabulary"));

    add(this.input, BorderLayout.NORTH);
    add(this.table, BorderLayout.CENTER);
  }

  public OccurrenceInputPanel getInputPanel() {
    return input;
  }
  
  public OccurrenceTablePanel getTablePanel() {
    return table;
  }

}
