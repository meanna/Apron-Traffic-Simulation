package Implementation;

import Interfaces.IAirport;
import Interfaces.IModel;

import java.util.ArrayList;
import java.util.List;

public class Airport implements IAirport
{
    private String info;
    private int inbound;
    private int outbound;
    private int pushbackLag;
    private double pushbackSpeed;
    private double safetyGround;
    private ArrayList<SafetyWakeTurbulence> safetyWakeTurbulencesList;

    public Airport(String info, int inbound, int outbound, int pushbackLag, double pushbackSpeed, double safetyGround, ArrayList<SafetyWakeTurbulence> safetyWakeTurbulancesList)
    {
        this.info = info;
        this.inbound = inbound;
        this.outbound = outbound;
        this.pushbackLag = pushbackLag;
        this.pushbackSpeed = pushbackSpeed;
        this.safetyGround = safetyGround;
        this.safetyWakeTurbulencesList = safetyWakeTurbulancesList;
    }

    public Airport(){

    }

    @Override
    public void Update(IModel model)
    {

    }


    /*----------------------------
    --------- Get & Set-----------
     -----------------------------*/


    @Override
    public String GetInfo() { return info; }
    @Override
    public int GetInbound() { return inbound; }
    @Override
    public int GetOutbound() { return outbound; }
    @Override
    public int GetPushbackLag() { return pushbackLag; }
    @Override
    public double GetPushbackSpeed() { return pushbackSpeed; }
    @Override
    public double GetSafetyGround() { return safetyGround; }
    @Override
    public List<SafetyWakeTurbulence> GetSafetyWakeTurbulence() { return safetyWakeTurbulencesList; }
}
