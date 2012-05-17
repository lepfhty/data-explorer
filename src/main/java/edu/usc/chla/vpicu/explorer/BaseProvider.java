package edu.usc.chla.vpicu.explorer;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

public abstract class BaseProvider extends JdbcTemplate {

  /**
   * An optional table schema.  This value is used as the 2nd argument to the methods
   * {@link DatabaseMetaData#getTables(String, String, String, String[])}
   * and {@link DatabaseMetaData#getColumns(String, String, String, String)}
   * @return an optional table schema
   */
  protected String getTableSchema() {
    return null;
  }

  /**
   * Returns an SQL query to count occurrences of values in a column.  For example:
   * <p><code>
   * SELECT o.occurrenceIdCol, l.labelCol, COUNT(*) AS count
   * FROM occurrenceTable o, lookupTable l
   * WHERE l.lookupIdCol = o.occurrenceIdCol
   * GROUP BY o.occurrenceIdCol, l.labelCol
   * ORDER BY count DESC
   * </code>
   * <p>The query should return 3 columns:
   * <ol>
   * <li><b>ID</b> - {@code String} - a value in this column</li>
   * <li><b>LABEL</b> - {@code String} - a human-readable label associated with the value (1-to-1 with ID)</li>
   * <li><b>COUNT</b> - {@code int} - the number of occurrences of the value</li>
   * </ol>
   * @param occurrenceTable the table containing the occurrences
   * @param occurrenceIdCol the column containing the occurrences
   * @param lookupTable the lookup table to provide human-readable labels
   * @param lookupIdCol the lookup key column to provide human-readable labels
   * @param labelCol the column containing human-readable labels
   * @param sample provider-specific sampling parameters
   * @return an SQL query to count occurrences of values in a column
   */
  public abstract String getOccurrenceQuery(String occurrenceTable, String occurrenceIdCol,
      String lookupTable, String lookupIdCol, String labelCol, Map<String, Object> sample);

  /**
   * Returns an SQL query to count occurrences of values in a column.  For example:
   * <p><code>
   * SELECT occurrenceIdCol, labelCol, COUNT(*) AS count
   * FROM occurrenceTable
   * GROUP BY occurrenceIdCol, labelCol
   * ORDER BY count DESC
   * </code>
   * <p>The query should return 3 columns:
   * <ol>
   * <li><b>ID</b> - {@code String} - a value in this column</li>
   * <li><b>LABEL</b> - {@code String} - a human-readable label associated with the value (1-to-1 with ID)</li>
   * <li><b>COUNT</b> - {@code int} - the number of occurrences of the value</li>
   * </ol>
   * @param occurrenceTable the table containing the occurrences
   * @param occurrenceIdCol the column containing the occurrences
   * @param labelCol the column containing human-readable labels
   * @param sample provider-specific sampling parameters
   * @return an SQL query to count occurrences of values in a column
   */
  public abstract String getOccurrenceQuery(String occurrenceTable, String occurrenceIdCol,
      String labelCol, Map<String, Object> sample);

  /**
   * Returns a parameterized SQL query to count a random sampling of values for a given ID.  For example:
   * <p><code>
   * SELECT occurrenceValueCol, COUNT(*) AS count
   * FROM occurrenceTable
   * WHERE occurrenceIdCol = ?
   * GROUP BY occurrenceValueCol
   * ORDER BY occurrenceValueCol
   * </code>
   * <p>The query should return 2 columns:
   * <ol>
   * <li><b>VALUE</b> - {@code String} - a distinct value
   * <li><b>COUNT</b> - {@code int} - the number of occurrences of the value
   * </ol>
   * @param occurrenceTable the table containing the occurrences
   * @param occurrenceIdCol the column containing the ID
   * @param occurrenceValueCol the column containing the values to be counted
   * @param sample provider-specific sampling parameters
   * @return a parameterized SQL query to count a random sampling of values for a given ID
   */
  public abstract String getSampleQuery(String occurrenceTable, String occurrenceIdCol,
      String occurrenceValueCol, Map<String, Object> sample);

  /**
   * Returns a Map containing default parameters for a sampleQuery.
   * User interface elements should use this method to obtain their own copy of the parameters.
   * @return
   */
  public abstract Map<String, Object> createDefaultSampleParams();

  /**
   * Returns a Map containing default sampling parameters for an occurrenceQuery
   * User interface elements should use this method to obtain their own copy of the parameters.
   * @return a Map containing default parameters for a sampleQuery.
   */
  public abstract Map<String, Object> createDefaultOccurrenceParams();

