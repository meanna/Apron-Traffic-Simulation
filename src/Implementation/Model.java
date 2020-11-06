package Implementation;

import Interfaces.*;

import java.util.*;

public class Model implements IModel
{
    public static final int SecondsPerDay = 86400;

    private IAirport airport;
    private ArrayList<IAircraft> aircraftTypes;
    private ArrayList<IFlight> flights;
    private ArrayList<INode> nodeList;
    private ArrayList<IEdge> edgeList;

    private IView view;

    private int seconds;
    private boolean isPaused;

    private Map<String, IAircraft> aircraftTypeLookup;
    private Map<String, List<INode>> nodeNameLookup;
    private Map<Integer, INode> nodeNumberLookup;

    private CrashHandler crashHandler = null;
    private GraphEx graphEx;

    public Model()
    {
        JsonInput jInput = new JsonInput();
        jInput.parseData();

        aircraftTypes = jInput.getAircraftList();
        flights = jInput.getFlightList();
        airport = jInput.getAirport();
        nodeList = jInput.getNodeList();
        edgeList = jInput.getEdgeList();

        crashHandler = new CrashHandler();

        CreateAircraftLookup();
        CreateNodeLookup();

        graphEx = new GraphEx(this);

    }

    /**
     * This method utilizes the Breadth First Search algorithm in order to find the
     * quickest way from one starting node to a goal node. To accomplish that goal,
     * it relies on the in and out values of edges to identify each nodes neighbours.
     *
     * @param startNode node from which the way begins
     * @param endNode   node which is the goal of the search
     * @param flight    current flight
     * @return resultWay A list of Strings representing the numbers of Nodes passed in order
     * @author Eugen Henze
     */
    @Override
    public Deque<INode> findPath(INode startNode, INode endNode, IFlight flight)
    {

        /***Data structures required for searching using BFS algorithm***/
        /*Final result, shortest way from start to finish*/
        Deque<INode> resultWay = new ArrayDeque<>();

        /*Map of nodes that have already been checked*/
        LinkedHashMap<String, String> checkedNodesMap = new LinkedHashMap<>();
        checkedNodesMap.put(String.valueOf(startNode.GetNumber()), String.valueOf(startNode.GetNumber()));

        /*Set of Nodes no longer needing a checkup*/
        HashSet<String> removedNodes = new HashSet<>();

        /*Deque used for keeping track of nodes that need to currently be checked*/
        Deque<String> currentNodesDeque = new ArrayDeque<>();
        currentNodesDeque.add(String.valueOf(startNode.GetNumber()));

        /***Begin of actual search algorithm***/
        //TODO: check for extra requirements, such as plane type and direction
        ArrayList<String> nodesOnCurrentLevel = new ArrayList<>();
        nodesOnCurrentLevel.add(currentNodesDeque.getFirst());

        boolean restricted = false;

        while (!currentNodesDeque.isEmpty())
        {

            nodesOnCurrentLevel.clear();

            /*Adding of all child nodes to nodesOnCurrentLevel ArrayList*/
            for (int i = 0; i < edgeList.size(); i++)
            {

              /*Checking if edge can be traversed by plane type*/
              for(int j = 0; j < edgeList.get(i).GetAircraftType().size(); j++) {
                if(edgeList.get(i).GetAircraftType().get(j).equals(flight.GetAircraftType())) {
                  restricted = true;
                }
              }

                if (edgeList.get(i).GetInNodeNumber().equals(currentNodesDeque.getFirst()))
                {
                    String neighbourID = edgeList.get(i).GetOutNodeNumber();

                    if (!removedNodes.contains(neighbourID) && restricted == false)
                    {
                        nodesOnCurrentLevel.add(neighbourID);
                        currentNodesDeque.add(neighbourID);
                        checkedNodesMap.put(neighbourID, currentNodesDeque.getFirst());
                    }

                }
                restricted = false;
            }

            /*Check if end node is included in current level, stop searching if true*/
            if (nodesOnCurrentLevel.contains(String.valueOf(endNode.GetNumber())))
            {
                currentNodesDeque.clear();
                break;
            }

            /*Steps required if search continues*/
            if (!currentNodesDeque.isEmpty())
            {
                removedNodes.add(currentNodesDeque.getFirst());
                currentNodesDeque.removeFirst();
            }
        }

        /*Construction of searched way*/
        ArrayList<String> reversedLHM = new ArrayList<>(checkedNodesMap.keySet());
        Collections.reverse(reversedLHM);

        String currentNode = String.valueOf(endNode.GetNumber());

        for (String key : reversedLHM)
        {
            if (key.equals(currentNode))
            {
                for (int i = 0; i < nodeList.size(); i++)
                {
                    if (String.valueOf(nodeList.get(i).GetNumber()).equals(key))
                    {
                        resultWay.add(nodeList.get(i));
                        break;
                    }
                }

                currentNode = checkedNodesMap.get(currentNode);
            }
        }

        /*Collections.reverse(resultWay);*/
        ArrayList<INode> resultList = new ArrayList<>(((ArrayDeque<INode>) resultWay).clone());
        Collections.reverse(resultList);
        resultWay.clear();

        for (int i = 0; i < resultList.size(); i++)
        {
            resultWay.add(resultList.get(i));
        }

    /*for(INode result : resultWay) {
      System.out.println(String.valueOf(result.GetNumber()));
    }*/

        return resultWay;
    }

