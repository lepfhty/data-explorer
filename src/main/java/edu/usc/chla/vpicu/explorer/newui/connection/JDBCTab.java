package edu.usc.chla.vpicu.explorer.newui.connection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import org.apache.tomcat.jdbc.pool.DataSource;

import edu.usc.chla.vpicu.explorer.BaseProvider;

abstract class JDBCTab extends ConnectionTab implements FocusListener, ActionListener {

  private static final long serialVersionUID = 1L;

  public static final String JDBCURL = "JDBC URL";
  public static final String DRIVERCLASS = "Driver Class";
  public static final String HOST = "Host";
  public static final String PORT = "Port";
  public static final String USERNAME = "Username";
  public static final String PASSWORD = "Password";

  public JDBCTab() {
    addConnectionFields();
    addCustomFields();
    addJdbcFields();
    addVerticalGlue();
    fields.get(PORT).setText(String.valueOf(getDefaultPort()));
    fillJdbcFields();
  }

  @Override
  public boolean requiredFieldsSet() {
    String u = fields.get(USERNAME).getText();
    String pw = fields.get(PASSWORD).getText();
    String h = fields.get(HOST).getText();
    String p = fields.get(PORT).getText();
    return u != null && !u.isEmpty() && pw != null && !pw.isEmpty() &&
        h != null && !h.isEmpty() && p != null && !p.isEmpty();
  }

  protected void addJdbcFields() {
    addSeparator();
    addInputRow(JDBCURL);
    addInputRow(DRIVERCLASS);
    fields.get(JDBCURL).setEnabled(false);
    fields.get(DRIVERCLASS).setEnabled(false);
  }

  protected void addConnectionFields() {
    addInputRow(USERNAME);
    addLabel(PASSWORD);
    addPasswordField(PASSWORD);
    addInputRow(HOST);
    addInputRow(PORT);
    addJdbcFieldListeners(HOST, PORT);
  }

  protected abstract void addCustomFields();

  protected abstract int getDefaultPort();

  @Override
  public void focusGained(FocusEvent e) {
  }

  @Override
  public void focusLost(FocusEvent e) {
    fillJdbcFields();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    fillJdbcFields();
  }

  protected void fillJdbcFields() {
    BaseProvider p = getProvider();
    DataSource ds = (DataSource)p.getDataSource();
    fields.get(JDBCURL).setText(ds.getUrl());
    fields.get(DRIVERCLASS).setText(ds.getDriverClassName());
  }

  protected void addJdbcFieldListeners(String... keys) {
    for (String k : keys) {
      fields.get(k).addFocusListener(this);
      fields.get(k).addActionListener(this);
    }
  }

}
