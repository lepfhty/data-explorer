package edu.usc.chla.vpicu.explorer.newui;

import java.util.EventListener;

public interface OccurrenceListener extends EventListener {

  public void queryPerformed(OccurrenceEvent e);
  public void tableChanged(OccurrenceEvent e);
  public void idColumnChanged(OccurrenceEvent e);
  public void rowChanged(OccurrenceEvent e);

}
