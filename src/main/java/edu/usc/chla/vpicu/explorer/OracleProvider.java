package edu.usc.chla.vpicu.explorer;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.tomcat.jdbc.pool.PoolConfiguration;
import org.apache.tomcat.jdbc.pool.PoolProperties;

public class OracleProvider extends BaseProvider {

  public static final String ORACLE_ROWNUM = "Oracle RowNum";
  public static final String ORACLE_SAMPLE = "Oracle Sample";

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
    params.put(ORACLE_SAMPLE, 20);
    params.put(ORACLE_ROWNUM, 1000);
    return params;
  }

  @Override
  public Map<String, Object> createDefaultOccurrenceParams() {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put(ORACLE_SAMPLE, 20);
    return params;
  }

  public static void main(String[] args) {
    PoolConfiguration conf = new PoolProperties();
    conf.setDriverClassName("oracle.jdbc.driver.OracleDriver");
    conf.setUrl("jdbc:oracle:thin:@10.101.21.31:1521:orcl");
    conf.setUsername("ism");
    conf.setPassword("ism4u");
    DataSource ds = new org.apache.tomcat.jdbc.pool.DataSource(conf);
    OracleProvider p = new OracleProvider();
    p.setDataSource(ds);

  }
}
