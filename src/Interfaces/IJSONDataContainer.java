package Interfaces;

import java.util.List;

public interface IJSONDataContainer
{
    IAirport GetAirport();
    List<IAircraft> GetAllAircraft();
    List<IFlight> GetFlights();
}
