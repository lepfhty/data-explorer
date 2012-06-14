package edu.usc.chla.vpicu.explorer.newui;

import java.util.EventObject;

import edu.usc.chla.vpicu.explorer.Histogram;

public class SampleEvent extends EventObject {

  private static final long serialVersionUID = 1L;
  private Histogram hist;
  private String title;
  
  public SampleEvent(Object source, Histogram hist, String title) {
    super(source);
    this.hist = hist;
    this.title = title;
  }
  
  public Histogram getHistogram() {
    return hist;
  }
  
  public String getTitle() {
    return title;
  }
}
