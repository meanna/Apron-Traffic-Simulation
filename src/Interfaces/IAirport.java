package Interfaces;

import Implementation.Airport;
import Implementation.SafetyWakeTurbulence;

import java.util.ArrayList;
import java.util.List;

public interface IAirport extends IRequiresModelUpdate
{
    String GetInfo();
    int GetInbound();
    int GetOutbound();
    int GetPushbackLag();
    double GetPushbackSpeed();
    double GetSafetyGround();
    List<SafetyWakeTurbulence> GetSafetyWakeTurbulence();

    //Factory
    static IAirport CreateInstance(String info, int inbound, int outbound, int pushbackLag, double pushbackSpeed, double safetyGround, ArrayList<SafetyWakeTurbulence> safetyWakeTurbulancesList)
    {
        //Call Constructor
        return new Airport(info, inbound, outbound, pushbackLag, pushbackSpeed, safetyGround, safetyWakeTurbulancesList);

    }
}
