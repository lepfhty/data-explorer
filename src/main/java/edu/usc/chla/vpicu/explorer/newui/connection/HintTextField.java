package edu.usc.chla.vpicu.explorer.newui.connection;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

public class HintTextField extends JTextField implements FocusListener {

  private static final long serialVersionUID = 1L;

  private final String hint;
  private final Font valueFont;
  private final Font hintFont;

  public HintTextField(String hint) {
    this.hint = hint;
    valueFont = getFont();
    hintFont = valueFont.deriveFont(Font.ITALIC);
    addFocusListener(this);
    focusLost(null);
  }

  @Override
  public void focusGained(FocusEvent e) {
    if (isHint()) {
      setText("");
      setForeground(Color.BLACK);
      setFont(valueFont);
    }
  }

  @Override
  public void focusLost(FocusEvent e) {
    if (getText().isEmpty()) {
      setText(hint);
      setForeground(Color.LIGHT_GRAY);
      setFont(hintFont);
    }
  }

  @Override
  public String getText() {
    if (isHint())
      return "";
    return super.getText();
  }

  private boolean isHint() {
    return super.getText().equals(hint) && getForeground() == Color.LIGHT_GRAY && getFont().equals(hintFont);
  }

}
