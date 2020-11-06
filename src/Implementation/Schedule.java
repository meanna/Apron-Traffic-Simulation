package Implementation;

import Interfaces.ISchedule;

public class Schedule implements ISchedule
{
    private String wayPoint;
    private int duration;
    private int time;
    private int priority;
    private double repetition;

    public Schedule(String wayPoint) {
        this.wayPoint = wayPoint;
    }

    public Schedule (String wayPoint, int duration, int time, int priority)
    {
        this.wayPoint = wayPoint;
        this.duration = duration;
        this.time = time;
        this.priority = priority;
    }


    /*----------------------------
    --------- Get & Set-----------
     -----------------------------*/

    @Override
    public String GetWayPoint() { return wayPoint; }
    @Override
    public int GetDuration() { return duration; }
    @Override
    public int GetTime() { return time; }
    @Override
    public int GetPriority() { return priority; }

    public void setWayPoint(String wayPoint) {
        this.wayPoint = wayPoint;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public void setTime(int time) {
        this.time = time;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }
    public double getRepetition() {
        return repetition;
    }
    public void setRepetition(double repetition) {
        this.repetition = repetition;
    }
}
