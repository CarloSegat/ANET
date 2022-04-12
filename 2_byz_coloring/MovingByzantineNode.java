import io.jbotsim.core.Color;
import io.jbotsim.core.Node;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class MovingByzantineNode extends Node {
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
            if (color == ((MovingByzantineNode) node).color) {
                return true;
            }
        }
        return false;
    }


    void findMinimalColor(List<Node> l) {
        List<Integer> colors = l.stream().map(n -> ((MovingByzantineNode) n).color).collect(Collectors.toList());
        for(int i = 0; i < all_colors.length; i++){
            if(! colors.contains(i)){
                setColor(i);
                return;
            }
        }
        setColor(0);
    }

    boolean isMinimal(List<Node> l){
        List<Integer> colors = l.stream().map(n -> ((MovingByzantineNode) n).color).collect(Collectors.toList());
        for(int lowerColor = 0; lowerColor < color; lowerColor++){
            if( ! colors.contains(lowerColor) ){
                System.out.println("not minimal");
                return false;
            }
        }
        return true;
    }

    void takeColorOfNeighbour(List<Node> l) {
        int randomNeigh = this.random.nextInt(l.size());
        System.out.println(randomNeigh);
        setColor(((MovingByzantineNode)l.get(randomNeigh)).getColorAsInt());
    }

    @Override
    public void onStart() {
        setDirection(Math.random()*2*Math.PI);
        setColor(0);
    }

    @Override
    public void onSelection() {
        System.out.println("Byzzzzzz");
        this.isByzantine = ! this.isByzantine;
    }

    @Override
    public void onClock() {

        if (isConflict(getNeighbors())) {
            if(! this.isByzantine) {
//                System.out.println("non byz");
                findMinimalColor(getNeighbors());
            }
        } else {
            // no conflicts
            if(this.isByzantine && getNeighbors().size() > 0) {
                takeColorOfNeighbour(getNeighbors());
            }
        }

        if(! this.isByzantine && ! isMinimal(getNeighbors())){
            findMinimalColor(getNeighbors());
        }

        if(this.isByzantine){
            move(1);
            wrapLocation();
        }

    }
}