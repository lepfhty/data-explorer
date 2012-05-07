package edu.usc.chla.vpicu.explorer.newui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

public class VocabInputPanel extends JPanel implements ActionListener {

  private static final long serialVersionUID = 1L;
  private final JCheckBox useLookup;
  private final JComboBox occTable;
  private final JComboBox occId;
  private final JComboBox occLabel;
  private final JTextField occSample;
  private final JComboBox lookupTable;
  private final JComboBox lookupId;
  private final JComboBox lookupLabel;
  private final JCheckBox showSql;
  private final JButton getVocab;
  private final JTextArea sqlPreview;

  public VocabInputPanel() {
    setLayout(new GridBagLayout());
    setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

    useLookup = new JCheckBox("Use Lookup Table");
    useLookup.addActionListener(this);
    add(useLookup, gbc(0,0,2,1,0,0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));

    add(new JLabel("Occurrence Table"), gbc(0,1,1,1,0,0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
    occTable = new JComboBox(new Object[] {"clinical_event", "lu_clinical_events"});
    add(occTable, gbc(1,1,1,1,1.0,0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
    add(new JLabel("Occurrence ID"), gbc(0,2,1,1,0,0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
    occId = new JComboBox(new Object[] {"event_cd", "event_cd_desc"});
    add(occId, gbc(1,2,1,1,1.0,0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
    occLabel = new JComboBox(new Object[] {"event_cd", "event_cd_desc"});
    add(new JLabel("Occurrence Label"), gbc(0,3,1,1,0,0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
    add(occLabel, gbc(1,3,1,1,1.0,0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
    add(new JLabel("Occurrence Sample %"), gbc(0,4,1,1,0,0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
    occSample = new JTextField("5", 5);
    add(occSample, gbc(1,4,1,1,0,0, GridBagConstraints.LINE_START,GridBagConstraints.NONE));

    add(new JLabel("Lookup Table"), gbc(2,1,1,1,0,0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
    lookupTable = new JComboBox(new Object[] {"clinical_event", "lu_clinical_events"});
    add(lookupTable, gbc(3,1,1,1,1.0,0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
    add(new JLabel("Lookup ID"), gbc(2,2,1,1,0,0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
    lookupId = new JComboBox(new Object[] {"event_cd", "event_cd_desc"});
    add(lookupId, gbc(3,2,1,1,1.0,0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
    add(new JLabel("Lookup Label"), gbc(2,3,1,1,0,0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
    lookupLabel = new JComboBox(new Object[] {"event_cd", "event_cd_desc"});
    add(lookupLabel, gbc(3,3,1,1,1.0,0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));

    showSql = new JCheckBox("Show SQL Preview");
    showSql.addActionListener(this);
    add(showSql, gbc(0,5,1,1,0,0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
    getVocab = new JButton("Get Vocabulary");
    add(getVocab, gbc(3,5,1,1,0,0, GridBagConstraints.LINE_END,GridBagConstraints.NONE));
    sqlPreview = new JTextArea("SELECT l.event_cd, l.event_cd_desc, COUNT(o.event_cd) AS count\n"
        + "  FROM clinical_event o TABLESAMPLE(5 PERCENT)\n"
        + "  INNER JOIN lu_clinical_events l\n"
        + "  ON o.event_cd = l.event_cd\n"
        + "  GROUP BY l.event_cd, l.event_cd_desc\n"
        + "  ORDER BY count DESC");
    sqlPreview.setVisible(false);
    add(sqlPreview, gbc(0,6,4,1,0,0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));

    useLookup.doClick();
  }

  private static GridBagConstraints gbc(int x, int y, int gw, int gh, double wx, double wy, int anchor, int fill) {
    return new GridBagConstraints(x,y,gw,gh,wx,wy,anchor,fill,new Insets(3,3,3,3),0,0);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Object src = e.getSource();
    if (src == useLookup) {
      // enable/disable comboboxes
      boolean b = useLookup.isSelected();
      occLabel.setEnabled(!b);
      lookupTable.setEnabled(b);
      lookupId.setEnabled(b);
      lookupLabel.setEnabled(b);
    }
    else if (src == showSql) {
      // show/hide sql textarea
      sqlPreview.setVisible(showSql.isSelected());
    }
  }


}
