package edu.usc.chla.vpicu.explorer.newui.connection;

import java.awt.GridBagConstraints;

import edu.usc.chla.vpicu.explorer.BaseProvider;
import edu.usc.chla.vpicu.explorer.SqliteProvider;

public class SQLiteTab extends ConnectionTab {

  private static final long serialVersionUID = 1L;

  public static final String DBFILE = "Database File";

  private final FileChooserButton chooser;

  public SQLiteTab() {
    addLabel(DBFILE);

    chooser = new FileChooserButton(DBFILE);
    add(chooser, gbc(1,row++,1,1,1,0,GridBagConstraints.LINE_START,GridBagConstraints.HORIZONTAL));

    addVerticalGlue();
  }

  @Override
  public BaseProvider getProvider() {
    return new SqliteProvider(chooser.getSelectedFile());
  }

}
