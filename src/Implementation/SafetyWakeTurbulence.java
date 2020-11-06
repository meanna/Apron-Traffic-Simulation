package Implementation;

public class SafetyWakeTurbulence
{
    private String predecessor;
    private String follower;
    private double distance;

    public SafetyWakeTurbulence(String predecessor, String follower, double distance)
    {
        this.predecessor = predecessor;
        this.follower = follower;
        this.distance = distance;
    }

    String GetPredecessor(){return predecessor;}
    String GetFollower(){return follower;}

    /**
     *
     * @return distance In knots, refer to Helper for conversion in meters
     */
    double GetDistance(){return distance;}
}
