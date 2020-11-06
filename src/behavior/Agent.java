package behavior;

import Implementation.*;

import java.awt.*;
import java.util.*;

/**
 * Schnittstelle zur Steuerung eines Agentens durch den Verhaltensbaum
 * Diese Datei sollte erweitert werden, um die benoetigte Funktionalitaet
 * auch tatsaechlich zu enthalten.
 */
public class Agent {


    double goalSpeed;

    ArrayList<Node> plan;
    ArrayList<Node> alternativePlan;
    BehaveNode tree;
    static Airport airport;

    Flight flight;
    Node goal_node;
    Node current_node;
    Edge current_edge;
    Aircraft plane;
    boolean lastWayPointArrived;

    double speedAtGoalNode;
    double current_acc;
    double len_edge;
    double changeSpeedPosition;

    double current_speed;
    Node breakPosition;
    double init_speed;
    double init_acc;
    double maxAcc;


    double duration;
    double time;
    double maxSpeed;

    boolean speedSet;

    boolean nodevisited;


    public Agent(Aircraft plane, Airport airport) {
        this.plane = plane;
        this.airport = airport;
        current_acc = 0;
        duration = 0;
        speedSet = false;


    }

    public Agent(Flight flight, Airport airport) {
        this.flight = flight;
        this.airport = airport;


    }


    public void tick(int now) {


        double displacement = calDisplacementCeil(goalSpeed, init_speed, init_acc);
        double breakDistance = len_edge - displacement;
        changeSpeedPosition = breakDistance;

        breakPosition = calPointOnEdge(breakDistance, current_node, goal_node);


////////// move with tree/////////////
        /**

         if (tree.getStateKind() == StateKind.SUCCESS || tree.getStateKind() == StateKind.FAILURE||tree.getStateKind()== StateKind.INVALID) {
         tree.init(now, this);
         }



         tree.printTree();

         tree.tick(now);
         **/

////////// move without tree////////////


        double edgeDone = calEdgeDone();
        boolean reachedGoalSpeed = false;

        if (current_speed == goalSpeed) {
            reachedGoalSpeed = true;

        }


        // if break
        /**
         if(speedAtGoalNode == 0) {
         Point2 predict = new Point2(plane.getX(), plane.getY());
         Point2 goal = new Point2(goal_node.getX(), goal_node.getY());
         Point2 start = new Point2(current_node.getX(), current_node.getY());
         movePoint(predict, goal, current_speed);

         double predict_position = calEdgeDone(start, predict);
         if (predict_position >= len_edge) {

         goalSpeed = len_edge - calEdgeDone();

         }
         }
         **/

        if (!nodevisited && reachedGoalSpeed && current_speed == 0) {
            goalSpeed = 0.25 * maxSpeed;


        } else if (current_speed >= speedAtGoalNode) {


            double dis = calDisplacementRound(speedAtGoalNode, current_speed, maxAcc);
            double disfrombegin = dis + edgeDone;


            if (disfrombegin >= 0.85 * len_edge) { // should break now
                goalSpeed = speedAtGoalNode;


            } else if (current_speed < 0.8 * maxSpeed && !speedSet) {


                Aircraft mock = new Aircraft(0, 3, plane.getX(), plane.getY());
                double optimalSpeed = current_speed;
                mock.movePlane(goal_node, optimalSpeed);

                double dis_mock = calDisplacementCeil(optimalSpeed, speedAtGoalNode, maxAcc);
                double disfrombegin_mock = dis_mock + calEdgeDone(current_node.getX(), current_node.getY(), mock.getX(), mock.getY());
                while (disfrombegin_mock < 0.85 * len_edge) {
                    optimalSpeed += optimalSpeed * (0.25);
                    mock.movePlane(goal_node, optimalSpeed);
                    dis_mock = calDisplacementCeil(optimalSpeed, speedAtGoalNode, maxAcc);
                    disfrombegin_mock = dis_mock + calEdgeDone(current_node.getX(), current_node.getY(), mock.getX(), mock.getY());

                }

                goalSpeed = optimalSpeed;
                speedSet = true;


            }


        } else if (current_speed < speedAtGoalNode && !speedSet) {
            goalSpeed = speedAtGoalNode;

        }


        // for breaking at node
        edgeDone = calEdgeDone();
        if (edgeDone >= len_edge) {
            System.out.println(" node visited");
            if (speedAtGoalNode == 0) {
                goalSpeed = 0;
            }
            nodevisited = true;
        }

        ////////////////////////////////move plane//////////////////////

        System.out.println("goal speed: " + goalSpeed);

        // has to slow down
        if (current_speed == goalSpeed) {
            current_acc = 0;

        } else if (current_speed > goalSpeed) {
            current_acc = maxAcc;
            // if slow down with this speed, then speed is too slow then change acc
            if (current_speed - current_acc < goalSpeed) {
                current_acc = (-1) * Math.abs(current_speed - goalSpeed);

            } else {
                current_acc = (-1) * maxAcc;
            }


            // must speed up
        } else if (current_speed < goalSpeed) {
            current_acc = maxAcc;
            if (current_speed + current_acc > goalSpeed) {
                current_acc = Math.abs(goalSpeed - current_speed);
            }


        }
        System.out.println("acc :" + current_acc);
        current_speed += current_acc;


        System.out.println("current Speed " + current_speed);


        plane.movePlane(goal_node, current_speed);
        System.out.println("move with speed of " + current_speed);

        if (duration > 0) {
            duration--;
        }


    }

