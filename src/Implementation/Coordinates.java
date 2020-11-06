package Implementation;

import static java.lang.Math.cos;

/**
 * Class for all Coordinates
 * Usages: Creating Coordinate Objects that carry both WGS and Relative Coordinate Values
 * Mathematical Operations: Adding, POSSIBLE ADDITIONS NEEDED
 */

public class Coordinates
{
    //Relative
    private double XPos;
    private double YPos;

    //WGS
    private double yLongitude;
    private double xLatitude;

    // Point of Origin
    private static final double XBase = -0.38434;
    private static final double YBase =51.87189;

    /**
     * Creates new Coordinate object from WGS values
     * containing both WGS (yLongitude & xLatitude) and Relative (xPos, yPos)  values
     * @param xLatitude XWGS coordinate
     * @param yLongitude YWGS coordinate
     * @return Coordinate Object
     */
    public static Coordinates WGS(double xLatitude, double yLongitude)
    {
        Coordinates coord = new Coordinates();

        coord.yLongitude = yLongitude;
        coord.xLatitude = xLatitude;

        coord.YPos = (YBase - yLongitude) * 60 * 1852;
        coord.XPos = (xLatitude - XBase) * 60 * 1852 * cos(Math.toRadians(YBase));

        return coord;
    }

    /**
     * Creates new Coordinate object from x & y coordinates Relative to the origin
     * containing both WGS (yLongitude & xLatitude) and Relative (xPos, yPos) values
     * Can be used with simple x & y coordinate values and the Vector class
     * @param xPos Relative X coordinate
     * @param yPos Relative Y coordinate
     * @return Coordinate Object
     */
    public static Coordinates Relative(double xPos, double yPos)
    {
        Coordinates coord = new Coordinates();

        coord.yLongitude = -1 * ((yPos / (60 * 1852)) - YBase);
        coord.xLatitude = (xPos / (60 * 1852 * cos(Math.toRadians(YBase)))) + XBase;

        coord.XPos = xPos;
        coord.YPos = yPos;

        return coord;
    }

    private Coordinates() {}

    // Copy constructor, avoids pointer problems as it creates a new object
    public Coordinates(Coordinates copy)
    {
        this.XPos = copy.XPos;
        this.YPos = copy.YPos;

        this.xLatitude = copy.xLatitude;
        this.yLongitude = copy.yLongitude;
    }

    /**
     * Adds a Coordinate object to another one
     * If you only have XWGS & YWGS Values create a new Coordinate Object first
     * @param coordinates the Coordinates to be added
     * @return new Added Coordinate Object
     */
    public Coordinates Add(Coordinates coordinates)
    {
        return Coordinates.WGS(xLatitude + coordinates.xLatitude, yLongitude + coordinates.yLongitude);
    }


    /**
     * Adds a Vector to a Coordinate object
     * If you only have Relative xPos & yPos values create a new Vector first
     * @param vector the Vector to be added
     * @return new Added Coordinate Object
     */
    public Coordinates Add(Vector vector)
    {
        return Coordinates.Relative(XPos + vector.getX(), YPos + vector.getY());
    }


    /*----------------------------
    --------- Get & Set-----------
     -----------------------------*/

    public double getXPos() { return XPos; }
    public double getYPos() { return YPos; }
    public double getyLongitude() { return yLongitude; }
    public double getxLatitude() { return xLatitude; }

    @Override
    public String toString()
    {
        return String.format("RelX = %f RelY = %f xLatitude = %f yLongitude = %f", XPos, YPos, xLatitude, yLongitude);
    }
}
