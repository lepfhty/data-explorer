package edu.usc.chla.vpicu.explorer.newui.connection;

import java.awt.Component;
import java.awt.FileDialog;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import edu.usc.chla.vpicu.explorer.BaseProvider;
import edu.usc.chla.vpicu.explorer.H2Provider;

public class CSVTab extends ConnectionTab implements SaveFileCallback {

  private static final long serialVersionUID = 1L;

  public static final String TABLENAME = "Table Name";
  public static final String CSVFILE = "CSV File";
  public static final String ADDTABLE = "Add Table";
  public static final String REMOVETABLE = "Remove Table";
  public static final String SAVEDB = "Save Database ...";

  protected final Map<String, JButton> buttons = new HashMap<String, JButton>();
  protected final Map<String, FileChooserButton> choosers = new HashMap<String, FileChooserButton>();
  private final JTable tables;
  private final DefaultTableModel model;
  private File h2db;

  public CSVTab() {
    FileChooserButton c = new FileChooserButton(CSVFILE);
    c.setSuffix(".csv");
    choosers.put(CSVFILE, c);
    add(c, gbc(0,row,1,1,0.5,0,GridBagConstraints.LINE_START,GridBagConstraints.HORIZONTAL));

    JTextField f = new HintTextField(TABLENAME);
    fields.put(TABLENAME, f);
    add(f, gbc(1,row,1,1,0.5,0,GridBagConstraints.LINE_START,GridBagConstraints.HORIZONTAL));

    JButton b = new JButton(ADDTABLE);
    b.setEnabled(false);
    buttons.put(ADDTABLE, b);
    add(b, gbc(2,row++,1,1,0,0,GridBagConstraints.LINE_START,GridBagConstraints.HORIZONTAL));

    model = new CSVTableModel();
    tables = new JTable(model);
    tables.setFillsViewportHeight(true);
    tables.setRowSelectionAllowed(true);
    tables.setColumnSelectionAllowed(false);
    tables.getColumnModel().getColumn(0).setHeaderValue(CSVFILE);
    tables.getColumnModel().getColumn(1).setHeaderValue(TABLENAME);
    tables.getColumnModel().getColumn(0).setCellRenderer(new TooltipCellRenderer());
    JScrollPane sp = new JScrollPane(tables);
    add(sp, gbc(0,row,2,2,1,1,GridBagConstraints.LINE_START,GridBagConstraints.BOTH));

    b = new JButton(REMOVETABLE);
    b.setEnabled(false);
    buttons.put(REMOVETABLE, b);
    add(b, gbc(2,row++,1,1,0,0,GridBagConstraints.LINE_START,GridBagConstraints.HORIZONTAL));

    c = new FileChooserButton(SAVEDB);
    c.setMode(FileDialog.SAVE);
    c.setSaveFileCallback(this);
    c.setEnabled(false);
    choosers.put(SAVEDB, c);
    add(c, gbc(2,row++,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL));

    // enable import button when table is named and file is chosen
    ImportButtonEnabledListener importListener = new ImportButtonEnabledListener(
        fields.get(TABLENAME), choosers.get(CSVFILE), buttons.get(ADDTABLE));
    choosers.get(CSVFILE).addPropertyChangeListener(AbstractButton.TEXT_CHANGED_PROPERTY, importListener);
    fields.get(TABLENAME).getDocument().addDocumentListener(importListener);

    // create row when "Import" button clicked
    buttons.get(ADDTABLE).addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        String file = choosers.get(CSVFILE).getSelectedFile().getAbsolutePath();
        String name = fields.get(TABLENAME).getText();
        model.addRow(new Object[] { file, name });
      }

    });

    // delete row when "Delete" button clicked
    buttons.get(REMOVETABLE).addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        model.removeRow(tables.getSelectedRow());
      }

    });

    // enabled "Delete" button when row selected
    tables.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

      @Override
      public void valueChanged(ListSelectionEvent arg0) {
        buttons.get(REMOVETABLE).setEnabled(tables.getSelectedRow() > -1);
      }

    });

    // enabled "Save" button when row exists
    model.addTableModelListener(new TableModelListener() {

      @Override
      public void tableChanged(TableModelEvent e) {
        choosers.get(SAVEDB).setEnabled(tables.getRowCount() > 0);
      }

    });

  }

  @Override
  public void saveFile(File selectedFile) {
    h2db = selectedFile;
    for (int r = 0; r < model.getRowCount(); r++) {
      File csv = new File((String)model.getValueAt(r, 0));
      String table = (String)model.getValueAt(r, 1);
      H2Provider p = new H2Provider(h2db, "sa", "");
      StringBuilder sql = new StringBuilder("CREATE TABLE ");
      sql.append(table).append(" AS SELECT * FROM CSVREAD('").append(csv.getPath()).append("');");
      System.out.println(sql.toString());
      p.execute(sql.toString());
    }
  }

  @Override
  public BaseProvider getProvider() {
    if (h2db == null) {
      try {
        h2db = File.createTempFile("csv", ".h2.db");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return new H2Provider(h2db, "sa", "");
  }

  private static class TooltipCellRenderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = 1L;
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column) {
      super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      setToolTipText(value.toString());
      return this;
    }
  }

  private static class CSVTableModel extends DefaultTableModel {
    private static final long serialVersionUID = 1L;
    public CSVTableModel() {
      super(0,2);
    }
    @Override
    public boolean isCellEditable(int r, int c) {
      return false;
    }
  }

  private static class ImportButtonEnabledListener implements PropertyChangeListener, DocumentListener {
    private final JTextField field;
    private final FileChooserButton chooser;
    private final JButton button;
    public ImportButtonEnabledListener(JTextField field, FileChooserButton chooser, JButton button) {
      this.field = field;
      this.chooser = chooser;
      this.button = button;
    }
    private void update() {
      button.setEnabled(!field.getText().isEmpty() && chooser.getSelectedFile() != null);
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
    public void propertyChange(PropertyChangeEvent evt) {
      if (evt.getPropertyName().equals(AbstractButton.TEXT_CHANGED_PROPERTY))
        update();
    }
  }

}
