package edu.usc.chla.vpicu.explorer.ui;

import java.util.EventListener;

import org.apache.commons.math3.stat.Frequency;

public interface SampleListener extends EventListener {

  public void samplePerformed(Frequency f);

}
