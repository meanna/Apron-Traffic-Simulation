package Implementation;

import Interfaces.IEdge;
import Interfaces.IFlight;

import java.util.HashSet;
import java.util.List;

public class Edge implements IEdge
{
    private String in;
    private String out;
    private String direction;
    private double maxSpeed;
    private double minSpeed;
    private List<String> aircraftTypeList;
    private List<Integer> disableList;
    private HashSet<IFlight> flightsOnEdge;


    public Node node_in;
    public Node node_out;

    public Edge() { }

    public Edge (String in, String out, String direction, double maxSpeed, double minSpeed, List<String> aircraftTypeList, List<Integer> disableList)
    {
        this.in = in;
        this.out = out;
        this.direction = direction;
        this.maxSpeed = maxSpeed;
        this.minSpeed = minSpeed;
        this.aircraftTypeList = aircraftTypeList;
        this.disableList = disableList;
    }


    @Override
    public HashSet<IFlight> GetFlightsOnEdge()
    {
        return flightsOnEdge;
    }

    /*public IFlight GetPredecessor(IFlight flight)
    {
        if (!flightsOnEdge.contains(flight)) return flightsOnEdge.get(flightsOnEdge.size() - 1);

        for(int i = 0; i < flightsOnEdge.size(); i++)
        {
            IFlight f = flightsOnEdge.get(i);
            if (f != flight) continue;
            if (i == 0) continue;
            return flightsOnEdge.get(i - 1);
        }
        return null;
    }*/

    /*----------------------------
    --------- Get & Set-----------
     -----------------------------*/

    @Override
    public String GetInNodeNumber()
    {
        return in;
    }
    @Override
    public String GetOutNodeNumber()
    {
        return out;
    }
    @Override
    public String GetDirection()
    {
        if(direction == null || direction == "") return "oneway";
        else return direction;
    }
    @Override
    public Double GetMaxSpeed()
    {
        return maxSpeed;
    }
    @Override
    public Double GetMinSpeed()
    {
        return minSpeed;
    }
    @Override
    public List<String> GetAircraftType()
    {
        return aircraftTypeList;
    }
    @Override
    public List<Integer> GetDisable()
    {
        return disableList;
    }

    public void setIn(String in) {
        this.in = in;
    }
    public void setOut(String out) {
        this.out = out;
    }
    public void setDirection(String direction) {
        this.direction = direction;
    }
    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }
    public void setMinSpeed(double minSpeed) {
        this.minSpeed = minSpeed;
    }
    public void setAircraftTypeList(List<String> aircraftTypeList) {
        this.aircraftTypeList = aircraftTypeList;
    }
    public void setDisableList(List<Integer> disableList) {
        this.disableList = disableList;
    }




    public Edge(Node node_in, Node node_out) {
        this.node_in = node_in;
        this.node_out = node_out;

    }


    public Node getNodeIn(){
        return node_in;
    }
    public Node getNodeOut(){
        return node_out;
    }

}
