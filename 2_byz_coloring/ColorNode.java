import io.jbotsim.core.Node;
import io.jbotsim.core.Color;
import io.jbotsim.ui.icons.Icons;

import java.util.*;
import java.lang.*;

public class ColorNode extends Node {
    // array of predefined jbotsim colors
    private static final Color all_colors[] = {Color.black, Color.white, Color.gray, Color.
            lightGray, Color.pink, Color.red, Color.darkGray, Color.orange, Color.yellow,
            Color.magenta, Color.cyan, Color.blue, Color.green};
    private static final int nb_colors = all_colors.length;
    // the node current color
    private int color = 0;
    private boolean isByzantine = false;
    private Random random = new Random();

    // Convert internal color to jBotSim color
    Color toColor(int i) {
        return all_colors[i % nb_colors];
    }

    // change node color and adjust graphics
    void setColor(int i) {
        color = i % nb_colors;
        setColor(toColor(i));
    }

    public int getColorAsInt(){
        return this.color;
    }

    // returns true if the node has a color conflict with a neighbor
    boolean isConflict(List<Node> l) {
        for (Node node : l) {
            if (color == ((ColorNode) node).color) {
                return true;
            }
        }
        return false;
    }

    // finds a new color for the node among available colors
// if the node has more neighbors than max nb of colors, the node may not change its color
    void findColor(List<Node> l) {
        int last = color;
        while (isConflict(l)) {
            color += 1 % nb_colors;
            if (color == last) {
                break;
            }
        }
    }

    void takeColorOfNeighbour(List<Node> l) {
        int randomNeigh = this.random.nextInt(l.size());
        System.out.println(randomNeigh);
        setColor(((ColorNode)l.get(randomNeigh)).getColorAsInt());
    }

    @Override
    public void onStart() {
        setColor(0);
    }

    @Override
    public void onSelection() {
        this.isByzantine = ! this.isByzantine;
    }

    @Override
    public void onClock() {
        if (isConflict(getNeighbors())) {
            if(! this.isByzantine) {
                System.out.println("non byz");
                findColor(getNeighbors());
            }
        } else {
            // no conflicts
            if(this.isByzantine && getNeighbors().size() > 0) {
                takeColorOfNeighbour(getNeighbors());
            }
        }

    }
}