package edu.usc.chla.vpicu.explorer.newui.connection;

abstract class JDBCTab extends ConnectionTab {

  private static final long serialVersionUID = 1L;
  
  public static final String JDBCURL = "JDBC URL";
  public static final String DRIVERCLASS = "Driver Class";
  public static final String HOST = "Host";
  public static final String PORT = "Port";
  public static final String USERNAME = "Username";
  public static final String PASSWORD = "Password";
  
  protected void addJDBCFields() {
    addSeparator();
    addInputRow(JDBCURL);
    addInputRow(DRIVERCLASS);
  }
  
  protected void addConnectionFields() {
    addInputRow(USERNAME);
    addLabel(PASSWORD);
    addPasswordField(PASSWORD);
    addInputRow(HOST);
    addInputRow(PORT);
  }

}
