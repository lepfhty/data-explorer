package edu.usc.chla.vpicu.explorer.newui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.event.EventListenerList;

import edu.usc.chla.vpicu.explorer.BaseProvider;
import edu.usc.chla.vpicu.explorer.Column;

public class OccurrenceInputPanel extends JPanel implements ActionListener {

  private static final long serialVersionUID = 1L;

  private final EventListenerList listeners = new EventListenerList();

  private final JCheckBox useLookup;
  private final JComboBox occTable;
  private final JComboBox occId;
  private final JComboBox occLabel;
  private final JComboBox lookupTable;
  private final JComboBox lookupId;
  private final JComboBox lookupLabel;
  private final JCheckBox showSql;
  private final JButton getVocab;
  private final JTextArea sqlPreview;
  private final JPanel sqlPreviewScroll;

  private final BaseProvider provider;
  private final Map<String, Object> params;

  public OccurrenceInputPanel(BaseProvider prov) {
    provider = prov;

    setLayout(new GridBagLayout());
    setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

    useLookup = new JCheckBox("Use Lookup Table");
    useLookup.addActionListener(this);
    add(useLookup, gbc(0,0,2,1,0,0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));

    add(new JLabel("Occurrence Table"), gbc(0,1,1,1,0,0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
    occTable = new JComboBox();
    add(occTable, gbc(1,1,1,1,1.0,0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));

    add(new JLabel("Occurrence ID"), gbc(0,2,1,1,0,0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
    occId = new JComboBox();
    add(occId, gbc(1,2,1,1,0,0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
    occLabel = new JComboBox();
    add(new JLabel("Occurrence Label"), gbc(0,3,1,1,0,0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
    add(occLabel, gbc(1,3,1,1,0,0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));

    add(new JLabel("Lookup Table"), gbc(2,1,1,1,0,0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
    lookupTable = new JComboBox();
    add(lookupTable, gbc(3,1,1,1,1.0,0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));

    add(new JLabel("Lookup ID"), gbc(2,2,1,1,0,0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
    lookupId = new JComboBox();
    add(lookupId, gbc(3,2,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
    add(new JLabel("Lookup Label"), gbc(2,3,1,1,0,0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
    lookupLabel = new JComboBox();
    add(lookupLabel, gbc(3,3,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));

    params = provider.createDefaultOccurrenceParams();
    createParameterComponents(4);

    showSql = new JCheckBox("Show SQL Preview");
    showSql.addActionListener(this);
    add(showSql, gbc(0,4+params.size(),1,1,0,0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
    getVocab = new JButton("Get Vocabulary");
    add(getVocab, gbc(3,4+params.size(),1,1,0,0, GridBagConstraints.LINE_END,GridBagConstraints.NONE));
    
    sqlPreview = new JTextArea(4, 10);
    sqlPreview.setLineWrap(true);
    sqlPreview.setWrapStyleWord(true);
    sqlPreview.setEditable(false);

    sqlPreviewScroll = new JPanel();
    sqlPreviewScroll.setLayout(new GridLayout(1,1));
    sqlPreviewScroll.add(new JScrollPane(sqlPreview));
    sqlPreviewScroll.setVisible(false);
    add(sqlPreviewScroll, gbc(0,5+params.size(),4,1,1.0,1.0, GridBagConstraints.CENTER,GridBagConstraints.BOTH));

    useLookup.doClick();

    for (String table : provider.getTables()) {
      occTable.addItem(table);
      lookupTable.addItem(table);
    }
    occTable.addActionListener(this);
    occId.addActionListener(this);
    occLabel.addActionListener(this);
    lookupTable.addActionListener(this);
    lookupId.addActionListener(this);
    lookupLabel.addActionListener(this);
    getVocab.addActionListener(this);
  }

  private void createParameterComponents(int row) {
    for (String label : params.keySet()) {
      final String key = label;
      JTextField f = new JTextField(params.get(label).toString(), 5);
      f.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
          JTextField src = (JTextField)e.getSource();
          params.put(key, src.getText());
          updateSqlPreview();
        }

      });
      f.addFocusListener(new FocusListener() {

        @Override
        public void focusGained(FocusEvent e) {
        }

        @Override
        public void focusLost(FocusEvent e) {
          JTextField src = (JTextField)e.getSource();
          params.put(key, src.getText());
          updateSqlPreview();          
        }
        
      });
      add(new JLabel(label), gbc(0,row,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
      add(f, gbc(1,row,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
      row++;
    }
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
      sqlPreviewScroll.setVisible(showSql.isSelected());
      validate();
    }
    else if (src == occTable) {
      occId.removeAllItems();
      occLabel.removeAllItems();
      String newTable = (String) occTable.getSelectedItem();
      for (Column col : provider.getColumns(newTable)) {
        occId.addItem(col);
        occLabel.addItem(col);
      }
      updateSqlPreview();
      fireOccurrenceTableChanged(newTable);
    }
    else if (src == lookupTable) {
      lookupId.removeAllItems();
      lookupLabel.removeAllItems();
      for (Column col : provider.getColumns((String)lookupTable.getSelectedItem())) {
        lookupId.addItem(col);
        lookupLabel.addItem(col);
      }
      updateSqlPreview();
    }
    else if (src == occId) {
      updateSqlPreview();
      fireOccurrenceIdColumnChanged((Column)occId.getSelectedItem());
    }
    else if (src == occLabel || src == lookupId || src == lookupLabel) {
      updateSqlPreview();
    }
    else if (src == getVocab) {
      try {
        List<Object[]> occurrences = useLookup.isSelected()
            ? provider.getOccurrences((String)occTable.getSelectedItem(),
                (Column)occId.getSelectedItem(),
                (String)lookupTable.getSelectedItem(),
                (Column)lookupId.getSelectedItem(),
                (Column)lookupLabel.getSelectedItem(),
                params)
            : provider.getOccurrences((String)occTable.getSelectedItem(),
                (Column)occId.getSelectedItem(),
                (Column)occLabel.getSelectedItem(),
                params);
        fireOccurrenceQueryPerformed(occurrences);
      } catch (NullPointerException npe) {
        JOptionPane.showMessageDialog(this, "Missing column selection", "ERROR", ERROR);
      }
    }
  }

  private void updateSqlPreview() {
    Column occIdCol = (Column)occId.getSelectedItem();
    Column occLabelCol = (Column)occLabel.getSelectedItem();
    Column lookupIdCol = (Column)lookupId.getSelectedItem();
    Column lookupLabelCol = (Column)lookupLabel.getSelectedItem();
    sqlPreview.setText(useLookup.isSelected()
        ? provider.getOccurrenceQuery((String)occTable.getSelectedItem(),
            occIdCol == null ? "null" : occIdCol.name,
            (String)lookupTable.getSelectedItem(),
            lookupIdCol == null ? "null" : lookupIdCol.name,
            lookupLabelCol == null ? "null" : lookupLabelCol.name,
            params)
        : provider.getOccurrenceQuery((String)occTable.getSelectedItem(),
            occIdCol == null ? "null" : occIdCol.name,
            occLabelCol == null ? "null" : occLabelCol.name,
            params));
  }

  public void addOccurrenceListener(OccurrenceListener l) {
    listeners.add(OccurrenceListener.class, l);
  }

  private void fireOccurrenceQueryPerformed(List<Object[]> results) {
    for (OccurrenceListener l : listeners.getListeners(OccurrenceListener.class))
      l.queryPerformed(OccurrenceEvent.createQueryResult(this, results));
  }
  
  private void fireOccurrenceTableChanged(String newTable) {
    for (OccurrenceListener l : listeners.getListeners(OccurrenceListener.class))
      l.tableChanged(OccurrenceEvent.createTableChanged(this, newTable));
  }
  
  private void fireOccurrenceIdColumnChanged(Column newId) {
    for (OccurrenceListener l : listeners.getListeners(OccurrenceListener.class))
      l.idColumnChanged(OccurrenceEvent.createIdColumnChanged(this, newId));
  }

}
