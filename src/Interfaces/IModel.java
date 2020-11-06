package Interfaces;

import Implementation.CrashHandler;
import Implementation.GraphEx;

import java.util.Deque;
import java.util.List;

public interface IModel
{
    IAirport GetAirport();
    List<IFlight> GetFlights();
    void LoadJSONData(IJSONDataContainer container);
    int GetSeconds();
    void UpdateTick();
    boolean GetPaused();
    void SetPaused(boolean state);
    void SetView(IView view);
    List<INode> getNodes();
    List<IEdge> getEdges();
    CrashHandler getCrashHandler();
    GraphEx GetGraphEx();

    IAircraft GetAircraftBlueprintByType(String type);
    List<INode> GetNodeByName(String name);
    INode GetNodeByNumber(int nr);
    /**
     * Fast forward
     * @param ticks amount of seconds to fastforward
     */
    void AdvanceTicks(int ticks);

    IEdge findEdgePosition(INode currentNode, IFlight flight);
    Deque<INode> findPath(INode startNode, INode endNode, IFlight flight);
}
