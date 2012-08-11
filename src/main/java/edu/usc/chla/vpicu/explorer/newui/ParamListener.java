package edu.usc.chla.vpicu.explorer.newui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.EventObject;
import java.util.Map;

import javax.swing.JTextField;

public class ParamListener implements ActionListener, FocusListener {

  private final String key;
  private final Map<String, Object> params;
  private final SqlPreviewer sp;

  public ParamListener(String key, Map<String, Object> params, SqlPreviewer sp) {
    this.key = key;
    this.params = params;
    this.sp = sp;
  }

  private void updateParams(EventObject e) {
    JTextField fld = (JTextField) e.getSource();
    try {
      int i = Integer.parseInt(fld.getText());
      params.put(key, i);
    } catch (NumberFormatException nfe) {
      params.put(key, fld.getText());
    }
    sp.updateSqlPreview();
  }

  @Override
  public void focusGained(FocusEvent e) {
  }

  @Override
  public void focusLost(FocusEvent e) {
    updateParams(e);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    updateParams(e);
  }

}
