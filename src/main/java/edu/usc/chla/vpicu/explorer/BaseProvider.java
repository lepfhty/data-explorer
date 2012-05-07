package edu.usc.chla.vpicu.explorer;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.stat.Frequency;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

public abstract class BaseProvider extends JdbcTemplate {

  protected String getTableSchema() {
    return null;
  }

  protected abstract String getDistinctColumnValuesQuery(String tableName, String columnName, Map<String, Object> sample);

  protected abstract String getFrequencyQuery(String tableName, Column filterColumn, Object filterValue,
      String valueColumn, Map<String, Object> sample);

  public Map<String, Object> fillDefaultSampleParams(Map<String, Object> given) {
    return given == null ? new HashMap<String, Object>() : given;
  }

  public List<String> getTableNames() {
    List<String> tables = new ArrayList<String>();
    try {
      DatabaseMetaData met = getDataSource().getConnection().getMetaData();
      ResultSet rs = met.getTables(null, getTableSchema(), "%", null);
      while (rs.next()) {
        tables.add(rs.getString(3));
      }
      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return tables;
  }

  public List<Column> getColumns(String tableName) {
    List<Column> columns = new ArrayList<Column>();
    try {
      DatabaseMetaData met = getDataSource().getConnection().getMetaData();
      ResultSet rs = met.getColumns(null, getTableSchema(), tableName, "%");
      while (rs.next())
        columns.add(new Column(rs.getString(4), rs.getInt(5), rs.getString(6)));
      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return columns;
  }

  public List<Object[]> getDistinctColumnValues(String tableName, String columnName, Map<String, Object> sample) {
    String sql = getDistinctColumnValuesQuery(tableName, columnName, sample);
    logger.info(sql);
    return query(sql, new RowMapper<Object[]>() {

      @Override
      public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Object[] { rs.getString(1), rs.getString(2), rs.getInt(3) };
      }

    });
  }

  public Frequency getFrequency(final String tableName, final Column filterColumn, final Object filterValue,
      final String valueColumn, final Map<String, Object> sample) {
    final Frequency freq = new Frequency();
    query(new PreparedStatementCreator() {

      @Override
      public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        String sql = getFrequencyQuery(tableName, filterColumn, filterValue, valueColumn, sample);
        logger.info(sql);
        PreparedStatement p = con.prepareStatement(sql);
        p.setObject(1, filterValue, filterColumn.type);
        return p;
      }

    }, new RowCallbackHandler() {

      @Override
      public void processRow(ResultSet rs) throws SQLException {
        Object o = rs.getObject(1);
        if (o == null)
          return;
        if (o instanceof Number) {
          safeAddValue(freq, ((Number)o).doubleValue());
        } else {
          Double d = null;
          try {
            d = Double.valueOf(o.toString());
          } catch (NumberFormatException e) {
            safeAddValue(freq, o.toString());
            return;
          }
          safeAddValue(freq, d);
        }
      }
    });
    return freq;
  }

  private static boolean safeAddValue(Frequency freq, Comparable<?> value) {
    try {
      freq.addValue(value);
    } catch (MathIllegalArgumentException e) {
      return false;
    }
    return true;
  }

  @SuppressWarnings("unused")
  private static void printResultSetRow(ResultSet rs) throws SQLException {
    int n = rs.getMetaData().getColumnCount();
    for (int i = 0; i < n; i++)
      System.out.print(rs.getString(i+1) + "\t");
    System.out.println();
  }

}
