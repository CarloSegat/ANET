import io.jbotsim.core.*;
import io.jbotsim.core.event.*;
import io.jbotsim.ui.JViewer;
import io.jbotsim.core.Point;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

public class AdvisedWayPointNode extends Node {

    double step = 1;
    static public SinkNode Sink;
    public double distanceToSink = 0;
    public double traveledDistance = 0;
    public java.util.Vector<Point> destinations = new java.util.Vector<Point>();

    static public int nbTransmission = 0;
    static public int startingTime = 0;

    private boolean restart = false;

    public void onStart() {
        this.setProperty("data", true);
        this.traveledDistance = 0.0;
        this.distanceToSink = 0.0;
        this.setProperty("distanceToSink", 0.0);
        this.setColor(Color.green);
        this.setCommunicationRange(30);
        this.destinations.clear();
        this.computeDestinations();
    }


    private boolean doWeTransmitToNode(Node node) {
        if(node.getProperty("sink") != null){
            return true;
        }
        return (double) node.getProperty("distanceToSink") < this.distanceToSink;
    }


    public void onClock() {
        if(this.restart){
            this.restart = false;
            nbTransmission = 0;
            this.getTopology().restart();
            startingTime = this.getTopology().getTime();
        }
        //If we don't have data, we cannot do anything
        if (!(boolean) this.getProperty("data")) {
            return;
        }

        java.util.List<Node> neigList = getNeighbors();
        for (Node node : neigList) {
            if ((boolean) node.getProperty("data")) {
                if (doWeTransmitToNode(node)) {
                    setProperty("data", false);
                    setColor(Color.red);
                    nbTransmission++;
                    break;
                }
            }
            else {
                if (doWeTransmitToNode(node)) {
                    setProperty("data", false);
                    setColor(Color.red);
                    //nbTransmission++;
                    node.setProperty("data", true);
                    node.setColor(Color.green);
                    break;
                }
            }
        }


        if (nbTransmission == this.getTopology().getNodes().size() - 1) {
            System.out.println("Aggregation Done in " + (this.getTopology().getTime() - startingTime) + " time unit ");
            this.restart = true;
        }

    }

    public void onPreClock() {

        double remainingDistance = step;
        while (true) {
            if (destinations.size() == 0) {
                this.computeDestinations();
            }
            this.setDirection(destinations.get(0));
            // If the next destination is close, we move to this destination and we continue to the next destination.
            if (this.getLocation().distance(destinations.get(0)) < remainingDistance) {
                double advancedDistance = this.getLocation().distance(destinations.get(0));
                remainingDistance -= advancedDistance;
                this.setLocation(destinations.get(0));
                destinations.remove(0);
            } else // Else, we move toward the next destination, by the remaining distance
            {
                this.move(remainingDistance);
                break;
            }
        }
        traveledDistance += step;
        if (this.traveledDistance + step >= this.distanceToSink) {
            this.computeDestinations();
        }
    }

    Point projection(Point v, Point w, Point p) {
        // Return minimum distance between line segment vw and point p
        double l2 = v.distance(w);
        if (l2 == 0.0) return v;   // v == w case
        // Consider the line extending the segment, parameterized as v + t (w - v).
        // We find projection of point p onto the line.
        // It falls where t = [(p-v) . (w-v)] / |w-v|^2
        double t = ((p.getX() - v.getX()) * (w.getX() - v.getX()) +
                (p.getY() - v.getY()) * (w.getY() - v.getY())) / l2;
        if (t < 0.0) return v;       // Beyond the 'v' end of the segment
        if (t > 1.0) return w;  // Beyond the 'w' end of the segment

        Point projection = new Point(v.getX() + t * (w.getX() - v.getX()),
                v.getY() + t * (w.getY() - v.getY()));  // Projection falls on the segment
        return projection;
    }

    void computeDestinations() {
        Point origin = this.getLocation();
        boolean firstPass = true;
        if (destinations.size() > 0) {
            origin = destinations.lastElement();
            firstPass = false;
        }

        while (true) {
            Point target = new Point(Math.random() * 600, Math.random() * 600);
            destinations.add(target);
            Point proj = projection(origin, target, this.Sink.getLocation());

            if (proj.distance(this.Sink.getLocation()) < this.getCommunicationRange() - 1.0
                    && (!firstPass ||
                    this.getLocation().distance(this.Sink.getLocation()) > this.getCommunicationRange()
            )
            ) {
                this.distanceToSink += origin.distance(proj);
                this.setProperty("distanceToSink", this.distanceToSink);
                return;
            }
            firstPass = false;
            this.distanceToSink += origin.distance(target);
            origin = target;
        }
    }

}
