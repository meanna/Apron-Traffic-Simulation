package Implementation;

import Interfaces.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * This class parses through all the files included in the data folder
 * and initializes all the objects with their starting values, later
 * used by the different classes of the program.
 * If a file including statistical data is found, the view presenting
 * statistical data will be called instead of starting the program
 * with values initialized by the contents of .JSON files.
 *
 * @author Eugen Henze
 */
public class JsonInput {

  /*Objects used by other classes*/
  private IAirport parsedAirport;
  private ArrayList<IAircraft> aircraftList = new ArrayList<>();
  private ArrayList<IFlight> flightList = new ArrayList<>();
  private ArrayList<INode> nodeList = new ArrayList<>();
  private ArrayList<IEdge> edgeList = new ArrayList<>();

  private ArrayList<Limit> limitList = new ArrayList<>();
  private ArrayList<Schedule> scheduleList = new ArrayList<>();
  private ArrayList<String> restrictedTypeList = new ArrayList<>();
  private ArrayList<Integer> disabledTimeList = new ArrayList<>();

  private File dataFolder = new File("data/");
  private File[] dataFileArray = dataFolder.listFiles();

  /**
   * Constructor of the JSONInput class
   * Its sole purpose is to immediately parse all data once an object of its class
   * is initialized.
   */
  public JsonInput() {
    parseData();
  }

  /**
   * This method checks all the files included in data folder and parses them with their according
   * methods. If a file containing statistical data is found, it calls the statistical data view instead.
   *
   * @throws FileNotFoundException if there was no file found in the data folder
   */
  public void parseData() {

    FileReader fileReader;

    for(int i = 0; i < dataFileArray.length; i++) {

      try {
        String fileName = "data/" + dataFileArray[i].getName();

        fileReader = new FileReader(fileName);

        JSONTokener tokener = new JSONTokener(fileReader);
        JSONObject data = new JSONObject(tokener);

        if(data.has("statistics")) {
          parseStatistics(fileName);
          //TODO: Call Statistical data view
        }

        if(data.has("nodes") && nodeList.isEmpty()) {
          parseNodes(fileName);
        }

        if(data.has("edges") && edgeList.isEmpty()) {
          parseEdges(fileName);
        }

        if(data.has("airport") && parsedAirport == null) {
          parseAirport(fileName);
        }

        if(data.has("planes") && aircraftList.isEmpty()) {
          parsePlanes(fileName);
        }

        if(data.has("flights") && flightList.isEmpty()) {
          parseFlights(fileName);
        }

      } catch (FileNotFoundException e) {
        System.out.println("File not found!");
      }

    }

  }

  //TODO: Update method with actual values once they exist

