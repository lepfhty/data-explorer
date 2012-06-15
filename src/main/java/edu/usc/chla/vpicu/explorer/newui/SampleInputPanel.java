package edu.usc.chla.vpicu.explorer.newui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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
import edu.usc.chla.vpicu.explorer.Histogram;

public class SampleInputPanel extends JPanel implements ActionListener, OccurrenceListener {

  private static final long serialVersionUID = 1L;
  private JComboBox valueColumn;
  private JCheckBox showSql;
  private JButton sample;
  private JTextArea sqlPreview;
  private Map<String, Object> params;
  private JPanel sqlPreviewScroll;
  
  private BaseProvider provider;
  private String occTable;
  private Column occId;
  private Object[] occRow;
  
  private EventListenerList listeners = new EventListenerList();

  public SampleInputPanel(BaseProvider prov) {
    provider = prov;
    setLayout(new GridBagLayout());
    setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    
    add(new JLabel("Value Column"), gbc(0,0,1,1,0,0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
    valueColumn = new JComboBox();
    add(valueColumn, gbc(1,0,1,1,1.0,0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
    
    params = provider.createDefaultSampleParams();
    createParameterComponents(1);

    showSql = new JCheckBox("Show SQL Preview");
    showSql.addActionListener(this);
    add(showSql, gbc(0,1+params.size(),1,1,0,0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
    sample = new JButton("Sample");
    add(sample, gbc(1,1+params.size(),1,1,0,0, GridBagConstraints.LINE_END,GridBagConstraints.NONE));
    
    sqlPreview = new JTextArea(4, 10);
    sqlPreview.setLineWrap(true);
    sqlPreview.setWrapStyleWord(true);
    sqlPreview.setEditable(false);
    
    sqlPreviewScroll = new JPanel();
    sqlPreviewScroll.setLayout(new GridLayout(1,1));
    sqlPreviewScroll.add(new JScrollPane(sqlPreview));
    sqlPreviewScroll.setVisible(false);
    add(sqlPreviewScroll, gbc(0,2+params.size(),4,1,0,0, GridBagConstraints.CENTER,GridBagConstraints.BOTH));
    
    valueColumn.addActionListener(this);
    sample.addActionListener(this);
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
          params.put(key,  src.getText());
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
    if (src == showSql) {
      sqlPreviewScroll.setVisible(showSql.isSelected());
      validate();
    }
    else if (src == sample) {
      if (occTable == null || occId == null || occRow == null || valueColumn == null) {
        JOptionPane.showMessageDialog(this, "Please select an event");
        return;
      }
      Histogram h = provider.getHistogram(occTable, occId, occRow[0], (Column)valueColumn.getSelectedItem(), params);
      fireSampleQueryPerformed(h, occRow[1].toString());
    }
    else if (src == valueColumn) {
      updateSqlPreview();
    }
  }
  
  private void updateSqlPreview() {
    Column valcol = (Column)valueColumn.getSelectedItem();
    sqlPreview.setText(provider.getSampleQuery(occTable,
        occId == null ? "null" : occId.name,
        valcol == null ? "null" : valcol.name,
        params));
  }

  @Override
  public void queryPerformed(OccurrenceEvent e) {
  }

  @Override
  public void tableChanged(OccurrenceEvent e) {
    occTable = e.getTable();
    valueColumn.removeAllItems();
    for (Column col : provider.getColumns(occTable))
      valueColumn.addItem(col);
    updateSqlPreview();
  }

  @Override
  public void idColumnChanged(OccurrenceEvent e) {
    occId = e.getIdColumn();
    updateSqlPreview();
  }

  @Override
  public void rowChanged(OccurrenceEvent e) {
    occRow = e.getRow();
  }
  
  public void addSampleListener(SampleListener l) {
    listeners.add(SampleListener.class, l);
  }
  
  private void fireSampleQueryPerformed(Histogram h, String t) {
   for (SampleListener l : listeners.getListeners(SampleListener.class)) 
     l.queryPerformed(new SampleEvent(this, h, t));
  }

}
