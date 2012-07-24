package edu.usc.chla.vpicu.explorer.newui.connection;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

abstract class FileTab extends ConnectionTab {

  private static final long serialVersionUID = 1L;
  
  protected Map<String, JFileChooser> fileChoosers = new HashMap<String, JFileChooser>();
  
  protected void addFileChooserRow(String key) {
    addLabel(key);
    
    Box box = Box.createHorizontalBox();
    JButton button = new JButton("Choose ...");
    final JLabel label = new JLabel();
    box.add(button);
    box.add(label);
    
    final JFileChooser c = new JFileChooser();
    c.setFileSelectionMode(JFileChooser.FILES_ONLY);
    button.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        if (c.showDialog(FileTab.this, "Choose") == JFileChooser.APPROVE_OPTION) {
          label.setText(c.getSelectedFile().getName());
          label.setMaximumSize(new Dimension((int)label.getPreferredSize().getWidth(), Short.MAX_VALUE));
        }
      }
      
    });
    
    fileChoosers.put(key, c);
    add(box, gbc(1,row++,1,1,0,0,GridBagConstraints.LINE_START,GridBagConstraints.NONE));
  }
}
