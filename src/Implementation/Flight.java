package Implementation;

import Interfaces.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;

public class Flight implements IFlight, ICrashListener
{
    private String aircraftType;
    private List<Schedule> scheduleList;
    private double repetition;

    private double totalDelay = 0;

    // Timelog of all the positions of a flight, For View and Json output
    private List<Coordinates> flightLog;

    private IAircraft aircraft = null;
    private IEdge currentEdge; //TODO: Set somewhere Agent/ScriptedWay
    private INode currentNode; //TODO: Set somewhere Agent/ScriptedWay
    private Schedule currentSchedule;

    private boolean repetitionCheck = false; //Allows for repetition after the flight has been completed once; Once set in AdvanceSchedule to true, will not be set to false again
    private double waitingDurationCounter;
    private boolean pushbackLagDone = false;
    private boolean entirePushbackDone = true;

    private Deque<INode> path;

    // Json Constructor
    public Flight (String aircraftType, List<Schedule> scheduleList)
    {
        this.aircraftType = aircraftType;
        this.scheduleList = scheduleList;
        this.flightLog = new ArrayList<Coordinates>(Model.SecondsPerDay);
        currentSchedule = scheduleList.get(0);
    }

    // Simulation Constructor
    public Flight(String aircraftType, List<Schedule> scheduleList, double repetition, IModel model)
    {
        this.aircraftType = aircraftType;
        this.scheduleList = scheduleList;
        this.repetition = repetition;
        this.flightLog = new ArrayList<Coordinates>(Model.SecondsPerDay);
        currentSchedule = scheduleList.get(0);
        FindPath(model);
    }


    @Override
    public void Update(IModel model)
    {
        /*CreateAircraft(model);
        if (aircraft != null) aircraft.Update(model);

        aircraft.Turn(Math.PI * 1.5);

        System.out.println(aircraft.toString());

        /*aircraft.AccelerateTowards(Coordinates.Relative(20, 20), 0, 999999);
        System.out.println(aircraft.toString());
        UpdateSchedules(model);*/

        /*UpdateFlightLog();
        UpdateSchedules(model);
        ExecuteWait();
        ExecutePushback(model);

        model.getCrashHandler().Subscribe(this);*/
    }

    // TODO: --------------------------------
    // if there is No time in schedule object -> Airport inbound time calculation
    // Delay
    // Accident handling
    // TODO: ---------------------------------
    /* POSSIBLE EXTENSION: going through the list backwards, checking: model.seconds >= schedule time (-> past time),
    for guaranteeing the existence of the aircraft, in case of advanced fast forwards.
    ONLY NEEDED if simulation doesn't complete fully before any further handling (View, Statistics etc)*/


    //Called very second/Tick
    //Creates aircraft, checks for repetition
    //
    private void UpdateSchedules(IModel model)
    {
        //Creation and Entry of a new aircraft
        if (aircraft == null)
        {
            int seconds = currentSchedule.GetTime();

            if (seconds < 0) seconds = scheduleList.stream().
                    filter((x) -> x.GetTime() > 0).
                    findFirst().
                    get().
                    GetTime() - model.GetAirport().GetInbound();

            CreateAircraft(model);

            if(currentNode.GetNames().contains("entry"))
            {
                if (! (aircraft.GetVelocity() >= aircraft.GetFlightspeed())) return; //todo: Crash
            }
        }

        //RepetitionCheck
        if(repetitionCheck == true) CheckRepetition(model); //Checks every Second where the flight can be repeated
    }

    // called when current waypoint is done

    /**
     * Has to be called by whatever is controlling the flight: Agent/Script
     * WIll check for conditions on points
     * @param model
     */

    public void AdvanceSchedule(IModel model)
    {
        int index = scheduleList.indexOf(currentSchedule);
        if (index < scheduleList.size() - 1)
        {
            currentSchedule = scheduleList.get(index + 1);

            if (currentSchedule.GetTime() < model.GetSeconds())
            {
                //Add delay to Statistics
                totalDelay += (model.GetSeconds() - currentSchedule.GetTime()) * currentSchedule.GetPriority();
            }
        }
        else // 3.1.5 Removing the aircraft
        {
            if (currentSchedule.GetWayPoint().equals("hangar")) RemoveAircraft();
            else if (currentSchedule.GetWayPoint().equals("exit") && aircraft.GetVelocity() >= aircraft.GetFlightspeed()) RemoveAircraft();
            else return; //todo: Crash

            repetitionCheck = true; //Allows for repetition after the flight has been completed once
        }
    }

