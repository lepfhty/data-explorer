package edu.usc.chla.vpicu.explorer;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.tomcat.jdbc.pool.DataSource;

public class OracleProvider extends BaseProvider {

  public static final String ORACLE_ROWNUM = "Oracle RowNum";
  public static final String ORACLE_RANDOM = "Oracle Random Percent";
  public static final String ORACLE_SAMPLE = "Oracle Sample Percent";
  
  public OracleProvider() {
    super();
  }
  
  public OracleProvider(String host, int port, String user, String pass, String sidsvc, boolean isService) {
    DataSource ds = new DataSource();
    ds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
    ds.setUrl(MessageFormat.format("jdbc:oracle:thin:@{0}:{1,number,#}{2}{3}",
        host, port, isService ? "/" : ":", sidsvc));
    ds.setUsername(user);
    ds.setPassword(pass);
    setDataSource(ds);
  }

  @Override
  protected String getTableSchema() {
    try {
	    return getDataSource().getConnection().getMetaData().getUserName();
    } catch (SQLException e) {
      return null;
    }
  }

  @Override
  public String getOccurrenceQuery(String occurrenceTable, String occurrenceIdCol,
      String lookupTable, String lookupIdCol, String labelCol, Map<String, Object> sample) {
    String sql = MessageFormat.format("SELECT DISTINCT a.{1}, l.{4}, a.count FROM ("
        + "   SELECT {1}, COUNT(*) AS count"
        + "   FROM {0}"
        + "   SAMPLE({5})"
        + "   GROUP BY {1}) a"
        + " INNER JOIN {2} l"
        + " ON l.{3} = a.{1}"
        + " ORDER BY a.count DESC",
        occurrenceTable, occurrenceIdCol, lookupTable, lookupIdCol, labelCol, sample.get(ORACLE_SAMPLE));
    return sql.replaceAll("\\s+", " ");
  }

  @Override
  public String getOccurrenceQuery(String occurrenceTable, String occurrenceIdCol,
      String labelCol, Map<String, Object> sample) {
    String sql = MessageFormat.format("SELECT {1}, {2}, COUNT(*) AS count"
        + " FROM {0}"
        + " SAMPLE ({3})"
        + " GROUP BY {1}, {2}"
        + " ORDER BY count DESC",
        occurrenceTable, occurrenceIdCol, labelCol, sample.get(ORACLE_SAMPLE));
    return sql.replaceAll("\\s+", " ");
  }

  @Override
  public String getSampleQuery(String occurrenceTable, String occurrenceIdCol, String occurrenceValueCol,
      Map<String, Object> sample) {
    String sql = MessageFormat.format("SELECT {2}, COUNT(*) AS count FROM ("
        + "   SELECT {2}"
        + "   FROM {0}"
        + "   SAMPLE ({3})"
        + "   WHERE {1} = ?"
        + "   AND dbms_random.value(0,100) < {4}"
        + "   AND rownum < {5}) a"
        + " GROUP BY {2}"
        + " ORDER BY {2}",
        occurrenceTable, occurrenceIdCol, occurrenceValueCol,
        sample.get(ORACLE_SAMPLE).toString(),
        sample.get(ORACLE_RANDOM).toString(),
        sample.get(ORACLE_ROWNUM).toString());
    return sql.replaceAll("\\s+", " ");
  }

  @Override
  public Map<String, Object> createDefaultSampleParams() {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put(ORACLE_SAMPLE, 5);
    params.put(ORACLE_ROWNUM, 1000);
    params.put(ORACLE_RANDOM, 100);
    return params;
  }

  @Override
  public Map<String, Object> createDefaultOccurrenceParams() {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put(ORACLE_SAMPLE, 5);
    return params;
  }
  
}
