package edu.usc.chla.vpicu.explorer.newui.connection;

import edu.usc.chla.vpicu.explorer.BaseProvider;
import edu.usc.chla.vpicu.explorer.SqliteProvider;

public class SQLiteTab extends FileTab {

  private static final long serialVersionUID = 1L;
  
  public static final String DBFILE = "Database File";
  
  public SQLiteTab() {
    addFileChooserRow(DBFILE);
    addVerticalGlue();
  }

  @Override
  public BaseProvider getProvider() {
    return new SqliteProvider(fileChoosers.get(DBFILE).getSelectedFile());
  }
  
}
