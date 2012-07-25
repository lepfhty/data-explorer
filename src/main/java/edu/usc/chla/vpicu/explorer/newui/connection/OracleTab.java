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
  
  private Map<String, AbstractButton> buttons;
  
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
  protected void addCustomFields() {
    buttons = new HashMap<String, AbstractButton>();
    ButtonGroup group = new ButtonGroup();
    
    JRadioButton sid = new JRadioButton("SID");
    sid.addFocusListener(this);
    sid.addActionListener(this);
    sid.setSelected(true);
    group.add(sid);
    buttons.put(SID, sid);
    add(sid, gbc(0,row,1,1,0,0,GridBagConstraints.LINE_START,GridBagConstraints.NONE));
    JTextField sidf = new JTextField();
    fields.put(SID, sidf);
    add(sidf, gbc(1,row++,1,1,1,0,GridBagConstraints.LINE_START,GridBagConstraints.HORIZONTAL));
    
    JRadioButton svc = new JRadioButton("Service");
    svc.addFocusListener(this);
    svc.addActionListener(this);
    group.add(svc);
    buttons.put(SERVICE, svc);
    add(svc, gbc(0,row,1,1,0,0,GridBagConstraints.LINE_START,GridBagConstraints.NONE));
    JTextField svcf = new JTextField();
    svcf.setEnabled(false);
    fields.put(SERVICE, svcf);
    add(svcf, gbc(1,row++,1,1,1,0,GridBagConstraints.LINE_START,GridBagConstraints.HORIZONTAL));
    
    addJdbcFieldListeners(SID, SERVICE);
  }

  @Override
  protected int getDefaultPort() {
    return 1521;
  }
  
  @Override
  public void actionPerformed(ActionEvent e) {
    fields.get(SID).setEnabled(buttons.get(SID).isSelected());
    fields.get(SERVICE).setEnabled(buttons.get(SERVICE).isSelected());
    super.actionPerformed(e);
  }

}