  /**
   * This method relies on the above implemented BFS algorithm in order to determine which
   * edge is currently being traversed by a plane. In order to accomplish this, it utilizes
   * the way calculated by BFS algorithm, using the last visited node as a starting point
   * and InNode of an edge, and the subsequent Node as the OutNode of the same edge.
   *
   * @param currentNode Last reached Node
   * @param flight      current flight
   * @return currentEdge Edge that is currently being traversed
   * @author Eugen Henze
   */
    @Override
    public IEdge findEdgePosition(INode currentNode, IFlight flight) {

        IEdge currentEdge = null;
        currentNode = nodeList.get(88);
        Deque<INode> currentPath = findPath(currentNode, nodeList.get(18), flight);
        IModel model = new Model();

        INode inNode = model.GetNodeByNumber(currentNode.GetNumber());

        INode outNode = null;

        ArrayList<INode> resultList = new ArrayList<>(((ArrayDeque<INode>) currentPath).clone());

        for(int i = 0; i < resultList.size(); i++) {

          if(resultList.get(i).GetNumber() == currentNode.GetNumber()) {
            outNode = GetNodeByNumber(resultList.get(i+1).GetNumber());
          }

        }

        for(IEdge edge : edgeList) {
          if(edge.GetInNodeNumber().equals(String.valueOf(inNode.GetNumber())) &&
                  edge.GetOutNodeNumber().equals(String.valueOf(outNode.GetNumber()))) {

            currentEdge = edge;

          }
        }

        return currentEdge;
    }

  /**
   * This method adds up the distance between each node in the BFS algorithm
   * calculated way, outputting the overall distance in meters.
   *
   * @param way Node Deque of way calculated by BFS algorithm
   * @return meterDistance Overall distance in meters
   * @author Eugen Henze
   */
  public double calculateOverallDistance(Deque<INode> way) {

    double overallDistance = 0;

    INode node1 = way.getFirst();
    Coordinates node1Coordinates = node1.getPosition();

    INode node2;
    Coordinates node2Coordinates;

    while(way.size() > 1) {

      way.removeFirst();
      node2 = way.getFirst();
      node2Coordinates = node2.getPosition();

      overallDistance += calculateNodeDistance(node1Coordinates.getyLongitude(),
              node1Coordinates.getxLatitude(),
              node2Coordinates.getyLongitude(),
              node2Coordinates.getxLatitude());

      node1 = node2;
      node1Coordinates = node2.getPosition();

    }

    return overallDistance;
  }

