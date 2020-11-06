package Implementation;

import Interfaces.*;

import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;

//TODO: HEAVY WORK IN PROGRESS

/**
 * Contains all the listeners and the method to call them
 * AND: Checks for all Crashes
 */
public class CrashHandler implements IRequiresModelUpdate
{

    /**
     * List can contain EVERYTHING that implements ICrashListener
     */
    private List<ICrashListener> listeners;

    /**
     * When a new CrashHandler is made, an ArrayList of ICrashListeners gets made
     */
    public CrashHandler()
    {
        listeners = new ArrayList<ICrashListener>();
    }

    /**
     * Add an object that implements ICrashListener to the listener List in the CrashHandler
     * @param listener
     */
    public void Subscribe(ICrashListener listener)
    {
        listeners.add(listener);
    }

    /**
     * Remove an object that implements ICrashListener to the listener List in the CrashHandler
     * @param listener
     */
    public void Unsubscribe(ICrashListener listener)
    {
        listeners.remove(listener);
    }

    /**
     * Checks for crashes every second, called in Model
     * @param model
     */
    @Override
    public void Update(IModel model)
    {
        //Check for Crashes
        CheckRunwayOverrun(model);
        CheckCollision(model);
        CheckWakeTurbulence(model);
        CheckRunwayRestriction(model);
    }

    /**
     * Calls all CrashOccurred (unique functionality) methods for all listeners in the List
     * Basically, calls everything that happens when a crash happens (View window, Statistics log, etc..)
     * @param data
     */
    private void OnCrash(CrashData data)
    {
        for (ICrashListener listener : listeners)
            listener.CrashOccurred(data);
    }

    /**
     * Checks for the right aircraft Type and Velocity on the Edge (where it currently is)
     *
     * If conditions are not met, calls OnCrash(RunwayOverrun)
     * @param model
     */
    private void CheckRunwayOverrun(IModel model)
    {
        //3.2.1
        /*List<IFlight> flights = model.GetFlights();
        IEdge edge = flight.GetAgent().GetMomentaryEdge(); TODO: Needs the Edge on which the aircraft is at that moment

        for (IFlight flight : flights)
        {
            if (flight.GetAircraft().GetVelocity() < edge.GetMinSpeed() || flight.GetAircraft().GetVelocity() > edge.GetMaxSpeed())
            {
                CrashData crashData = new CrashData(CrashData.CrashType.RunwayOverrun);
                OnCrash(crashData);
            }


            if (edge.GetAircraftType().contains(flight.GetAircraftType()))
            {
                CrashData crashData = new CrashData(CrashData.CrashType.RunwayOverrun);
                OnCrash(crashData);
            }
            //else if ( MaxSpeedforTurn condition (in Aircraft) AND possibly others( SpeedChangePoint??))
        }
        */

    }

    /**
     * Checks all flights against each other for collision.
     *
     * Collision via an Ellipe2D "collider".
     * Checks if the two colliders intersect: if the area where the two intersect is NOT empty -> Collision
     *
     * Calls OnCrashed with the correct CrashType enum.
     * @param model
     */
    private void CheckCollision(IModel model)
    {
        //3.2.2
        List<IFlight> flights = model.GetFlights();

        for (IFlight flight1 : flights)
        {
            for (IFlight flight2 : flights)
            {
                if (flight1 == flight2) continue;
                Area a = new Area(flight1.GetAircraft().GetCollider());
                Area b = new Area(flight2.GetAircraft().GetCollider());
                a.intersect(b);
                if (!a.isEmpty())
                {
                    CrashData crashData = new CrashData(CrashData.CrashType.Collision);
                    OnCrash(crashData);
                }
            }
        }

    }

    /**
     * First, looks for the right DISTANCE in the safetyWakeTurbulence Array depending on the wakecat of the two planes
     * Then calculates the distance BETWEEN the two aircrafts
     * Then checks for violation of the first determined DISTANCE
     * and subsequently calls OnCrash(WakeTurbulence)
     * @param model
     */
    private void CheckWakeTurbulence(IModel model)
    {
        //3.2.3
        /*
        Needs Reference (from Agent probably) to the Predecessor aircraft, saved in the Nodes?

        Missing: Means of saving the wakeTurbulence after the predecessor aircraft has left the last knot
        and then calculating the distance (that become shorter over time)
         */
        IAirport airport = model.GetAirport();
        List<IFlight> flights = model.GetFlights();
        double distance;

        List<SafetyWakeTurbulence> swt = airport.GetSafetyWakeTurbulence();

        for (IFlight flight : flights)
        {
            if (flight.GetAircraft() == null) { continue; }
            /*for (SafetyWakeTurbulence s : swt)
            {
                //Looks for the right combination of swt -> wakecats for both planes to determine the distance
                if (s.GetPredecessor() == PredecessorAircraft && s.GetFollower() == flight.GetAircraft().GetWakecat())
                {
                    distance = s.GetDistance();
                    break;
                }
            }*/
        }

        //Checks the newly computed distance against the dist between PreAircraft & FollowerAircraft, then Calls OnCrash
        //TODO: calculation of distance between preAircraft & FollowerAircraft
        /*
        double distanceBetweenAircrafts = distance between PredecessorAircraft & flight.GetAircraft();

        if (distance < distanceBetweenAircrafts)
        {
            CrashData crashData = new CrashData(CrashData.CrashType.WakeTurbulence);
            OnCrash(crashData);
        }
         */
    }

    /**
     *
     * @param model
     */
    private void CheckRunwayRestriction(IModel model)
    {
        //3.2.4

        List<IEdge> edgeList = model.getEdges();
        GraphEx graphEx = model.GetGraphEx();

        for (IEdge edge : edgeList)
        {
            INode in = model.GetNodeByNumber(Integer.parseInt(edge.GetInNodeNumber()));
            INode out = model.GetNodeByNumber(Integer.parseInt(edge.GetOutNodeNumber()));

            if (edge.GetFlightsOnEdge().size() == 0) continue;

            for (String inRes : in.GetRestrictions())
            {
                for (INode inResNode : model.GetNodeByName(inRes))
                {
                    IEdge e = graphEx.EdgeBetweenNodes(in, inResNode);

                    if (e == null || e.GetFlightsOnEdge().size() <= 0) continue;
                    if (e == edge && e.GetFlightsOnEdge().size() <= 1) continue;

                    CrashData crashData = new CrashData(CrashData.CrashType.RunwayRestriction);
                    OnCrash(crashData);
                }
            }

            for (String outRes : out.GetRestrictions())
            {
                for (INode outResNode : model.GetNodeByName(outRes))
                {
                    IEdge e = graphEx.EdgeBetweenNodes(out, outResNode);

                    if (e == null || e.GetFlightsOnEdge().size() <= 0) continue;
                    if (e == edge && e.GetFlightsOnEdge().size() <= 1) continue;

                    CrashData crashData = new CrashData(CrashData.CrashType.RunwayRestriction);
                    OnCrash(crashData);
                }
            }
        }
    }
}
