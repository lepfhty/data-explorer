package edu.usc.chla.vpicu.explorer.newui.connection;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import edu.usc.chla.vpicu.explorer.BaseProvider;
import edu.usc.chla.vpicu.explorer.MySqlProvider;

public class MySQLTab extends JDBCTab {

  private static final long serialVersionUID = 1L;
  
  public static final String DATABASE = "Database";
  public static final String PROPERTIES = "Properties";
  
  public MySQLTab() {
    addConnectionFields();
    addInputRow(DATABASE);
    addInputRow(PROPERTIES);
    addJDBCFields();
    addVerticalGlue();
    
    fields.get(DRIVERCLASS).setText("com.mysql.jdbc.Driver");
    fields.get(DRIVERCLASS).setEnabled(false);
  }

  @Override
  public BaseProvider getProvider() {
    return new MySqlProvider(fields.get(HOST).getText(),
        Integer.parseInt(fields.get(PORT).getText()),
        fields.get(USERNAME).getText(),
        fields.get(PASSWORD).getText(),
        fields.get(DATABASE).getText(),
        fields.get(PROPERTIES).getText());
  }
  
  private class MySqlUrlListener implements DocumentListener {
    
    @Override
    public void insertUpdate(DocumentEvent e) {
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
      // TODO Auto-generated method stub
      
    }
    
  }
  
}
