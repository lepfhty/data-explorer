package edu.usc.chla.vpicu.explorer;

import java.sql.Types;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.math3.stat.Frequency;
import org.apache.tomcat.jdbc.pool.PoolConfiguration;
import org.apache.tomcat.jdbc.pool.PoolProperties;

public class JtdsProvider extends BaseProvider {

  public static final String MSSQL_TABLESAMPLE = "MSSQL TableSample Percent";
  public static final String MSSQL_TOPN = "MSSQL TopN Rows";

  @Override
  protected String getTableSchema() {
    return "dbo";
  }

  @Override
  protected String getDistinctColumnValuesQuery(String tableName, String columnName, Map<String, Object> sample) {
    return "SELECT " + columnName + ", COUNT(" + columnName + ") AS count"
        + " FROM " + tableName
        + " TABLESAMPLE(" + sample.get(MSSQL_TABLESAMPLE) + " PERCENT)"
        + " GROUP BY " + columnName
        + " ORDER BY count DESC";
  }

  @Override
  protected String getFrequencyQuery(String tableName, Column filterColumn, Object filterValue, String valueColumn,
      Map<String, Object> sample) {
    sample = fillDefaultSampleParams(sample);
    return "SELECT TOP " + sample.get(MSSQL_TOPN) + " " + valueColumn
		    + " FROM " + tableName
		    + " TABLESAMPLE (" + sample.get(MSSQL_TABLESAMPLE) + " PERCENT)"
		    + " WHERE " + filterColumn.name + " = ?";
  }

  @Override
  public Map<String, Object> fillDefaultSampleParams(Map<String, Object> given) {
    Map<String, Object> params = super.fillDefaultSampleParams(given);
    if (!params.containsKey(MSSQL_TABLESAMPLE))
      params.put(MSSQL_TABLESAMPLE, 20);
    if (!params.containsKey(MSSQL_TOPN))
      params.put(MSSQL_TOPN, 1000);
    return params;
  }

  public static void main(String[] args) {
    PoolConfiguration conf = new PoolProperties();
    conf.setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
    conf.setUrl("jdbc:jtds:sqlserver://CHLADB01.la.ad.chla.org:1433/APPDB;instance=MSS09BPM");
    conf.setUsername("vpicu");
    conf.setPassword("vpicu100");
    DataSource ds = new org.apache.tomcat.jdbc.pool.DataSource(conf);
    JtdsProvider p = new JtdsProvider();
    p.setDataSource(ds);

//    for (String t : p.getTableNames()) {
//      System.out.println(t);
//      for (Column c : p.getColumns(t))
//        System.out.println(c);
//    }

//    for (Object o : p.getDistinctColumnValues("lu_clinical_events", "event_cd"))
//      System.out.println(o.toString());

    Frequency f = p.getFrequency("clinical_event", new Column("event_cd",Types.FLOAT,"FLOAT"), 688867, "result_val", null);
    System.out.println(f);
    System.out.println(f.getSumFreq());
  }




}
