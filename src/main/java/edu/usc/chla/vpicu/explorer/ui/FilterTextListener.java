package edu.usc.chla.vpicu.explorer.ui;

import java.util.EventListener;

public interface FilterTextListener extends EventListener {

  public void filterTextChanged(String newText);

  public void tableNameChanged(String newTableName);

  public void columnNameChanged(String newColumnName);
}
