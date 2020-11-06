package Interfaces;

import Implementation.Edge;

import java.util.HashSet;
import java.util.List;

public interface IEdge
{
    String GetInNodeNumber();
    String GetOutNodeNumber();
    String GetDirection();
    Double GetMaxSpeed();
    Double GetMinSpeed();
    List<String> GetAircraftType();
    List<Integer> GetDisable(); //Int list because All time values are supposed to be Ints

    /**
     * Returns all flights on the current Edge
     * Flights must be added and removed form this List
     * as the Agent/Scripted way traverses the graph
     * @return
     */
    HashSet<IFlight> GetFlightsOnEdge();

    /*IFlight GetPredecessor(IFlight flight);*/

    //Factory
    static IEdge CreateInstance (String in, String out, String direction, double maxSpeed, double minSpeed, List<String> aircraftTypeList, List<Integer> disableList)
    {
        return new Edge(in, out, direction, maxSpeed, minSpeed, aircraftTypeList, disableList);
    }

}
