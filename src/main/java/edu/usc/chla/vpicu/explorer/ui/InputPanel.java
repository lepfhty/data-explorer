package edu.usc.chla.vpicu.explorer.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;

import org.apache.commons.math3.stat.Frequency;

import edu.usc.chla.vpicu.explorer.BaseProvider;
import edu.usc.chla.vpicu.explorer.Column;

public class InputPanel extends JPanel {

  private static final long serialVersionUID = 1L;

  private final JComboBox tableCombo = new JComboBox();
  private final JComboBox filterCombo = new JComboBox();
  private final JTextField filterField = new JTextField();
  private final JComboBox valueCombo = new JComboBox();
  private final JButton freqButton = new JButton("Sample!");

  private final Map<String, Object> sampleParams;
  private final List<JLabel> paramLabels = new ArrayList<JLabel>();

  private final BaseProvider provider;

  public InputPanel(BaseProvider prov) {
    provider = prov;
    sampleParams = provider.fillDefaultSampleParams(null);

    GridLayout layout = new GridLayout(5 + sampleParams.size(),2,3,6);
    setLayout(layout);
    setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3),
        BorderFactory.createBevelBorder(BevelBorder.LOWERED)));

    for (String t : prov.getTableNames())
      tableCombo.addItem(t);

    tableCombo.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        // update combobox elements
        filterCombo.removeAllItems();
        valueCombo.removeAllItems();
        for (Column c : provider.getColumns((String)tableCombo.getSelectedItem())) {
          filterCombo.addItem(c);
          valueCombo.addItem(c);
        }

        // update table name
        String t = (String)tableCombo.getSelectedItem();
        if (t != null) {
	        for (FilterTextListener l : listeners.getListeners(FilterTextListener.class)) {
	          l.tableNameChanged(t);
	        }
        }
      }

    });

    filterCombo.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        // update column name
        Column c = (Column)filterCombo.getSelectedItem();
        if (c != null) {
	        for (FilterTextListener l : listeners.getListeners(FilterTextListener.class)) {
	          l.columnNameChanged(c.name);
	        }
        }
      }

    });

    freqButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        // update sampleParams
        for (JLabel l : paramLabels) {
          JTextField f = (JTextField)l.getLabelFor();
          sampleParams.put(l.getText(), f.getText());
        }

        // get histogram
        Frequency f = provider.getFrequency((String)tableCombo.getSelectedItem(),
            (Column)filterCombo.getSelectedItem(),
            filterField.getText(),
            ((Column)valueCombo.getSelectedItem()).name,
            sampleParams);

        // update sample listeners
        for (SampleListener l : listeners.getListeners(SampleListener.class)) {
          l.samplePerformed(f);
        }
      }

    });

    filterField.getDocument().addDocumentListener(new DocumentListener() {

      public void insertUpdate(DocumentEvent e) {
//        char ch = e.getKeyChar();
//        if (ch != KeyEvent.CHAR_UNDEFINED) {
          String pattern = filterField.getText();
//          if (ch != '\b' && ch != '\n')
//            pattern += ch;
	        for (FilterTextListener l : listeners.getListeners(FilterTextListener.class)) {
	          l.filterTextChanged(pattern);
	        }
//        }
      }

      public void removeUpdate(DocumentEvent e) {
      }

      public void changedUpdate(DocumentEvent e) {
      }

    });

    add(new JLabel("Table Name:"));
    add(tableCombo);
    add(new JLabel("Filter Column:"));
    add(filterCombo);
    add(new JLabel("Filter Value:"));
    add(filterField);
    add(new JLabel("Value Column:"));
    add(valueCombo);

    for (String key : sampleParams.keySet()) {
      JLabel l = new JLabel(key);
      JTextField f = new JTextField(sampleParams.get(key).toString());
      l.setLabelFor(f);
      paramLabels.add(l);
      add(l);
      add(f);
    }

    add(new JLabel());
    add(freqButton);
  }

  public JButton getDefaultButton() {
    return freqButton;
  }

  private final EventListenerList listeners = new EventListenerList();

  public void addSampleListener(SampleListener l) {
    listeners.add(SampleListener.class, l);
  }

  public void addFilterTextListener(FilterTextListener l) {
    listeners.add(FilterTextListener.class, l);
  }

}
