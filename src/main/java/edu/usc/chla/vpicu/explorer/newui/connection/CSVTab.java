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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

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
  private JTable tables;
  private DefaultTableModel model;
  private JTree tree;
  private CSVTreeModel tmodel;
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

    //JScrollPane sp = new JScrollPane(createImportTable());
    JScrollPane sp = new JScrollPane(createImportTree());
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

    //addImportTableListeners();
    addImportTreeListeners();
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
      p.execute(sql.toString());
    }
  }

  @Override
  public BaseProvider getProvider() {
    if (h2db == null) {
      try {
        h2db = File.createTempFile("csv", ".tmp");
        h2db.deleteOnExit();
        saveFile(h2db);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return new H2Provider(h2db, "sa", "");
  }

  private JTable createImportTable() {
    model = new CSVTableModel();
    tables = new JTable(model);
    tables.setFillsViewportHeight(true);
    tables.setRowSelectionAllowed(true);
    tables.setColumnSelectionAllowed(false);
    tables.getColumnModel().getColumn(0).setHeaderValue(CSVFILE);
    tables.getColumnModel().getColumn(1).setHeaderValue(TABLENAME);
    tables.getColumnModel().getColumn(0).setCellRenderer(new TooltipCellRenderer());

    // enable "Delete" button when row selected
    tables.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

      @Override
      public void valueChanged(ListSelectionEvent arg0) {
        buttons.get(REMOVETABLE).setEnabled(tables.getSelectedRow() > -1);
      }

    });

    // enable "Save" button when row exists
    model.addTableModelListener(new TableModelListener() {

      @Override
      public void tableChanged(TableModelEvent e) {
        choosers.get(SAVEDB).setEnabled(tables.getRowCount() > 0);
      }

    });

    return tables;
  }

  private void addImportTableListeners() {
    // create row when "Add Table" button clicked
    buttons.get(ADDTABLE).addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        String file = choosers.get(CSVFILE).getSelectedFile().getAbsolutePath();
        String name = fields.get(TABLENAME).getText();
        model.addRow(new Object[] { file, name });
        // clear selection
        choosers.get(CSVFILE).clearSelectedFile();
        fields.get(TABLENAME).setText("");
      }

    });

    // delete row when "Remove Table" button clicked
    buttons.get(REMOVETABLE).addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        model.removeRow(tables.getSelectedRow());
      }

    });
  }

  private JTree createImportTree() {
    tmodel = new CSVTreeModel(new DefaultMutableTreeNode());
    tree = new JTree(tmodel);
    tree.setRootVisible(false);
    tree.setShowsRootHandles(true);
    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

    // enable "Delete" button when row selected
    tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {

      @Override
      public void valueChanged(TreeSelectionEvent e) {
        buttons.get(REMOVETABLE).setEnabled(!tree.isSelectionEmpty());
      }

    });

    // enable "Save" button when row exists
    tmodel.addTreeModelListener(new TreeModelListener() {

      @Override
      public void treeNodesChanged(TreeModelEvent e) {
      }

      @Override
      public void treeNodesInserted(TreeModelEvent e) {
        choosers.get(SAVEDB).setEnabled(true);
      }

      @Override
      public void treeNodesRemoved(TreeModelEvent e) {
        choosers.get(SAVEDB).setEnabled(!tmodel.getRoot().isLeaf());
      }

      @Override
      public void treeStructureChanged(TreeModelEvent e) {
      }

    });
    return tree;
  }

  private void addImportTreeListeners() {
    // create row when "Add Table" button clicked
    buttons.get(ADDTABLE).addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        String file = choosers.get(CSVFILE).getSelectedFile().getAbsolutePath();
        String name = fields.get(TABLENAME).getText();
        tree.expandPath(tmodel.add(name, file).getParentPath());
        // clear selection
        choosers.get(CSVFILE).clearSelectedFile();
        fields.get(TABLENAME).setText("");
      }

    });

    // delete row when "Remove Table" button clicked
    buttons.get(REMOVETABLE).addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        tmodel.remove(tree.getSelectionPath());
      }

    });
  }

  private static class TooltipCellRenderer implements TableCellRenderer, TreeCellRenderer {
    private final DefaultTableCellRenderer tableCR = new DefaultTableCellRenderer();
    private final DefaultTreeCellRenderer treeCR = new DefaultTreeCellRenderer();
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column) {
      JLabel l = (JLabel)tableCR.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      l.setToolTipText(value.toString());
      return l;
    }
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
        boolean leaf, int row, boolean hasFocus) {
      JLabel l = (JLabel)treeCR.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
      l.setToolTipText(value.toString());
      return l;
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

  private static class CSVTreeModel extends DefaultTreeModel {

    private static final long serialVersionUID = 1L;

    public CSVTreeModel(DefaultMutableTreeNode root) {
      super(root);
    }

    @Override
    public DefaultMutableTreeNode getRoot() {
      return (DefaultMutableTreeNode)root;
    }

    @SuppressWarnings("unchecked")
    public TreePath add(String tableName, String csvPath) {
      TreePath path = new TreePath(getRoot());
      Enumeration<DefaultMutableTreeNode> tables = getRoot().children();
      DefaultMutableTreeNode tableNode = null;
      while (tables.hasMoreElements()) {
        DefaultMutableTreeNode n = tables.nextElement();
        if (n.getUserObject().equals(tableName)) {
          tableNode = n;
          break;
        }
      }
      if (tableNode == null) {
        tableNode = new DefaultMutableTreeNode(tableName);
        insertNodeInto(tableNode, getRoot(), getRoot().getChildCount());
      }
      path = path.pathByAddingChild(tableNode);
      DefaultMutableTreeNode child = new DefaultMutableTreeNode(csvPath, false);
      insertNodeInto(child, tableNode, tableNode.getChildCount());
      return path.pathByAddingChild(child);
    }

    public void remove(TreePath path) {
      if (path.getPathCount() == 3) { // csv file
        DefaultMutableTreeNode tableNode = (DefaultMutableTreeNode)path.getPathComponent(1);
        if (tableNode.getChildCount() == 1)
          removeNodeFromParent(tableNode);
        else
          removeNodeFromParent((DefaultMutableTreeNode)path.getLastPathComponent());
      }
      else if (path.getPathCount() == 2) { // table name
        removeNodeFromParent((DefaultMutableTreeNode)path.getLastPathComponent());
      }
    }

    @SuppressWarnings("unchecked")
    public Map<String, List<String>> getTableMap() {
      Map<String, List<String>> map = new HashMap<String, List<String>>();
      Enumeration<DefaultMutableTreeNode> tables = getRoot().children();
      while (tables.hasMoreElements()) {
        List<String> files = new ArrayList<String>();
        DefaultMutableTreeNode t = tables.nextElement();
        map.put((String)t.getUserObject(), files);
        Enumeration<DefaultMutableTreeNode> fileNodes = t.children();
        while (fileNodes.hasMoreElements()) {
          files.add((String)fileNodes.nextElement().getUserObject());
        }
      }
      return map;
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
      if (evt.getPropertyName().equals(AbstractButton.TEXT_CHANGED_PROPERTY)) {
        // auto fill "Table Name" field (which triggers update())
        File f = chooser.getSelectedFile();
        if (f != null) {
          String basename = f.getName();
          int i = basename.lastIndexOf('.');
          String name = basename.substring(0, i);
          field.setText(name);
        }
      }
    }
  }

}
