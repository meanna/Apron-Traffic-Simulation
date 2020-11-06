package Interfaces;//package behavior;

import Implementation.Agent;

public interface IBehaveNode {

  

  /**
   *  Neuen Zustand eines Verhaltensbaums starten.
   *  @param now Aktueller Zeitpunkt in Sekunden
   *  @return Der erzeugte Verhaltensbaumzustand
   */
  public IBehaveState init(int now, Agent agent);



}