  /**
   * Parses statistical data contained in .JSON file
   * calls on View to display said data
   *
   * @param fileName file name of file containing statistical data
   * @throws FileNotFoundException if no file was found
   */
  private void parseStatistics(String fileName) {
    String dataFile = fileName;

    FileReader fileReader;

    try {
      fileReader = new FileReader(dataFile);

      JSONTokener tokener = new JSONTokener(fileReader);
      JSONObject data = new JSONObject(tokener);

      JSONObject statistics = data.getJSONObject("statistics");

      int flighCount = statistics.getInt("flightCount");
      int takeOffCount = statistics.getInt("takeOffCount");
      int landingCount = statistics.getInt("landingCount");
      double delaysMeanValue = statistics.getInt("delaysMeanValue");
      double delaysStandardDeviation = statistics.getInt("delaysStandardDeviation");
      double traversedDistanceMeanValue = statistics.getInt("traversedDistanceMeanValue");
      double traversedDistanceStandardDeviation = statistics.getInt("traversedDistanceStandardDeviation");

      /*Parsing of arrays*/
      /*Hot Spot Nodes*/
      JSONArray hotSpotNodeArray = statistics.getJSONArray("hotSpotNodes");
      ArrayList<Integer> hotSpotNodes = new ArrayList<>();

      for(int i = 0; i < hotSpotNodeArray.length(); i++) {
        hotSpotNodes.add(hotSpotNodeArray.getInt(i));
      }

      /*Hot Spot Edges*/
      JSONArray hotSpotEdgeArray = statistics.getJSONArray("hotSpotEdges");
      ArrayList<Integer> hotSpotEdges = new ArrayList<>();

      for(int i = 0; i < hotSpotEdgeArray.length(); i++) {
        hotSpotEdges.add(hotSpotEdgeArray.getInt(i));
      }

      /*Most Delayed Ndes*/
      JSONArray mostDelayedNodesArray = statistics.getJSONArray("mostDelayedNodes");
      ArrayList<Integer> mostDelayedNodes = new ArrayList<>();

      for(int i = 0; i < mostDelayedNodesArray.length(); i++) {
        mostDelayedNodes.add(mostDelayedNodesArray.getInt(i));
      }

      /*Most Delayed Edges*/
      JSONArray mostDelayedEdgesArray = statistics.getJSONArray("mostDelayedEdges");
      ArrayList<Integer> mostDelayedEdges = new ArrayList<>();

      for(int i = 0; i < mostDelayedEdgesArray.length(); i++) {
        mostDelayedEdges.add(mostDelayedEdgesArray.getInt(i));
      }

      /*Undesired Waiting Nodes*/
      JSONArray undesiredWaitingNodesArray = statistics.getJSONArray("undesiredWaitingNodes");
      ArrayList<Integer> undesiredWaitingNodes = new ArrayList<>();

      for(int i = 0; i < undesiredWaitingNodesArray.length(); i++) {
        undesiredWaitingNodes.add(undesiredWaitingNodesArray.getInt(i));
      }

      /*Undesired Waiting Edges*/
      JSONArray undesiredWaitingEdgesArray = statistics.getJSONArray("undesiredWaitingEdges");
      ArrayList<Integer> undesiredWaitingEdges = new ArrayList<>();

      for(int i = 0; i < undesiredWaitingEdgesArray.length(); i++) {
        undesiredWaitingNodes.add(undesiredWaitingEdgesArray.getInt(i));
      }

      /*Mean Edge Speeds*/
      JSONArray meanEdgeSpeedsArray = statistics.getJSONArray("meanEdgeSpeeds");
      ArrayList<Integer> meanEdgeSpeeds = new ArrayList<>();

      for(int i = 0; i < meanEdgeSpeedsArray.length(); i++) {
        meanEdgeSpeeds.add(meanEdgeSpeedsArray.getInt(i));
      }

    } catch (FileNotFoundException e) {
      System.out.println("Statistical data file not found!");
    }
  }

  /**
   * This method parses all of the nodes contained in a .JSON file
   * it then fills up a list of Nodes, which other classes can use
   * via getter
   *
   * @param fileName file name containing nodes
   * @throws FileNotFoundException if no file was found
   */
  private void parseNodes(String fileName) {
    String dataFile = fileName;

    FileReader fileReader;

    try {
      fileReader = new FileReader(dataFile);

      JSONTokener tokener = new JSONTokener(fileReader);
      JSONObject data = new JSONObject(tokener);

      JSONArray nodes = data.getJSONArray("nodes");

      for(int i = 0; i < nodes.length(); i++) {
        JSONObject node = nodes.getJSONObject(i);
        Node parsedNode;

        int nr = node.getInt("nr");
        double x = node.getDouble("x");
        double y = node.getDouble("y");

        parsedNode = new Node(nr, x, y);

        /*Parsing of names array*/
        if(node.has("names")) {
          List<String> nameList = new ArrayList<String>();
          JSONArray names = node.getJSONArray("names");
          for(int j = 0; j < names.length(); j++) {
            String name = names.getString(j);
            nameList.add(name);

          }
          parsedNode.setNamesList(nameList);
        }

        /*Parsing of restriction array*/
        if(node.has("restrictions")) {
          List<String> restrictionList = new ArrayList<String>();
          JSONArray restrictions = node.getJSONArray("restrictions");
          for(int k = 0; k < restrictions.length(); k++) {
            String restriction = restrictions.getString(k);
            restrictionList.add(restriction);
          }
          parsedNode.setRestrictionsList(restrictionList);
        }

        nodeList.add(parsedNode);

      }

    } catch (FileNotFoundException e) {
      System.out.println("File containing Nodes not found!");
    }
  }

