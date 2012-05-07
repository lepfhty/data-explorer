package edu.usc.chla.vpicu.explorer.newui;

import java.awt.BorderLayout;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

public class SampleInputPanel extends JPanel implements ActionListener {

  private static final long serialVersionUID = 1L;
  private JComboBox valueColumn;
  private JTextField samplePercent;
  private JTextField sampleRows;
  private JCheckBox showSql;
  private JButton sample;
  private JTextArea sqlPreview;
  private JPanel sqlPreviewScroll;

  public SampleInputPanel() {
    setLayout(new GridBagLayout());
    setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    
    add(new JLabel("Value Column"), gbc(0,0,1,1,0,0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
    valueColumn = new JComboBox(new Object[] {"result_val","performed_dt_tm"});
    add(valueColumn, gbc(1,0,1,1,1.0,0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
    add(new JLabel("Sample Percent"), gbc(0,1,1,1,0,0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
    samplePercent = new JTextField("20", 5);
    add(samplePercent, gbc(1,1,1,1,0,0, GridBagConstraints.LINE_START,GridBagConstraints.NONE));
    add(new JLabel("Sample Rows"), gbc(0,2,1,1,0,0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
    sampleRows = new JTextField("1000", 5);
    add(sampleRows, gbc(1,2,1,1,0,0, GridBagConstraints.LINE_START,GridBagConstraints.NONE));

    showSql = new JCheckBox("Show SQL Preview");
    showSql.addActionListener(this);
    add(showSql, gbc(0,3,1,1,0,0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
    sample = new JButton("Sample");
    add(sample, gbc(1,3,1,1,0,0, GridBagConstraints.LINE_END,GridBagConstraints.NONE));
    sqlPreview = new JTextArea("SELECT TOP 1000 result_val\n"
        + "  FROM clinical_event TABLESAMPLE(20 PERCENT)\n"
        + "  WHERE event_cd = ?", 4, 20);
    sqlPreview.setEditable(false);
    sqlPreviewScroll = new JPanel();
    sqlPreviewScroll.setLayout(new BorderLayout());
    sqlPreviewScroll.add(new JScrollPane(sqlPreview), BorderLayout.CENTER);
    sqlPreviewScroll.setVisible(false);
    add(sqlPreviewScroll, gbc(0,4,4,1,0,0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL));
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
  }

}
