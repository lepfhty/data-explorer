package edu.usc.chla.vpicu.explorer.newui.connection;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import edu.usc.chla.vpicu.explorer.BaseProvider;

abstract class ConnectionTab extends JPanel {

  private static final long serialVersionUID = 1L;

  public static final String NAME = "Name";

  protected int row = 0;

  protected final Map<String, JTextField> fields = new HashMap<String, JTextField>();

  protected ConnectionTab() {
    setLayout(new GridBagLayout());
  }

  public abstract BaseProvider getProvider();

  public abstract boolean requiredFieldsSet();

  protected JLabel addLabel(String name) {
    JLabel l = new JLabel(name);
    add(l, gbc(0,row,1,1,0,0,GridBagConstraints.LINE_START,GridBagConstraints.NONE));
    return l;
  }

  protected JTextField addField(String key) {
    JTextField f = new JTextField(10);
    fields.put(key, f);
    add(f, gbc(1,row++,1,1,1,0,GridBagConstraints.LINE_START,GridBagConstraints.HORIZONTAL));
    return f;
  }

  protected JPasswordField addPasswordField(String key) {
    JPasswordField f = new JPasswordField(10);
    fields.put(key, f);
    add(f, gbc(1,row++,1,1,1,0,GridBagConstraints.LINE_START,GridBagConstraints.HORIZONTAL));
    return f;
  }

  protected void addInputRow(String name) {
    addLabel(name);
    addField(name);
  }

  protected void addSeparator() {
    add(new JSeparator(), gbc(0,row++,2,1,0,0,GridBagConstraints.LINE_START,GridBagConstraints.HORIZONTAL));
  }

  protected void addVerticalGlue() {
    add(Box.createVerticalGlue(), gbc(0,row++,2,1,1,1,GridBagConstraints.LINE_START,GridBagConstraints.BOTH));
  }

  protected GridBagConstraints gbc(int x, int y, int gw, int gh, double wx, double wy, int anchor, int fill) {
    return new GridBagConstraints(x,y,gw,gh,wx,wy,anchor,fill,new Insets(3,3,3,3),0,0);
  }

}
