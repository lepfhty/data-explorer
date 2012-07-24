package edu.usc.chla.vpicu.explorer.newui.connection;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import edu.usc.chla.vpicu.explorer.BaseProvider;
import edu.usc.chla.vpicu.explorer.OracleProvider;

public class OracleTab extends JDBCTab implements ActionListener {

  private static final long serialVersionUID = 1L;
  
  public static final String SERVICE = "Service";
  public static final String SID = "SID";
  
  private final Map<String, AbstractButton> buttons = new HashMap<String, AbstractButton>();
  
  public OracleTab() {
    addConnectionFields();
    addServiceRows();
    addJDBCFields();
    addVerticalGlue();
  }
  
  private void addServiceRows() {
    ButtonGroup group = new ButtonGroup();
    JRadioButton sid = new JRadioButton("SID");
    group.add(sid);
    buttons.put(SID, sid);
    sid.addActionListener(this);
    add(sid, gbc(0,row,1,1,0,0,GridBagConstraints.LINE_START,GridBagConstraints.NONE));
    JTextField sidf = new JTextField();
    fields.put(SID, sidf);
    add(sidf, gbc(1,row++,1,1,1,0,GridBagConstraints.LINE_START,GridBagConstraints.HORIZONTAL));
    
    JRadioButton svc = new JRadioButton("Service");
    group.add(svc);
    buttons.put(SERVICE, svc);
    svc.addActionListener(this);
    add(svc, gbc(0,row,1,1,0,0,GridBagConstraints.LINE_START,GridBagConstraints.NONE));
    JTextField svcf = new JTextField();
    fields.put(SERVICE, svcf);
    add(svcf, gbc(1,row++,1,1,1,0,GridBagConstraints.LINE_START,GridBagConstraints.HORIZONTAL));
    
    
  }

  @Override
  public BaseProvider getProvider() {
    return new OracleProvider(fields.get(HOST).getText(),
        Integer.parseInt(fields.get(PORT).getText()),
        fields.get(USERNAME).getText(),
        fields.get(PASSWORD).getText(),
        buttons.get(SID).isSelected() ? fields.get(SID).getText() : fields.get(SERVICE).getText(),
        buttons.get(SERVICE).isSelected());
  }
  
  @Override
  public void actionPerformed(ActionEvent e) {
    fields.get(SID).setEnabled(buttons.get(SID).isSelected());
    fields.get(SERVICE).setEnabled(buttons.get(SERVICE).isSelected());
  }
  
}
