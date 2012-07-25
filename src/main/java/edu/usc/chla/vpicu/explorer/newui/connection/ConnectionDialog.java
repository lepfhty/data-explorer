package edu.usc.chla.vpicu.explorer.newui.connection;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import edu.usc.chla.vpicu.explorer.BaseProvider;

public class ConnectionDialog extends JDialog implements ActionListener {

  private static final long serialVersionUID = 1L;

  private final JTabbedPane tabs;
  private BaseProvider provider;

  private final JButton connect;
  private final JButton cancel;

  public ConnectionDialog() {
    setModal(true);
    setLayout(new BorderLayout());
    tabs = new JTabbedPane();
    JPanel csvtab = new CSVTab();
    JPanel h2tab = new H2Tab();
    JPanel sqlitetab = new SQLiteTab();
    JPanel mysqltab = new MySQLTab();
    JPanel oracletab = new OracleTab();
    JPanel sqlservertab = new SQLServerTab();
    tabs.add("CSV", csvtab);
    tabs.add("H2", h2tab);
    tabs.add("SQLite", sqlitetab);
    tabs.add("MySQL", mysqltab);
    tabs.add("Oracle", oracletab);
    tabs.add("SQL Server", sqlservertab);

    add(tabs);
    setPreferredSize(new Dimension(640,480));
    connect = new JButton("Connect");
    cancel = new JButton("Cancel");
    Box box = Box.createHorizontalBox();
    box.add(Box.createHorizontalGlue());
    box.add(cancel);
    box.add(connect);
    add(box, BorderLayout.SOUTH);
    pack();

    cancel.addActionListener(this);
    connect.addActionListener(this);
  }

  public BaseProvider getProvider() {
    return provider;
  }

  public static void main(String[] args) {
    new ConnectionDialog().setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == connect) {
      ConnectionTab t = (ConnectionTab)tabs.getSelectedComponent();
      provider = t.getProvider();
    }
    else
      provider = null;
    setVisible(false);
  }
}
