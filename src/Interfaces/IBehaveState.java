package Interfaces;//package behavior;

import Implementation.StateKind;

public interface IBehaveState {

  /**
   *  In welcher Sorte von Ausfuehrungszustands befindet sich dieser
   *  BehaveState?
   *  @return Art des Ausfuehrungszustands
   */
  public StateKind kind();
  /**
   *  Verhaltensbaum wird fortgesetzt bis zu dem angegebenen Zeitpunkt.
   *  @param now Neuer Zeitpunkt in Sekunden
   *  @return Neuer Ausfuehrungszustand. Kann this sein, kann aber auch
   *          ein neues Objekt, vielleicht sogar aus einer anderen
   *          Klasse sein
   */
  public IBehaveState tick(int now);

}

