package edu.usc.chla.vpicu.explorer.newui.connection;

import edu.usc.chla.vpicu.explorer.BaseProvider;
import edu.usc.chla.vpicu.explorer.MySqlProvider;

public class MySQLTab extends JDBCTab {

  private static final long serialVersionUID = 1L;
  
  public static final String DATABASE = "Database";
  public static final String PROPERTIES = "Properties";
  
  @Override
  public BaseProvider getProvider() {
    return new MySqlProvider(fields.get(HOST).getText(),
        Integer.parseInt(fields.get(PORT).getText()),
        fields.get(USERNAME).getText(),
        fields.get(PASSWORD).getText(),
        fields.get(DATABASE).getText(),
        fields.get(PROPERTIES).getText());
  }

  @Override
  protected void addCustomFields() {
    addInputRow(DATABASE);
    addInputRow(PROPERTIES);
    addJdbcFieldListeners(DATABASE, PROPERTIES);
  }

  @Override
  protected int getDefaultPort() {
    return 3306;
  }
  
}
