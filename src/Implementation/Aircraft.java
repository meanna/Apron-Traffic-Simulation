package Implementation;

import Interfaces.*;

import java.awt.geom.Ellipse2D;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Aircraft implements IAircraft
{
    private String type;
    private double wingspan;
    private double length;
    private String wakecat;
    private double flightSpeed;
    private double acceleration;
    private double safety;
    private List<Limit> limitList; //Gets sorted in Constructor, highest -> lowest value

    private double angle;
    private double velocity;
    private boolean flying = false; //TODO: Still need to be determined somewhere -> Agent has list of nodes & Edges to look for right node/edge identifier: air and/or runway
    private double waitingDurationCounter = 0;
    private boolean waitingForPushback = false;
    private boolean secondPuashbackOver = false;

    private Coordinates position; //NEVER directly set this, ONLY use SetPosition()

    private double maxSpeedForTurn = 0;

    private Ellipse2D collider;


    double x;
    double y;


    public Aircraft(String type, double wingspan, double length, String wakecat, double flightSpeed, double acceleration, double safety, List<Limit> limitList)
    {
        this.type = type;
        this.wingspan = wingspan;
        this.length = length;
        this.wakecat = wakecat;
        this.flightSpeed = flightSpeed;
        this.acceleration = acceleration;
        this.safety = safety;
        this.limitList = limitList;
        maxSpeedForTurn = limitList.stream().map(x -> x.GetSpeed()).max(Comparator.comparing(Double::valueOf)).get();
        limitList.sort(Comparator.comparing(x -> x.GetSpeed()));
        Collections.reverse(limitList);
    }

    @Override
    public void Update(IModel model)
    {
        SetPosition(position.Add(GetFacingDirection().Multiply(velocity)), model);
        //ExecuteWait();
        //StartPushbackAfterWait(model);
        //System.out.println(position);
    }

    @Override
    public void SetPosition(Coordinates position, IModel model)
    {
        if (waitingDurationCounter != 0) return; //If the aircraft still has to wait, one cannot newly set the position

        this.position = new Coordinates(position);

        double mainAxis = model.GetAirport().GetSafetyGround() + this.length + (this.safety * this.velocity);
        double minorAxis = model.GetAirport().GetSafetyGround() + this.wingspan;

        collider = new Ellipse2D.Double(position.getXPos(), position.getYPos(), mainAxis, minorAxis);
    }

    @Override
    public void Turn(Vector target)
    {
        target = target.Normalize();
        double radians = Math.atan2(target.getY(), target.getX());

        Turn(radians);
    }


    /*
    TODO:Needs further work
    Especially the 1% always turning thing
     */
    @Override
    public void Turn(double targetRadians)
    {
        double turnSpeed = Double.MAX_VALUE;
        double turnAngleDelta = 0;

        if (this.velocity > maxSpeedForTurn) { return; } //TODO: Crash

        if (limitList.size() == 1)
        {
            turnSpeed = limitList.get(0).GetDegree();
        }

        for (int i = 0; i < limitList.size() - 1; i++)
        {
            Limit l1 = limitList.get(i);
            Limit l2 = limitList.get(i + 1);

            if (this.velocity <= l1.GetSpeed() && this.velocity > l2.GetSpeed())
            {
                turnSpeed = l1.GetDegree();
            }

            if (i == limitList.size() - 2 && turnSpeed == Double.MAX_VALUE)
            {
                turnSpeed = l2.GetDegree();
            }
        }

        turnAngleDelta = Helper.getSignedAngleRadians(angle, targetRadians);
        turnSpeed = Math.toRadians(turnSpeed);

        if (Math.abs(turnAngleDelta) < turnSpeed)
        {
            angle = targetRadians;
        }
        else
        {
            turnSpeed *= Math.signum(turnAngleDelta);
            angle += turnSpeed;
        }

        angle = (angle + Math.PI * 2) % Math.PI * 2;
    }

    private void StartPushbackAfterWait(IModel model)
    {
        if (waitingDurationCounter != 0) return;
        else if (waitingForPushback == false) return;
        else
        {
            SetSpeed(model.GetAirport().GetPushbackSpeed()); // TODO: aircraft need to go backwards, negative value in SetSpeed()
        }

    }

    private void SwitchWaitingForPuashback()
    {
        if (waitingDurationCounter != 0) return;
        else
        {
            waitingForPushback = false;
        }
    }

    @Override
    public void StartWait(double duration)
    {
        if (velocity != 0) return; //Can only wait while standing still
        else waitingDurationCounter = duration;
    }

    private void ExecuteWait()
    {
        if(waitingDurationCounter == 0) { return; }
        else waitingDurationCounter--;

    }

    @Override
    public void FullThrust()
    {
        velocity += acceleration;
    }

    @Override
    public Vector GetFacingDirection()
    {
        return new Vector(Math.cos(angle), Math.sin(angle));
    }

    @Override
    public void SetSpeed(double targetSpeed)
    {
        if (waitingDurationCounter != 0) return;

        double diff = Math.abs(velocity - targetSpeed);
        if (diff < acceleration) { velocity = targetSpeed; return;} //prevents "jumps"

        if (velocity < targetSpeed) { FullThrust();}
        else if (velocity > targetSpeed) { Brake();}
    }

    @Override
    public void Brake()
    {
        if (Math.abs(velocity) <= acceleration) velocity = 0;
        else if (velocity > 0) velocity -= acceleration;
        else velocity += acceleration; //If you are going backwards you need to "accelerate" to brake
    }

    public void Test()
    {

    }

    @Override
    public void SetAtInNodePosition(INode node, IModel model)
    {
        SetPosition(new Coordinates(node.getPosition()), model);
    }

    @Override
    public String toString()
    {
        return String.format("Position %s Velocity %f Angle %f Type %s Wingspan %f Length %f WakeCat %s FlightSpeed %f Acceleration %f Safety %f", position.toString(), velocity, angle, type, wingspan, length, wakecat, flightSpeed, acceleration, safety);
    }

    /*----------------------------
    --------- Get & Set-----------
    -----------------------------*/

    @Override
    public String GetType()
    {
        return type;
    }
    @Override
    public double GetWingspan()
    {
        return wingspan;
    }
    @Override
    public double GetLength()
    {
        return length;
    }
    @Override
    public String GetWakecat()
    {
        return wakecat;
    }
    @Override
    public double GetFlightspeed()
    {
        return flightSpeed;
    }
    @Override
    public double GetAcceleration()
    {
        return acceleration;
    }
    @Override
    public double GetSafety()
    {
        return safety;
    }
    @Override
    public List<Limit> getLimits()
    {
        return limitList;
    }
    @Override
    public Coordinates GetPosition()
    {
        return position;
    }
    @Override
    public boolean GetFlying() { return flying; }
    @Override
    public Ellipse2D GetCollider() { return collider; }
    @Override
    public double GetVelocity() { return velocity; }
    @Override
    public double GetWaitingDurationCounter() { return waitingDurationCounter; }





    public Aircraft(double flightSpeed, double acceleration, double x, double y){
        this.flightSpeed = flightSpeed;
        this.acceleration = acceleration;
        this.x = x;
        this.y = y;

    }
    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }


    public void SetPosition(double x, double y){
        this.x = x;
        this.y = y;

    }


    public void movePlane( Node out, double speed){

        double tx = this.getX();
        double ty = this.getY();
        double sx = out.getX();
        double sy = out.getY();
        double deltaX = tx - sx;
        double deltaY = ty - sy;
        double angle = Math.atan2( deltaY, deltaX );

        tx -= speed * Math.cos( angle );
        ty -= speed * Math.sin( angle );

        //plane.setLocation(tx,ty);
        this.SetPosition(tx,ty);



    }


}
