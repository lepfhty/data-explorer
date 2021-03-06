package edu.usc.chla.vpicu.explorer;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.tomcat.jdbc.pool.DataSource;

public class MySqlProvider extends BaseProvider {

  public static final String MYSQL_LIMIT = "MySQL Limit";
  public static final String MYSQL_PERCENT = "MySQL Percent";
  
  public MySqlProvider() {
    super();
  }
  
  public MySqlProvider(String host, int port, String user, String pass, String db, String props) {
    DataSource ds = new DataSource();
    ds.setDriverClassName("com.mysql.jdbc.Driver");
    String url = MessageFormat.format("jdbc:mysql://{0}:{1,number,#}", host, port);
    if (db != null && !db.isEmpty())
      url += "/" + db;
    if (props != null && !props.isEmpty())
      url += "?" + props;
    ds.setUrl(url);
    ds.setUsername(user);
    ds.setPassword(pass);
    setDataSource(ds);
  }

  @Override
  public String getOccurrenceQuery(String occurrenceTable, String occurrenceIdCol,
      String lookupTable, String lookupIdCol, String labelCol, Map<String, Object> sample) {
    String sql = MessageFormat.format("SELECT DISTINCT a.{1}, l.{4}, a.count FROM ("
        + "   SELECT {1}, COUNT(*) AS count"
        + "   FROM {0}"
        + "   WHERE RAND() < ({5}/100.0)"
        + "   GROUP BY {1}) a"
        + " INNER JOIN {2} l"
        + " ON l.{3} = a.{1}"
        + " ORDER BY a.count DESC",
        occurrenceTable, occurrenceIdCol, lookupTable, lookupIdCol, labelCol, sample.get(MYSQL_PERCENT));
    return sql.replaceAll("\\s+", " ");
  }

  @Override
  public String getOccurrenceQuery(String occurrenceTable, String occurrenceIdCol,
      String labelCol, Map<String, Object> sample) {
    String sql = MessageFormat.format("SELECT {1}, {2}, COUNT(*) AS count"
        + " FROM {0}"
        + " WHERE RAND() < ({3}/100.0)"
        + " GROUP BY {1}, {2}"
        + " ORDER BY count DESC",
        occurrenceTable, occurrenceIdCol, labelCol, sample.get(MYSQL_PERCENT));
    return sql.replaceAll("\\s+", " ");
  }

  @Override
  public String getSampleQuery(String occurrenceTable, String occurrenceIdCol, String occurrenceValueCol,
      Map<String, Object> sample) {
    String sql = MessageFormat.format("SELECT {2}, COUNT(*) AS count FROM ("
        + "   SELECT {2}"
        + "   FROM {0}"
        + "   WHERE {1} = ?"
        + "   AND RAND() < ({4}/100.0)"
        + "   LIMIT {5}) a"
        + " GROUP BY {2}"
        + " ORDER BY {2}",
        occurrenceTable, occurrenceIdCol, occurrenceValueCol,
        sample.get(MYSQL_PERCENT).toString(),
        sample.get(MYSQL_LIMIT).toString());
    return sql.replaceAll("\\s+", " ");  }

  @Override
  public Map<String, Object> createDefaultSampleParams() {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put(MYSQL_LIMIT, 1000);
    params.put(MYSQL_PERCENT, 20);
    return params;
  }

  @Override
  public Map<String, Object> createDefaultOccurrenceParams() {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put(MYSQL_PERCENT, 20);
    return params;
  }

}
