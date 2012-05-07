package edu.usc.chla.vpicu.explorer;

import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.math3.stat.Frequency;
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
  protected String getDistinctColumnValuesQuery(String tableName, String columnName, Map<String, Object> sample) {
    // FIXME
    return "SELECT " + columnName + ", COUNT(" + columnName + ") AS count"
        + " FROM " + tableName
        + " SAMPLE(" + sample.get(ORACLE_SAMPLE) + ")"
        + " GROUP BY " + columnName
        + " ORDER BY count DESC";
  }

  @Override
  protected String getFrequencyQuery(String tableName, Column filterColumn, Object filterValue, String valueColumn,
      Map<String, Object> sample) {
    sample = fillDefaultSampleParams(sample);
    // FIXME
    return "SELECT /* FIRST_ROWS(" + sample.get(ORACLE_ROWNUM) + ") */ " + valueColumn
        + " FROM " + tableName
        + " SAMPLE(" + sample.get(ORACLE_SAMPLE) + ")"
        + " WHERE " + filterColumn.name + " = ?"
        + " AND ROWNUM < " + sample.get(ORACLE_ROWNUM);
  }

  @Override
  public Map<String, Object> fillDefaultSampleParams(Map<String, Object> given) {
    Map<String, Object> params = super.fillDefaultSampleParams(given);
    if (!params.containsKey(ORACLE_SAMPLE))
      params.put(ORACLE_SAMPLE, 20);
    if (!params.containsKey(ORACLE_ROWNUM))
      params.put(ORACLE_ROWNUM, 1000);
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

//    for (String t : p.getTableNames()) {
//      System.out.println(t);
//      for (Column c : p.getColumns(t))
//        System.out.println(c);
//    }
//
//    for (Object o : p.getDistinctColumnValues("chartevents", "itemid"))
//      System.out.println(o.toString());

    Frequency f = p.getFrequency("chartevents", new Column("itemid",Types.INTEGER,"INT"), 1139, "value2", null);
    System.out.println(f);
    System.out.println(f.getSumFreq());
  }
}
