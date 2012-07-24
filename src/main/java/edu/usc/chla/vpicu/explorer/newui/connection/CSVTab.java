package edu.usc.chla.vpicu.explorer.newui.connection;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import edu.usc.chla.vpicu.explorer.BaseProvider;

public class CSVTab extends FileTab {

  private static final long serialVersionUID = 1L;
  
  public static final String TABLENAME = "Table Name";
  public static final String CSVFILE = "CSV File";
  public static final String IMPORT = "Import";
  public static final String DELETE = "Delete";
  public static final String SAVE = "Save ...";
  
  protected final Map<String, JButton> buttons = new HashMap<String, JButton>();
  private JTable tables;
  private DefaultTableModel model;
  
  public CSVTab() {
    addInputRow(TABLENAME);
    addFileChooserRow(CSVFILE);
    addButtonRow(IMPORT);
    addTableList();
    addButtonRow(DELETE, SAVE);
    buttons.get(IMPORT).setEnabled(false);
    buttons.get(DELETE).setEnabled(false);
    buttons.get(SAVE).setEnabled(false);
    
    ImportButtonEnabledListener importListener = new ImportButtonEnabledListener(
        fields.get(TABLENAME), fileChoosers.get(CSVFILE), buttons.get(IMPORT));
    fileChoosers.get(CSVFILE).addActionListener(importListener);
    fields.get(TABLENAME).getDocument().addDocumentListener(importListener);
    
    buttons.get(IMPORT).addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent arg0) {
        String name = fields.get(TABLENAME).getText();
        String file = fileChoosers.get(CSVFILE).getSelectedFile().getAbsolutePath();
        model.addRow(new Object[] { name, file });
      }
      
    });
    
    tables.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

      @Override
      public void valueChanged(ListSelectionEvent arg0) {
        buttons.get(DELETE).setEnabled(tables.getSelectedRow() > -1);
      }
      
    });
    
    model.addTableModelListener(new TableModelListener() {

      @Override
      public void tableChanged(TableModelEvent e) {
        buttons.get(SAVE).setEnabled(tables.getRowCount() > 0);
      }
      
    });
    
  }
  
  @Override
  public BaseProvider getProvider() {
    return null;
  }
  
  protected void addButtonRow(String... keys) {
    Box box = Box.createHorizontalBox();
    for (String key : keys) {
      JButton b = new JButton(key);
      buttons.put(key, b);
      box.add(b);
    }
    add(box, gbc(1,row++,1,1,0,0,GridBagConstraints.LINE_END,GridBagConstraints.NONE));
  }
  
  protected void addTableList() {
    model = new DefaultTableModel(0,2) {
      public boolean isCellEditable(int r, int c) {
        return false;
      }
    };
    tables = new JTable(model);
    tables.setFillsViewportHeight(true);
    tables.setRowSelectionAllowed(true);
    tables.setColumnSelectionAllowed(false);
    tables.getColumnModel().getColumn(0).setHeaderValue(TABLENAME);
    tables.getColumnModel().getColumn(1).setHeaderValue(CSVFILE);
    JScrollPane sp = new JScrollPane(tables);
    add(sp, gbc(0,row++,2,1,1,1,GridBagConstraints.LINE_START,GridBagConstraints.BOTH));
  }
  
  private static class ImportButtonEnabledListener implements ActionListener, DocumentListener {
    
    private JTextField field;
    private JFileChooser chooser;
    private JButton button;
    
    public ImportButtonEnabledListener(JTextField field, JFileChooser chooser, JButton button) {
      this.field = field;
      this.chooser = chooser;
      this.button = button;
    }
    
    private void update() {
      button.setEnabled(
          !field.getText().isEmpty() &&
          chooser.getSelectedFile() != null);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
      update();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
      update();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
      update();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      update();
    }
    
  }

}
