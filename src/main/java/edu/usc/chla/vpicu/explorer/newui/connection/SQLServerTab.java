package edu.usc.chla.vpicu.explorer.newui.connection;

import edu.usc.chla.vpicu.explorer.BaseProvider;
import edu.usc.chla.vpicu.explorer.JtdsProvider;

public class SQLServerTab extends JDBCTab {

  private static final long serialVersionUID = 1L;
  
  public static final String INSTANCE = "Instance";
  public static final String DATABASE = "Database";
  public static final String PROPERTIES = "Properties";
  
  @Override
  public BaseProvider getProvider() {
    return new JtdsProvider(fields.get(HOST).getText(),
        Integer.parseInt(fields.get(PORT).getText()),
        fields.get(USERNAME).getText(),
        fields.get(PASSWORD).getText(),
        fields.get(INSTANCE).getText(),
        fields.get(DATABASE).getText(),
        fields.get(PROPERTIES).getText());
  }

  @Override
  protected void addCustomFields() {
    addInputRow(INSTANCE);
    addInputRow(DATABASE);
    addInputRow(PROPERTIES);
    addJdbcFieldListeners(INSTANCE, DATABASE, PROPERTIES);
  }

  @Override
  protected int getDefaultPort() {
    return 1433;
  }
  
}
