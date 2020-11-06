package Interfaces;

import java.awt.event.ActionEvent;

/**
 * Helper Interface for quickly converting knots-meters
 * Potential for extension with other common program wide operations
 *
 * How to use: Helper.MethodName()
 */

public interface Helper
{
    /**
     * Converts meters into knots
     * @param meters
     * @return knots
     */
    static double MetersToKnots(double meters)
    {
        return meters * 1852;
    }

    /**
     * Converts knots into meters
     * @param knots
     * @return meters
     */
    static double KnotsToMeters(double knots)
    {
        return knots / 1852;
    }

    /**
     * Clamps a value between two other values (Intervall)
     * @param value to be clamped
     * @param min lower boundary
     * @param max upper boundary
     * @return clamped value
     */
    static double Clamp(double value, double min, double max)
    {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Calculates the smallest angle between two angles,
     * sign (Vorzeichen) determines the direction (left/right)
     * @param b1 starting angle
     * @param b2 target angle
     * @return angel in degrees
     */
    static double getSignedAngleDegrees(double b1, double b2)
    {
        double r = (b2 - b1) % 360.0;
        if (r < -180.0)
            r += 360.0;
        if (r >= 180.0)
            r -= 360.0;
        return r;
    }

    /**
     * Calculates the smallest angle between two angles,
     * sign (Vorzeichen) determines the direction (left/right)
     * @param b1 starting angle
     * @param b2 target angle
     * @return angel in radians
     */
    static double getSignedAngleRadians(double b1, double b2)
    {
        double r = (b2 - b1) % (Math.PI * 2);
        if (r < -Math.PI)
            r += Math.PI * 2;
        if (r >= Math.PI)
            r -= Math.PI * 2;
        return r;
    }
}
