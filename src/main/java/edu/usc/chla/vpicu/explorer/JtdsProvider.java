package edu.usc.chla.vpicu.explorer;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.tomcat.jdbc.pool.DataSource;

public class JtdsProvider extends BaseProvider {

  public static final String MSSQL_TABLESAMPLE = "MSSQL TableSample Percent";
  public static final String MSSQL_TOPN = "MSSQL TopN Rows";
  public static final String MSSQL_CHECKSUM = "MSSQL CheckSum Percent";
  
  public JtdsProvider() {
    super();
  }
  
  public JtdsProvider(String host, int port, String user, String pass, String instance, String db, String props) {
    DataSource ds = new DataSource();
    ds.setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
    String url = MessageFormat.format("jdbc:jtds:sqlserver://{0}:{1,number,#}", host, port);
    if (db != null && !db.isEmpty())
      url += "/" + db;
    if (instance != null && !instance.isEmpty())
      url += ";instance=" + instance;
    if (props != null && !props.isEmpty())
      url += ";" + props;
    ds.setUrl(url);
    ds.setUsername(user);
    ds.setPassword(pass);
    setDataSource(ds);
  }

  @Override
  protected String getTableSchema() {
    return "dbo";
  }

  @Override
  public String getOccurrenceQuery(String occurrenceTable, String occurrenceIdCol,
      String lookupTable, String lookupIdCol, String labelCol, Map<String, Object> sample) {
    String sql = MessageFormat.format("SELECT DISTINCT a.{1}, l.{4}, a.count FROM ("
        + "   SELECT {1}, COUNT(*) AS count"
        + "   FROM {0}"
        + "   TABLESAMPLE ({5} PERCENT)"
        + "   GROUP BY {1}) a"
        + " INNER JOIN {2} l"
        + " ON l.{3} = a.{1}"
        + " ORDER BY a.count DESC",
        occurrenceTable, occurrenceIdCol, lookupTable, lookupIdCol, labelCol, sample.get(MSSQL_TABLESAMPLE));
    return sql.replaceAll("\\s+", " ");
  }

  @Override
  public String getOccurrenceQuery(String occurrenceTable, String occurrenceIdCol,
      String labelCol, Map<String, Object> sample) {
    String sql = MessageFormat.format("SELECT {1}, {2}, COUNT(*) AS count"
        + " FROM {0}"
        + " TABLESAMPLE ({3} PERCENT)"
        + " GROUP BY {1}, {2}"
        + " ORDER BY count DESC",
        occurrenceTable, occurrenceIdCol, labelCol, sample.get(MSSQL_TABLESAMPLE));
    return sql.replaceAll("\\s+", " ");
  }

  @Override
  public String getSampleQuery(String occurrenceTable, String occurrenceIdCol, String occurrenceValueCol,
      Map<String, Object> sample) {
    String sql = MessageFormat.format("SELECT {2}, COUNT(*) AS count FROM ("
        + "   SELECT TOP {3} {2}"
        + "   FROM {0}"
        + "   TABLESAMPLE ({4} PERCENT)"
        + "   WHERE {1} = ?"
        + "   AND ABS(CAST(BINARY_CHECKSUM({1}, {2}, NEWID()) AS INT)) % 100 < {5}) a"
        + " GROUP BY {2}"
        + " ORDER BY {2}",
        occurrenceTable, occurrenceIdCol, occurrenceValueCol,
        sample.get(MSSQL_TOPN).toString(),
        sample.get(MSSQL_TABLESAMPLE).toString(),
        sample.get(MSSQL_CHECKSUM).toString());
    return sql.replaceAll("\\s+", " ");
  }

  @Override
  public Map<String, Object> createDefaultSampleParams() {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put(MSSQL_TABLESAMPLE, 1);
    params.put(MSSQL_TOPN, 1000);
    params.put(MSSQL_CHECKSUM, 5);
    return params;
  }

  @Override
  public Map<String, Object> createDefaultOccurrenceParams() {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put(MSSQL_TABLESAMPLE, 1);
    return params;
  }

}
