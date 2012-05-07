package edu.usc.chla.vpicu.explorer.newui;

import java.util.List;

import javax.swing.table.AbstractTableModel;


public abstract class FilterTableModel extends AbstractTableModel {

  private static final long serialVersionUID = 1L;

  private List<Object[]> data;
  private Object[] columns;
  private int[] indices;
  private int size;

  public FilterTableModel(List<Object[]> data, Object[] columns) {
    setData(data, columns);
  }

  public void setData(List<Object[]> data, Object[] columns) {
    this.data = data;
    this.columns = columns;
    size = data.size();
    indices = new int[size];
    for (int i = 0; i < size; i++)
      indices[i] = i;
    fireTableDataChanged();
  }

  @Override
  public int getRowCount() {
    return size;
  }

  @Override
  public int getColumnCount() {
    return columns.length;
  }

  @Override
  public String getColumnName(int column) {
    return columns[column].toString();
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    return data.get(indices[rowIndex])[columnIndex];
  }

  public void filter() {
    int j = 0;
    for (int i = 0; i < data.size(); i++) {
      if (accept(data.get(i))) {
        indices[j++] = i;
      }
    }
    size = j;
    fireTableDataChanged();
  }

  protected abstract boolean accept(Object[] row);

}
