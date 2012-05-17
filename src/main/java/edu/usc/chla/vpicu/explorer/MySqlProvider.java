package edu.usc.chla.vpicu.explorer;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.tomcat.jdbc.pool.PoolConfiguration;
import org.apache.tomcat.jdbc.pool.PoolProperties;

public class MySqlProvider extends BaseProvider {

  public static final String MYSQL_LIMIT = "MySQL Limit";
  public static final String MYSQL_PERCENT = "MySQL Percent";

  @Override
  public String getOccurrenceQuery(String occurrenceTable, String occurrenceIdCol,
      String lookupTable, String lookupIdCol, String labelCol, Map<String, Object> sample) {
    return "";
  }

  @Override
  public String getOccurrenceQuery(String occurrenceTable, String occurrenceIdCol,
      String labelCol, Map<String, Object> sample) {
    return "";
  }

  @Override
  public String getSampleQuery(String occurrenceTable, String occurrenceIdCol, String occurrenceValueCol,
      Map<String, Object> sample) {
	  return "";
  }

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

  public static void main(String[] args) {
    PoolConfiguration conf = new PoolProperties();
    conf.setDriverClassName("com.mysql.jdbc.Driver");
    conf.setUrl("jdbc:mysql://sherylj.la.ad.chla.org:3306/vpsdb");
    conf.setUsername("root");
    conf.setPassword("welcome1");
    DataSource ds = new org.apache.tomcat.jdbc.pool.DataSource(conf);
    MySqlProvider p = new MySqlProvider();
    p.setDataSource(ds);

  }

}