  /**
   * This method calculates the distance in meters between two nodes.
   *
   * @param y1 latitude of first node
   * @param x1 longitude of first node
   * @param y2 latitude of second node
   * @param x2 longitude of second node
   * @return distance Distance between two nodes in meters
   * @author Eugen Henze
   */
  public double calculateNodeDistance(double y1, double x1, double y2, double x2) {

    /*Earth Radius in meters*/
    double earthRadius = 6371000;

    double distanceLat = Math.toRadians(y2-y1);
    double distanceLong = Math.toRadians(x2-x1);

    double a = Math.sin(distanceLat/2) * Math.sin(distanceLat/2) +
            Math.cos(Math.toRadians(y1)) * Math.cos(Math.toRadians(y2)) *
                    Math.sin(distanceLong/2) * Math.sin(distanceLong/2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

    double resultDistance = (earthRadius * c);

    return resultDistance;

  }


  public void LogCrash(IAircraft aircraft)
    {

    }

    private void CreateAircraftLookup()
    {
        aircraftTypeLookup = new TreeMap<String, IAircraft>();
        for (IAircraft a : aircraftTypes)
            aircraftTypeLookup.put(a.GetType(), a);
    }

    @Override
    public IAircraft GetAircraftBlueprintByType(String type)
    {
        if (!aircraftTypeLookup.containsKey(type))
        {
            System.out.println("This aircraft does not exist! -> " + type);
            return null;
        }
        return aircraftTypeLookup.get(type);
    }

    private void CreateNodeLookup()
    {
        nodeNameLookup = new TreeMap<String, List<INode>>();
        nodeNumberLookup = new TreeMap<Integer, INode>();
        for (INode n : nodeList)
        {
            for (String name : n.GetNames())
            {
                if (!nodeNameLookup.containsKey(name)) nodeNameLookup.put(name, new ArrayList<INode>());
                nodeNameLookup.get(name).add(n);
            }
            nodeNumberLookup.put(n.GetNumber(), n);
        }
    }

    @Override
    public List<INode> GetNodeByName(String name)
    {
        if (!nodeNameLookup.containsKey(name)) return null;
        return nodeNameLookup.get(name);
    }

    @Override
    public INode GetNodeByNumber(int nr)
    {
        if (!nodeNumberLookup.containsKey(nr)) return null;
        return nodeNumberLookup.get(nr);
    }

    @Override
    public CrashHandler getCrashHandler()
    {
        return crashHandler;
    }

    //Update for every Second
    @Override
    public void UpdateTick()
    {
        if (isPaused) return;
        System.out.println("---------------");

        //Call all Update methods ---------------------------------------------------------------------
        airport.Update(this);
        if (view != null) view.Update(this);

        for (IFlight f : flights)
            f.Update(this);

        crashHandler.Update(this);

        //all Update() have been called ---------------------------------------------------------------

        seconds++;
    }

    /**
     * Fast forward
     *
     * @param ticks amount of seconds to fastforward
     */
    @Override
    public void AdvanceTicks(int ticks)
    {
        if (isPaused) return;
        for (int i = 0; i < ticks; i++)
            UpdateTick();
    }


    /*----------------------------
    --------- Get & Set-----------
    -----------------------------*/


    @Override
    public void SetPaused(boolean state) { isPaused = state; }

    @Override
    public boolean GetPaused() { return isPaused; }

    @Override
    public void LoadJSONData(IJSONDataContainer container) { }

    @Override
    public IAirport GetAirport() { return airport; }

    @Override
    public List<IFlight> GetFlights() { return flights; }

    @Override
    public List<IEdge> getEdges() { return edgeList; }

    @Override
    public List<INode> getNodes() { return nodeList; }

    @Override
    public int GetSeconds() { return seconds; }

    @Override
    public GraphEx GetGraphEx() { return graphEx; }

    @Override
    public void SetView(IView view) { }
}
