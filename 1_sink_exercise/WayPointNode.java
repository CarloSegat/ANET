import io.jbotsim.core.*;

import io.jbotsim.core.Point;
//import java.awt.*;

public class WayPointNode extends Node {
    double step = 1;
    Point destination = new Point(Math.random() * 400, Math.random() * 400);
    static public int nbTransmission = 0;
    static public int startingTime = 0;

    public void onStart() {
        this.setProperty("data", true);
        this.setColor(Color.green);
        this.setCommunicationRange(40);
    }

    private boolean doWeTransmitToNode(Node node) {
        if(isOtherSink(node)){
            return true;
        }
        if(doIMeetSink()){
            return false;
        }
        if(doesOtherMeetSink((WayPointNode) node)){
            return true;
        }
        return false;
    }

    private boolean doesOtherMeetSink(WayPointNode node) {
        return node.getDestination().distance(300.0, 300.0) < 40;
    }

    private boolean doIMeetSink() {
        return destination.distance(300.0, 300.0) < 40;
    }

    private boolean isOtherSink(Node node) {
        return node.getProperty("sink") != null;
    }

    public Point getDestination(){
        return destination;
    }

    public void onClock() {
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
            } else {
                if (doWeTransmitToNode(node)) {
                    setProperty("data", false);
                    setColor(Color.red);
//                    nbTransmission++;
                    node.setProperty("data", true);
                    node.setColor(Color.green);
                    break;
                }
            }
        }


        if (nbTransmission == this.getTopology().getNodes().size() - 1) {
            System.out.println("Aggregation Done in " + (this.getTopology().getTime() - startingTime) + " time unit ");
            nbTransmission = 0;
            this.getTopology().restart();
            startingTime = this.getTopology().getTime();
        }

    }

    public void onPreClock() {

        setDirection(destination);
        if (distance(destination) > step)
            move(step);
        else {
            move(distance(destination));
            destination = new Point(Math.random() * 600, Math.random() * 600);
        }

    }


}
