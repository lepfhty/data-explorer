package edu.usc.chla.vpicu.explorer.newui;

import java.util.EventListener;
import java.util.List;

public interface OccurrenceListener extends EventListener {

  void occurrenceQueryPerformed(List<Object[]> results);

}