    public void reachGoalSpeed(double goal_speed, Node goal_node) {
        // adjust acc to reach goal speed
        // can be max or less than max acc
    }

    public void calMaxArrivalSpeed() {

    }


    // cal the distance from start node to the point, where plane should slowdown/ speed up
    public double calChangeSpeedPoint(double current_speed, double speedAtGoalNode, double current_acc) {


        double second = Math.abs(calSecondTakenToReachGoalSpeed(speedAtGoalNode, current_speed, current_acc));

        double displacement = calDisplacementInMeter(speedAtGoalNode, Math.ceil(second), current_acc);

        double changeSpeedPoint = len_edge - displacement;
        // System.out.println("break point :" + changeSpeedPoint);


        return changeSpeedPoint;

    }

    public double calDisplacementCeil(double u, double v, double acc) {
        double t = Math.abs(calSecondTakenToReachGoalSpeed(u, v, acc));

        t = Math.ceil(t);

        return calDisplacementInMeter(u, t, acc);

    }


    public double calDisplacementRound(double u, double v, double acc) {
        double t = Math.abs(calSecondTakenToReachGoalSpeed(u, v, acc));

        t = Math.round(t);

        return calDisplacementInMeter(u, t, acc);

    }

    public double calDisplacementForSpeedUp(double u, double v, double acc) {
        double t = Math.abs(calSecondTakenToReachGoalSpeed(u, v, acc));

        t = Math.ceil(t);

        return calDisplacementInMeter(u, t, acc);

    }


    public double calSecondTakenToReachGoalSpeed(double u, double v, double acc) {
        double t = (v - u) / acc;
        return t;
    }


    public double calTimeUsedToReachGoalSpeed(double u, double v, double acc) {
        double t = (v - u) / acc;
        return t;

    }

    public double CalDistanceGivenTime(double u, double acc, double t) {
        return calDisplacementInMeter(u, t, acc);
    }


    public double calDisplacementInMeter(double u, double t, double acc) {
        double s = (u * t) + (acc * t * t) / 2;
        return s;
        // meter used for cal position on the next tick

    }


    public double calEdgeDone(Point source, Point target) {
        return calDistanceBetweenPoints(source.getX(), source.getY(), target.getX(), target.getY());
    }

    public double calEdgeDone() {
        return calDistanceBetweenPoints(current_node.getX(), current_node.getY(), plane.getX(), plane.getY());
    }

    public double calEdgeDone(double source_x, double source_y, double target_x, double target_y) {
        return calDistanceBetweenPoints(source_x, source_y, target_y, target_y);
    }

    public double calDistanceBetweenPoints(
            double x1,
            double y1,
            double x2,
            double y2) {
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }


    public Node calPointOnEdge(double meter, Node source_node, Node final_node) {
        double m = calSlope(source_node, final_node);
        Node result = findPointOnLine(source_node, final_node, meter, m);
        return result;


    }


    public static double calSlope(Node p1, Node p2) {
        return (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
    }


    public static Node findPointOnLine(Node source, Node goal, double l, double m) {
        Node a, b;
        a = new Node();
        b = new Node();

        // slope is 0
        if (m == 0) {

            a.setLocation(source.getX() + l, source.getY());
            b.setLocation(source.getX() - l, source.getY());


        } else if (m == Double.POSITIVE_INFINITY || m == Double.NEGATIVE_INFINITY) {
            a.setLocation(source.getX(), source.getY() + l);
            b.setLocation(source.getX(), source.getY() - l);

        } else {
            double dx = (l / Math.sqrt(1 + (m * m)));
            double dy = m * dx;

            a.setLocation(source.getX() + dx, source.getY() + dy);
            b.setLocation(source.getX() - dx, source.getY() - dy);

        }


        if (calDistancePoint(goal, a) < calDistancePoint(goal, b)) {
            return a;
        } else {
            return b;

        }

        //

    }

    public void movePoint(Point2 plane, Point2 out, double speed) {

        double tx = plane.getX();
        double ty = plane.getY();
        double sx = out.getX();
        double sy = out.getY();
        double deltaX = tx - sx;
        double deltaY = ty - sy;
        double angle = Math.atan2(deltaY, deltaX);

        tx -= speed * Math.cos(angle);
        ty -= speed * Math.sin(angle);

        plane.setLocation(tx, ty);


    }


    public static double calDistancePoint(Node p1, Node p2) {
        double difX = p1.getX() - p2.getX();
        double difY = p1.getY() - p2.getY();
        return Math.sqrt((difX * difX) + (difY * difY));

    }

    public double calEdgeDone(Point2 source, Point2 target) {
        return calDistanceBetweenPoints(source.getX(), source.getY(), target.getX(), target.getY());
    }


}