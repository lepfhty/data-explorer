package edu.usc.chla.vpicu.explorer.newui.connection;

import java.awt.GridBagConstraints;

import javax.swing.ImageIcon;

import edu.usc.chla.vpicu.explorer.BaseProvider;
import edu.usc.chla.vpicu.explorer.H2Provider;

public class H2Tab extends ConnectionTab {

  private static final long serialVersionUID = 1L;

  public static final String DBFILE = "Database File";
  public static final String USERNAME = "Username";
  public static final String PASSWORD = "Password";

  private static final ImageIcon DB_ICON = new ImageIcon(H2Tab.class.getClassLoader().getResource("dbfile.png"));

  private final FileChooserButton chooser;

  public H2Tab() {
    addLabel(DBFILE);
    chooser = new FileChooserButton(DBFILE, DB_ICON);
    chooser.setSuffix(".h2.db");
    add(chooser, gbc(1,row++,1,1,1,0,GridBagConstraints.LINE_START,GridBagConstraints.HORIZONTAL));

    addInputRow(USERNAME);
    fields.get(USERNAME).setText("sa");
    addLabel(PASSWORD);
    addPasswordField(PASSWORD);

    addVerticalGlue();
  }

  @Override
  public BaseProvider getProvider() {
    return new H2Provider(chooser.getSelectedFile(),
        fields.get(USERNAME).getText(),
        fields.get(PASSWORD).getText());
  }

}
