package edu.usc.chla.vpicu.explorer;

import java.sql.Types;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.math3.stat.Frequency;
import org.apache.tomcat.jdbc.pool.PoolConfiguration;
import org.apache.tomcat.jdbc.pool.PoolProperties;

public class MySqlProvider extends BaseProvider {

  public static final String MYSQL_LIMIT = "MySQL Limit";
  public static final String MYSQL_PERCENT = "MySQL Percent";

  @Override
  protected String getDistinctColumnValuesQuery(String tableName, String columnName, Map<String, Object> sample) {
    return "SELECT " + columnName + ", COUNT(" + columnName + ") AS count"
        + " FROM " + tableName
        + " WHERE RAND()*100.0 < " + sample.get(MYSQL_PERCENT)
        + " GROUP BY " + columnName
        + " ORDER BY count DESC";
  }

  @Override
  protected String getFrequencyQuery(String tableName, Column filterColumn, Object filterValue,
      String valueColumn, Map<String, Object> sample) {
    sample = fillDefaultSampleParams(sample);
	  return "SELECT " + valueColumn
	      + " FROM " + tableName
			  + " WHERE " + filterColumn.name + " = ?"
			  + " AND RAND()*100.0 < " + sample.get(MYSQL_PERCENT)
			  + " ORDER BY RAND() LIMIT " + sample.get(MYSQL_LIMIT);
  }

  @Override
  public Map<String, Object> fillDefaultSampleParams(Map<String, Object> given) {
    Map<String, Object> params = super.fillDefaultSampleParams(given);
    if (!params.containsKey(MYSQL_LIMIT))
      params.put(MYSQL_LIMIT, 1000);
    if (!params.containsKey(MYSQL_PERCENT))
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

//    for (String t : p.getTableNames()) {
//      System.out.println(t);
//	    for (Column c : p.getColumns(t))
//	      System.out.println(c);
//    }
//
//    for (Object o : p.getDistinctColumnValues("Events", "VpsID"))
//      System.out.println(o.toString());

    Frequency f = p.getFrequency("Events", new Column("VpsID",Types.INTEGER,"INT"), 5, "ResultVal", null);
    System.out.println(f);
  }

}
