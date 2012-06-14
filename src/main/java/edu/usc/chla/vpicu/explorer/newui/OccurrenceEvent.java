package edu.usc.chla.vpicu.explorer.newui;

import java.util.EventObject;
import java.util.List;

import edu.usc.chla.vpicu.explorer.Column;

public class OccurrenceEvent extends EventObject {
  
  private static final long serialVersionUID = 5211807771212576817L;
  
  private final List<Object[]> queryResults;
  private final String table;
  private final Column idColumn;
  private final Object[] row;
  
  public static OccurrenceEvent createQueryResult(Object source, List<Object[]> results) {
    return new OccurrenceEvent(source, results);
  }
  
  public static OccurrenceEvent createTableChanged(Object source, String table) {
    return new OccurrenceEvent(source, table);
  }
  
  public static OccurrenceEvent createIdColumnChanged(Object source, Column id) {
    return new OccurrenceEvent(source, id);
  }
  
  public static OccurrenceEvent createRowChanged(Object source, Object[] row) {
    return new OccurrenceEvent(source, row);
  }

  protected OccurrenceEvent(Object source, List<Object[]> results) {
    super(source);
    queryResults = results;
    table = null;
    idColumn = null;
    row = null;
  }
  
  protected OccurrenceEvent(Object source, String table) {
    super(source);
    queryResults = null;
    this.table = table;
    idColumn = null;
    row = null;
  }
  
  protected OccurrenceEvent(Object source, Column id) {
    super(source);
    queryResults = null;
    table = null;
    idColumn = id;
    row = null;
  }
  
  protected OccurrenceEvent(Object source, Object[] row) {
    super(source);
    queryResults = null;
    table = null;
    idColumn = null;
    this.row = row;
  }
  
  public List<Object[]> getQueryResults() {
    return queryResults;
  }

  public String getTable() {
    return table;
  }

  public Column getIdColumn() {
    return idColumn;
  }
  
  public Object[] getRow() {
    return row;
  }
  
}
