package edu.usc.chla.vpicu.explorer;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.tomcat.jdbc.pool.PoolConfiguration;
import org.apache.tomcat.jdbc.pool.PoolProperties;

public class JtdsProvider extends BaseProvider {

  public static final String MSSQL_TABLESAMPLE = "MSSQL TableSample Percent";
  public static final String MSSQL_TOPN = "MSSQL TopN Rows";
  public static final String MSSQL_CHECKSUM = "MSSQL CheckSum Percent";

  @Override
  protected String getTableSchema() {
    return "dbo";
  }

  @Override
  public String getOccurrenceQuery(String occurrenceTable, String occurrenceIdCol,
      String lookupTable, String lookupIdCol, String labelCol, Map<String, Object> sample) {
    return MessageFormat.format("SELECT DISTINCT a.{1}, l.{4}, a.count FROM ("
        + "   SELECT {1}, COUNT(*) AS count"
        + "   FROM {0}"
        + "   TABLESAMPLE ({5} PERCENT)"
        + "   GROUP BY {1}) a"
        + " INNER JOIN {2} l"
        + " ON l.{3} = a.{1}"
        + " ORDER BY a.count DESC",
        occurrenceTable, occurrenceIdCol, lookupTable, lookupIdCol, labelCol, sample.get(MSSQL_TABLESAMPLE));
  }

  @Override
  public String getOccurrenceQuery(String occurrenceTable, String occurrenceIdCol,
      String labelCol, Map<String, Object> sample) {
    return MessageFormat.format("SELECT {1}, {2}, COUNT(*) AS count"
        + " FROM {0}"
        + " TABLESAMPLE ({3} PERCENT)"
        + " GROUP BY {1}, {2}"
        + " ORDER BY count DESC",
        occurrenceTable, occurrenceIdCol, labelCol, sample.get(MSSQL_TABLESAMPLE));
  }

  @Override
  public String getSampleQuery(String occurrenceTable, String occurrenceIdCol, String occurrenceValueCol,
      Map<String, Object> sample) {
    return MessageFormat.format("SELECT {2}, COUNT(*) AS count FROM ("
        + "   SELECT TOP {3} {2}"
        + "   FROM {0}"
        + "   TABLESAMPLE ({4} PERCENT)"
        + "   WHERE {1} = ?"
        + "   AND ABS(CAST(BINARY_CHECKSUM({1}, {2}, NEWID()) AS INT)) % 100 < {5}) a"
        + " GROUP BY {2}"
        + " ORDER BY {2}",
        occurrenceTable, occurrenceIdCol, occurrenceValueCol,
        sample.get(MSSQL_TOPN), sample.get(MSSQL_TABLESAMPLE), sample.get(MSSQL_CHECKSUM));
  }

  @Override
  public Map<String, Object> createDefaultSampleParams() {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put(MSSQL_TABLESAMPLE, 20);
    params.put(MSSQL_TOPN, 1000);
    return params;
  }

  @Override
  public Map<String, Object> createDefaultOccurrenceParams() {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put(MSSQL_TABLESAMPLE, 1);
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

//    Frequency f = p.getFrequency("clinical_event", new Column("event_cd",Types.FLOAT,"FLOAT"), 688867, "result_val", null);
//    System.out.println(f);
//    System.out.println(f.getSumFreq());


  }




}
