package Implementation;

import Interfaces.Helper;

/**
 * Vector Class
 *
 * For strong Positional Values as Vectors instead of two separate x & y Values
 */

public class Vector
{
    private double x;
    private double y;

    //Creates new Vector from two separate x & y Values
    public Vector(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector Add(double x, double y)
    {
        return new Vector (this.x + x, this.y + y);
    }

    public Vector Add(Vector v)
    {
        return new Vector (this.x + v.x, this.y + v.y);
    }

    public Vector Clamp(double min, double max)
    {
        double m = Magnitude();

        double x = this.x / m;
        double y = this.y / m;

        m = Helper.Clamp(m, min, max);

        x *= m;
        y *= m;

        return new Vector(x, y);
    }

    public double Magnitude()
    {
        return Math.sqrt((x * x) + (y * y));
    }

    public Vector Normalize()
    {
        double magnitude = Magnitude();

        return new Vector(x / magnitude, y / magnitude);
    }

    public Vector Multiply(double factor)
    {
        return new Vector(x * factor, y * factor);
    }

    public Vector Negate()
    {
        return new Vector(-x, -y);
    }

    public double getX() { return x; }
    public double getY() { return y; }

    @Override
    public String toString()
    {
        return "(" + x + "," + y + ")";
    }
}