    private void CheckRepetition(IModel model)
    {
        int currentTime = model.GetSeconds();
        int maxRepetitionTime = (currentTime / 3600);
        boolean isPast8Oclock = maxRepetitionTime >= 20;
        double repetitionCounter = model.GetSeconds() % 60;

        if (repetition == 0 || isPast8Oclock) return; //No repetition or after 8pm
        else if (repetitionCounter == 0) //Every full second
        {
            double repetitionChance = new Random().nextDouble();
            if (repetitionChance > repetition) return;

            for (int i = 0; i < scheduleList.size(); i++)
            {
                scheduleList.get(i).setTime(currentTime + scheduleList.get(i).GetTime()); //Adds the current time to the time in schedule
            }
        }
    }

    // For repetition: aircraft = null (somewhere); new CreateAircraft()
    private void CreateAircraft(IModel model)
    {
        if (aircraft != null) return;
        aircraft = IAircraft.Copy(model.GetAircraftBlueprintByType(aircraftType));
        aircraft.SetPosition(Coordinates.Relative(0, 0), model);
    }

    public void UpdateCurrentEdge(IModel model)
    {
        /*if (currentEdge != null) currentEdge.GetFlightsOnEdge().remove(this);
        currentEdge = edge;
        currentEdge.GetFlightsOnEdge().add(this);*/
        currentEdge = model.findEdgePosition(currentNode, this);
    }

    public void UpdateCurrentNode(IModel model)
    {

    }

    public void GetCurrentNode()
    {
        currentNode = path.getFirst();
    }

    public void FinishEdge(IModel model)
    {
        if (currentEdge == null) return;
        currentNode = model.GetNodeByNumber(Integer.parseInt(currentEdge.GetOutNodeNumber()));
    }

    private void FindPath(IModel model)
    {
        INode startNode = model.GetNodeByName(scheduleList.get(0).GetWayPoint()).get(0);
        INode endNode = model.GetNodeByName(scheduleList.get(scheduleList.size()).GetWayPoint()).get(0);

        path = model.findPath(startNode, endNode, this);
    }

    private void UpdatePath(IModel model)
    {

    }

    private void RemoveAircraft()
    {
        currentEdge.GetFlightsOnEdge().remove(this);
        aircraft = null;
    }

    @Override
    public void StartPushback(IModel model)
    {
        if (! (currentEdge.GetDirection().equals("pushback"))) return; //todo: Crash?

        IAirport airport = model.GetAirport();

        //Is this needed? or should it be a requirement?
        //SetSpeed(0); //Decelerates the aircraft to 0 velocity for the pushback to start, ideally should velocity already be 0
        if(!(aircraft.GetVelocity() == 0))  return; //just to make sure

        StartWait(airport.GetPushbackLag());
        entirePushbackDone = false;
    }

    //Checks every tick in Update whether it has to execute a pushback or not
    private void ExecutePushback(IModel model)
    {
        if (pushbackLagDone == false && entirePushbackDone == true) return;

        aircraft.SetSpeed(model.GetAirport().GetPushbackSpeed());

        // If the current Node has the same identifier as the "in" Node in the current Edge; reached target position
        if (Integer.parseInt(currentEdge.GetInNodeNumber()) == currentNode.GetNumber())
        {
            // TODO: how to ensure that the velocity is 0 at the node?
            // Missing rotation
            StartWait(model.GetAirport().GetPushbackLag());
            entirePushbackDone = true;
        }
    }

    //Initiates pushbackLag waiting time
    private void StartWait(double duration)
    {
        if (aircraft.GetVelocity() != 0) return; //Can only wait while standing still
        waitingDurationCounter = duration;
        pushbackLagDone = false;
    }

    //Executes the waiting time
    private void ExecuteWait()
    {
        if(waitingDurationCounter == 0) return;
        waitingDurationCounter--;
        if (waitingDurationCounter == 0) pushbackLagDone = true; // Becomes true after the Counter has been reduced to 0;
    }


    //Saves the coordinates of the given aircraft of the flight each tick in Model
    private void UpdateFlightLog() { flightLog.add(aircraft != null ? aircraft.GetPosition() : null); }

    /*----------------------------
    --------- Get & Set-----------
     -----------------------------*/

    @Override
    public String GetAircraftType() { return aircraftType; }
    @Override
    public List<Schedule> GetSchedule() { return scheduleList; }
    @Override
    public double GetRepetition() { return repetition; }
    @Override
    public double GetTotalDelay() { return totalDelay; }
    @Override
    public List<Coordinates> GetFlightLog() { return flightLog; }
    @Override
    public IAircraft GetAircraft() { return aircraft; }
    @Override
    public IEdge GetCurrentEdge() { return currentEdge; }
    @Override
    public void SetDelay(double addedDelay) { totalDelay += addedDelay; }

    public void setAircraftType(String aircraftType) { this.aircraftType = aircraftType; }
    public void setScheduleList(List<Schedule> scheduleList) { this.scheduleList = scheduleList; }
    public void setRepetition(double repetition) { this.repetition = repetition; }

    @Override
    public void CrashOccurred(CrashData data)
    {

    }
}