  /**
   * This method parses all of the edges contained in a .JSON file
   * it then fills up a list of Edges, which other classes can use
   * via getter
   *
   * @param fileName file name containing edges
   * @throws FileNotFoundException if no file was found
   */
  private void parseEdges(String fileName) {
    String dataFile = fileName;

    FileReader fileReader;

    try {
      fileReader = new FileReader(dataFile);

      JSONTokener tokener = new JSONTokener(fileReader);
      JSONObject data = new JSONObject(tokener);

      JSONArray edges = data.getJSONArray("edges");

      for(int i = 0; i < edges.length(); i++) {
        JSONObject edge = edges.getJSONObject(i);

        Edge parsedEdge = new Edge();

        //TODO: Maybe replace direction type with enum in the future
        String direction = "oneway";
        if(edge.has("direction")) {
          direction = edge.getString("direction");
          parsedEdge.setDirection(direction);
        }

        if(edge.has("maxspeed")) {
          double maxspeed = edge.getDouble("maxspeed");
          parsedEdge.setMaxSpeed(maxspeed);
        }

        if(edge.has("minspeed")) {
          double minspeed = edge.getDouble("minspeed");
          parsedEdge.setMinSpeed(minspeed);
        }

        String in = edge.getString("in");
        parsedEdge.setIn(in);

        String out = edge.getString("out");
        parsedEdge.setOut(out);

        /*Parsing of restricted types*/
        if(edge.has("types")) {
          JSONArray types = edge.getJSONArray("types");
          for(int j = 0; j < types.length(); j++) {
            String type = types.getString(j);
            restrictedTypeList.add(type);
            parsedEdge.setAircraftTypeList(restrictedTypeList);
          }
        }

        if(edge.has("disable")) {
          JSONArray disable = edge.getJSONArray("disable");
          for(int k = 0; k < disable.length(); k++) {
            int disableTime = disable.getInt(k);
            disabledTimeList.add(disableTime);
            parsedEdge.setDisableList(disabledTimeList);
          }
        }
        edgeList.add(parsedEdge);
        restrictedTypeList.clear();
        disabledTimeList.clear();
      }

    } catch (FileNotFoundException e) {
      System.out.println("File containing Edges not found!");
    }
  }

  /**
   * This method parses all the information about the airport contained
   * in its respective .JSON file.
   *
   * @param fileName file name containing nodes
   * @throws FileNotFoundException if no file was found
   */
  private void parseAirport(String fileName) {
    String dataFile = fileName;

    FileReader fileReader;

    try {

      fileReader = new FileReader(dataFile);

      JSONTokener tokener = new JSONTokener(fileReader);
      JSONObject data = new JSONObject(tokener);

      JSONObject airport = data.getJSONObject("airport");

      String info = airport.getString("info");
      int inbound = airport.getInt("inbound");
      int outbound = airport.getInt("outbound");
      int pushbacklag = airport.getInt("pushbacklag");
      double pushbackspeed = airport.getDouble("pushbackspeed");
      double safetyground = airport.getDouble("safetyground");

      /*Parsing of Safety Wake Turbulence Array, belonging to Airport*/
      JSONArray safetywaketurbulence = airport.getJSONArray("safetywaketurbulence");
      ArrayList<SafetyWakeTurbulence> turbulenceList = new ArrayList<>();

      for(int i = 0; i < safetywaketurbulence.length(); i++) {
        JSONObject turbulenceObject = safetywaketurbulence.getJSONObject(i);
        String predecessor = turbulenceObject.getString("predecessor");
        String follower = turbulenceObject.getString("follower");
        double distance = turbulenceObject.getDouble("distance");

        SafetyWakeTurbulence turbulence = new SafetyWakeTurbulence(predecessor, follower, distance);
        turbulenceList.add(turbulence);

      }

      parsedAirport = IAirport.CreateInstance(info, inbound, outbound, pushbacklag, pushbackspeed, safetyground, turbulenceList);

    } catch (FileNotFoundException e) {
      System.out.println("File containing Airport not found!");
    }
  }

