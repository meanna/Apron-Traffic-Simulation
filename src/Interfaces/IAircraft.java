package Interfaces;

import Implementation.*;
import javafx.scene.shape.Ellipse;

import java.awt.geom.Ellipse2D;
import java.util.List;

public interface IAircraft extends IRequiresModelUpdate
{
    String GetType();
    double GetWingspan();
    double GetLength();
    String GetWakecat();
    double GetFlightspeed();
    double GetAcceleration();
    double GetSafety();
    List<Limit> getLimits();
    boolean GetFlying();
    Ellipse2D GetCollider();
    double GetVelocity();
    Coordinates GetPosition();
    double GetWaitingDurationCounter();

    Vector GetFacingDirection();
    void SetAtInNodePosition(INode node, IModel model);
    void SetPosition(Coordinates position, IModel model);

    /**
     * Raises the current velocity by the aircraft's acceleration
     * without constrains
     */
    void FullThrust();

    /**
     * Rises the current velocity to the target velocity
     * @param targetSpeed
     */
    void SetSpeed(double targetSpeed);

    /**
     * Decelerates the aircraft
     * Can be used for both forwards and backwards braking
     */
    void Brake();

    /**
     *Turns the aircraft to a target radians
     * @param targetRadians
     */
    void Turn(double targetRadians);

    /**
     * Turns the aircraft to a target Vector
     * @param targetDirection
     */
    void Turn(Vector targetDirection);

    /**
     * Makes the aircraft wait for the duration
     * The velocity, prior to the wait, has to be zero
     * The aircraft cannot SetSpeed() and SetPosition() during the wait (can be called bu will return;)
     * the duration ticks down in Update()
     * @param duration the time the aircraft has to wait
     */
    void StartWait(double duration);


    //Factory
    static IAircraft CreateInstance(String type, double wingspan, double length, String wakecat,
                                    double flightspeed, double acceleration, double safety, List<Limit> limitList)
    {
        return new Aircraft(type, wingspan, length, wakecat, flightspeed, acceleration, safety, limitList);
    }

    // Copy Factory
    static IAircraft Copy(IAircraft copy)
    {
        return new Aircraft(copy.GetType(), copy.GetWingspan(), copy.GetLength(), copy.GetWakecat(),
                copy.GetFlightspeed(), copy.GetAcceleration(), copy.GetSafety(), copy.getLimits());
    }

}
