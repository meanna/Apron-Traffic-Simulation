package Implementation;

/**
 * Stores the Data of Crashes
 */
public class CrashData
{
    /*
    Enum crash types -> What crash occurred

    as well as data concerning the crash link: what kind(s) of aircrafts were involved, where it happened etc.

    -> This data (especially the planned enum) can then be used in ICrashListener CrashOccurred(crashdata) to determine what the implementing class does upon what crash
    ex. write in view what kind of crash happened
     */

    /**
     * Differentiates the type of Crash that occurred
     * Can be used with a switch/case in the Class implementing ICrashListener
     */

    public enum CrashType { RunwayOverrun, Collision, WakeTurbulence, RunwayRestriction }
    private CrashType crashType;

    public CrashData(CrashType crashType)
    {
        this.crashType = crashType;
    }

    public CrashType GetCrashType(){ return crashType;}

}