  /**
   * Returns a list of tables available in the database.
   * @return a list of tables available in the database.
   */
  public List<String> getTables() {
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

  /**
   * Returns a list of columns in the given table.
   * Columns have a name, type (java.sql.Types) and type name.
   * @param tableName
   * @return a list of columns in the given table
   */
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

  /**
   * Obtains a count of occurrences of values in a column.  Each Object array in the List contains 3 values:
   * <ol start="0">
   * <li><b>ID</b> - {@code String} - a value in this column</li>
   * <li><b>LABEL</b> - {@code String} - a human-readable label associated with the value (1-to-1 with ID)</li>
   * <li><b>COUNT</b> - {@code int} - the number of occurrences of the value</li>
   * </ol>
   * @param occurrenceTable the table containing the occurrences
   * @param occurrenceIdCol the column containing the occurrences
   * @param lookupTable the lookup table to provide human-readable labels
   * @param lookupIdCol the lookup key column to provide human-readable labels
   * @param labelCol the column containing human-readable labels
   * @param sample provider-specific sampling parameters
   * @return a List of arrays with ID, LABEL and COUNT for values in a database column
   */
  public List<Object[]> getOccurrences(String occurrenceTable, Column occurrenceIdCol,
      String lookupTable, Column lookupIdCol, Column labelCol, Map<String, Object> sample) {
    String sql = getOccurrenceQuery(occurrenceTable, occurrenceIdCol.name,
        lookupTable, lookupIdCol.name, labelCol.name, sample);
    logger.info(sql);
    return query(sql, new RowMapper<Object[]>() {

      @Override
      public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Object[] { rs.getString(1), rs.getString(2), rs.getInt(3) };
      }

    });
  }

  /**
   * Obtains a count of occurrences of values in a column.  Each Object array in the List contains 3 values:
   * <ol start="0">
   * <li><b>ID</b> - {@code String} - a value in this column</li>
   * <li><b>LABEL</b> - {@code String} - a human-readable label associated with the value (1-to-1 with ID)</li>
   * <li><b>COUNT</b> - {@code int} - the number of occurrences of the value</li>
   * </ol>
   * @param occurrenceTable the table containing the occurrences
   * @param occurrenceIdCol the column containing the occurrences
   * @param labelCol the column containing human-readable labels
   * @param sample provider-specific sampling parameters
   * @return a List of arrays with ID, LABEL and COUNT for values in a database column
   */
  public List<Object[]> getOccurrences(String occurrenceTable, Column occurrenceIdCol,
      Column labelCol, Map<String, Object> sample) {
    String sql = getOccurrenceQuery(occurrenceTable, occurrenceIdCol.name, labelCol.name, sample);
    logger.info(sql);
    return query(sql, new RowMapper<Object[]>() {

      @Override
      public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Object[] { rs.getString(1), rs.getString(2), rs.getInt(3) };
      }

    });
  }

  /**
   * Returns a histogram of values encountered when filtering on a given ID value.
   * @param occurrenceTable the table containing the occurrences
   * @param occurrenceIdCol the column containing the ID
   * @param occurrenceIdValue the ID value on which to filter
   * @param occurrenceValueCol the column containing the values to be counted
   * @param sample provider-specific sampling parameters
   * @return a histogram of values encountered when filtering on a given ID value
   */
  public Histogram getHistogram(final String occurrenceTable, final Column occurrenceIdCol, final Object occurrenceIdValue,
      final Column occurrenceValueCol, final Map<String, Object> sample) {
    return query(new PreparedStatementCreator() {

      @Override
      public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        String sql = getSampleQuery(occurrenceTable, occurrenceIdCol.name, occurrenceValueCol.name, sample);
        logger.info(sql);
        PreparedStatement p = con.prepareStatement(sql);
        p.setObject(1, occurrenceIdValue, occurrenceIdCol.type);
        return p;
      }

    }, new ResultSetExtractor<Histogram>() {

      @Override
      public Histogram extractData(ResultSet rs) throws SQLException, DataAccessException {
        Histogram h = new Histogram();
        while (rs.next())
          h.put(rs.getString(1), rs.getInt(2));
        return h;
      }

    });
  }

  @SuppressWarnings("unused")
  private static void printResultSetRow(ResultSet rs) throws SQLException {
    int n = rs.getMetaData().getColumnCount();
    for (int i = 0; i < n; i++)
      System.out.print(rs.getString(i+1) + "\t");
    System.out.println();
  }

}
