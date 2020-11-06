package Interfaces;

import Implementation.Coordinates;
import Implementation.Flight;
import Implementation.Schedule;

import java.util.List;

public interface IFlight extends IRequiresModelUpdate
{
    String GetAircraftType();
    List<Schedule> GetSchedule();
    double GetRepetition();
    void SetDelay(double addedDelay);

    double GetTotalDelay();
    IAircraft GetAircraft();
    IEdge GetCurrentEdge();

    /**
     * Retuns the Timelog of all the positions of a flight, For View and Json output
     * @return
     */
    List<Coordinates> GetFlightLog();

    /**
     * Initiates a pushback maneuver of the aircraft of the flight, on which this method is called
     * @param model
     */
    void StartPushback(IModel model);

    //Factory
    static IFlight CreateInstance(String aircraftType, List<Schedule> scheduleList, double repetition, IModel model)
    {
        return new Flight(aircraftType, scheduleList, repetition, model);
    }
}
