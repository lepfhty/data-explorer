package edu.usc.chla.vpicu.explorer.ui;

import java.awt.Dimension;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.math3.stat.Frequency;

import edu.usc.chla.vpicu.explorer.BaseProvider;

public class DistinctValuesPanel extends JPanel implements FilterTextListener, SampleListener {

  private static final long serialVersionUID = 1L;

  private final JEditorPane editor = new JEditorPane();
  private List<Object[]> counts;

  private String tableName;
  private String columnName;
  private final BaseProvider provider;
  private Map<String, Object> params;

  public DistinctValuesPanel(BaseProvider prov) {
    provider = prov;
    editor.setEditable(false);
    editor.setContentType("text/html");
    add(new JScrollPane(editor));
    editor.setPreferredSize(new Dimension(400,800));
  }

  public void filter(String pattern) {
    if (counts == null)
      return;
    Pattern p = pattern == null ? null : Pattern.compile(".*" + pattern + ".*");
    StringBuilder b = new StringBuilder("<table>\n");
    for (Object[] row : counts) {
      if (p == null || p.matcher((String)row[1]).matches())
	      b.append("<tr><td>").append(row[1]).append("</td><td>").append(row[2]).append("</td></tr>\n");
    }
    b.append("</table>");
    editor.setText(b.toString());
  }

  @Override
  public void filterTextChanged(String newText) {
    if (counts == null && tableName != null && columnName != null) {
      counts = provider.getDistinctColumnValues(tableName, columnName, provider.fillDefaultSampleParams(null));
    }
	  filter(newText);
  }

  @Override
  public void tableNameChanged(String newTableName) {
    if (!newTableName.equals(tableName)) {
	    counts = null;
	    tableName = newTableName;
    }
  }

  @Override
  public void columnNameChanged(String newColumnName) {
    if (!newColumnName.equals(columnName)) {
	    counts = null;
	    columnName = newColumnName;
    }
  }

  @Override
  public void samplePerformed(Frequency f) {
  }

}
