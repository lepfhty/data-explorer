package edu.usc.chla.vpicu.explorer.newui;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class StatusPanel extends JPanel {

  private static final long serialVersionUID = 1L;
  
  private final JLabel left = new JLabel("l");
  private final JLabel center = new JLabel("c");
  private final JLabel right = new JLabel("r");
  
  public StatusPanel() {
    setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
    setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    
    add(left);
    add(Box.createHorizontalGlue());
    add(center);
    add(Box.createHorizontalGlue());
    add(right);
  }
  
  public void setLeftStatus(String status) {
    left.setText(status);
  }
  
  public String getLeftStatus() {
    return left.getText();
  }

  public void setCenterStatus(String status) {
    center.setText(status);
  }
  
  public String getCenterStatus() {
    return center.getText();
  }

  public void setRightStatus(String status) {
    right.setText(status);
  }
  
  public String getRightStatus() {
    return right.getText();
  }

}