  /**
   * This method parses all of the planes contained in a .JSON file
   * it then fills up a list of planes, which other classes can use
   * via getter
   *
   * @param fileName file name containing planes
   * @throws FileNotFoundException if no file was found
   */
  private void parsePlanes(String fileName) {
    String dataFile = fileName;

    FileReader fileReader;

    try {

      fileReader = new FileReader(dataFile);

      JSONTokener tokener = new JSONTokener(fileReader);
      JSONObject data = new JSONObject(tokener);

      JSONArray planes = data.getJSONArray("planes");

      for(int i = 0; i < planes.length(); i++) {
        JSONObject planeObject = planes.getJSONObject(i);

        String type = planeObject.getString("type");
        double wingspan = planeObject.getDouble("wingspan");
        double length = planeObject.getDouble("length");
        String wakecat = planeObject.getString("wakecat");
        double acceleration = planeObject.getDouble("acceleration");
        double flightspeed = planeObject.getDouble("flightspeed");
        double safety = planeObject.getDouble("safety");

        /*Parsing of plane limits*/
        JSONArray limits = planeObject.getJSONArray("limits");

        for(int j = 0; j < limits.length(); j++) {
          JSONObject limitObject = limits.getJSONObject(j);

          double speed = limitObject.getDouble("speed");
          double degree = limitObject.getDouble("degree");

          Limit parsedLimit = new Limit(speed, degree);
          limitList.add(parsedLimit);
        }

        IAircraft parsedAircraft = IAircraft.CreateInstance(type, wingspan, length, wakecat, flightspeed, acceleration, safety, limitList);
        aircraftList.add(parsedAircraft);
        limitList.clear();

      }

    } catch (FileNotFoundException e) {
      System.out.println("File containing Planes not found!");
    }
  }

  /**
   * This method parses all of the flights contained in a .JSON file
   * it then fills up a list of flights, which other classes can use
   * via getter
   *
   * @param fileName file name containing flights
   * @throws FileNotFoundException if no file was found
   */
  private void parseFlights(String fileName) {
    String dataFile = fileName;

    FileReader fileReader;

    try {

      fileReader = new FileReader(dataFile);

      JSONTokener tokener = new JSONTokener(fileReader);
      JSONObject data = new JSONObject(tokener);

      JSONArray flights = data.getJSONArray("flights");

      for(int i = 0; i < flights.length(); i++) {
        JSONObject flightObject = flights.getJSONObject(i);

        String aircraft = flightObject.getString("aircraft");
        Flight parsedFlight;

        JSONArray schedule = flightObject.getJSONArray("schedule");

        for(int j = 0; j < schedule.length(); j++) {
          JSONObject scheduleObject = schedule.getJSONObject(j);

          String wayPoint = scheduleObject.getString("waypoint");
          Schedule parsedSchedule = new Schedule(wayPoint);

          if(scheduleObject.has("duration")) {
            int duration = scheduleObject.getInt("duration");
            parsedSchedule.setDuration(duration);
          }

          if(scheduleObject.has("time")) {
            int time = scheduleObject.getInt("time");
            parsedSchedule.setTime(time);
          }
          else parsedSchedule.setTime(-1);

          int priority = 1;
          parsedSchedule.setPriority(priority);

          if(scheduleObject.has("priority")) {
            priority = scheduleObject.getInt("priority");
            parsedSchedule.setPriority(priority);
          }

          scheduleList.add(parsedSchedule);
          parsedFlight = new Flight(aircraft, scheduleList);

          if(flightObject.has("repetition")) {
            double repetition = flightObject.getDouble("repetition");
            parsedFlight.setRepetition(repetition);
          }

          flightList.add(parsedFlight);
          scheduleList.clear();
        }

      }

    } catch (FileNotFoundException e) {
      System.out.println("File containing Flights not found!");
    }
  }

  public IAirport getAirport() {
    return parsedAirport;
  }

  public ArrayList<IAircraft> getAircraftList() {
    return aircraftList;
  }

  public ArrayList<IFlight> getFlightList() {
    return flightList;
  }

  public ArrayList<INode> getNodeList() {
    return nodeList;
  }

  public ArrayList<IEdge> getEdgeList() {
    return edgeList;
  }
}
