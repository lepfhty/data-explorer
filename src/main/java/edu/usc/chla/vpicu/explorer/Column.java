package edu.usc.chla.vpicu.explorer;

public final class Column {

  public final String name;
  public final int type;
  public final String typeName;

  /**
   * A class representing a SQL column
   * @param n the column name
   * @param t the column data type, as defined in java.sql.Types
   * @param tn the datasource-specific type name
   * @see java.sql.DatabaseMetaData#getColumns(String, String, String, String)
   */
  public Column(String n, int t, String tn) {
    name = n;
    type = t;
    typeName = tn;
  }

  @Override
  public String toString() {
    return name + " (" + typeName + ")";
  }

}
