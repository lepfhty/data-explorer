package edu.usc.chla.vpicu.explorer.newui.connection;

import java.awt.GridBagConstraints;

import javax.swing.ImageIcon;

import edu.usc.chla.vpicu.explorer.BaseProvider;
import edu.usc.chla.vpicu.explorer.SqliteProvider;

public class SQLiteTab extends ConnectionTab {

  private static final long serialVersionUID = 1L;

  public static final String DBFILE = "Database File";

  private static final ImageIcon DB_ICON = new ImageIcon(H2Tab.class.getClassLoader().getResource("dbfile.png"));

  private final FileChooserButton chooser;

  public SQLiteTab() {
    addLabel(DBFILE);

    chooser = new FileChooserButton(DBFILE, DB_ICON);
    add(chooser, gbc(1,row++,1,1,1,0,GridBagConstraints.LINE_START,GridBagConstraints.HORIZONTAL));

    addVerticalGlue();
  }

  @Override
  public BaseProvider getProvider() {
    return new SqliteProvider(chooser.getSelectedFile());
  }

}
