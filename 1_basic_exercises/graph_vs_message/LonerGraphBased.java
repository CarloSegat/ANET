package graph_vs_message;
import io.jbotsim.core.Color;
import io.jbotsim.core.Link;
import io.jbotsim.core.Node;

public class LonerGraphBased extends Node{
    @Override
    public void onStart() {
        if (getNeighbors().isEmpty()) {
            setColor(Color.green);
        } else {
            setColor(Color.red);
        }
    }

    @Override
    public void onLinkAdded(Link link) {
        setColor(Color.red);
    }

    @Override
    public void onLinkRemoved(Link link) {
        if (getNeighbors().isEmpty())
            setColor(Color.green);
    }
}
