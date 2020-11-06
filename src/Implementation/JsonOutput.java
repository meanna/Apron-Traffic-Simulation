package Implementation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

/**
 * This method outputs statistical data collected by running
 * the simulation into a .JSON file, which in turn can at a
 * later time be parsed again to call on the same statistical
 * data view.
 *
 * @author Eugen Henze
 */
public class JsonOutput {

  /**
   * This method writes collected statistical data to .JSON file
   *
   * @param fileName name of the file to output data to
   * @throws IOException if an error occurs while writing
   */
  public void outputFile(String fileName) {
    JSONObject output = new JSONObject();

    JSONObject statistics = new JSONObject();

    //TODO: Add actual statistical data as values
    statistics.put("flightCount", 2);
    statistics.put("takeOffCount", 0);
    statistics.put("landingCount", 0);
    statistics.put("delaysMeanValue", 0.0);
    statistics.put("delaysStandardDeviation", 0.0);
    statistics.put("traversedDistanceMeanValue", 0.0);
    statistics.put("traversedDistanceStandardDeviation",0.0);

    JSONArray hotSpotNodes = new JSONArray();
    statistics.put("hotSpotNodes", hotSpotNodes);

    JSONArray hotSpotEdges = new JSONArray();
    statistics.put("hotSpotEdges", hotSpotEdges);

    JSONArray mostDelayedNodes = new JSONArray();
    statistics.put("mostDelayedNodes", mostDelayedNodes);

    JSONArray mostDelayedEdges = new JSONArray();
    statistics.put("mostDelayedEdges", mostDelayedEdges);

    JSONArray undesiredWaitingNodes = new JSONArray();
    statistics.put("undesiredWaitingNodes", undesiredWaitingNodes);

    JSONArray undesiredWaitingEdges = new JSONArray();
    statistics.put("undesiredWaitingEdges", undesiredWaitingEdges);

    JSONArray edgeSpeeds = new JSONArray();
    //TODO: Add elements of edge name and mean speed as values of array
    statistics.put("meanEdgeSpeeds", edgeSpeeds);

    output.put("statistics", statistics);

    try(FileWriter fileWriter = new FileWriter("data/" + fileName + ".json")) {

      fileWriter.write(String.valueOf(output));

    } catch (IOException e) {
      System.out.println("An error while writing the output file has occurred!");
    }

  }

}
