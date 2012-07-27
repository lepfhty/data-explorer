package edu.usc.chla.vpicu.explorer.newui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.concurrent.ExecutionException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingWorker;

public abstract class ProgressButton<T> extends JButton {

  private static final long serialVersionUID = 1L;

  private static final ImageIcon ICON = new ImageIcon(ProgressButton.class.getClassLoader().getResource("ajax-loader.gif"));

  private String text;
  private Font font;

  public ProgressButton(String name) {
    super(name);
  }

  public void setInProgress() {
    text = getText();
    font = getFont();
    setIcon(ICON);
    setText(null);
  }

  public void setDone() {
    setIcon(null);
    setText(text);
    setFont(font);
  }

  @Override
  protected void fireActionPerformed(ActionEvent event) {
    super.fireActionPerformed(event);
    new Worker().execute();
  }

  protected abstract T doInBackground() throws Exception;
  protected abstract void done(T result);

  private class Worker extends SwingWorker<T,Object> {

    @Override
    protected T doInBackground() throws Exception {
      setInProgress();
      return ProgressButton.this.doInBackground();
    }

    @Override
    protected void done() {
      setDone();
      try {
        ProgressButton.this.done(get());
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (ExecutionException e) {
        e.printStackTrace();
      }
    }

  }
}
