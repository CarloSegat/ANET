import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.ClockListener;
import io.jbotsim.core.event.TopologyListener;
import io.jbotsim.core.event.ConnectivityListener;

public class CentralizedAlgo implements ClockListener, TopologyListener,
                                               ConnectivityListener{
    private Topology tp;

    public CentralizedAlgo(Topology tp){
        this.tp = tp;
        tp.addClockListener(this);
        tp.addTopologyListener(this);
        tp.addConnectivityListener(this);
    }

    @Override
    public void onClock() {
        if (tp.getTime() % 100 == 0)
            System.out.println("It is " + tp.getTime() + " o'clock!");
    }

    @Override
    public void onNodeAdded(Node node) {
        System.out.println("Hi " + node);
    }

    @Override
    public void onNodeRemoved(Node node) {
        System.out.println("Goodbye " + node);
    }

    @Override
    public void onLinkAdded(Link link) {
        System.out.println("Nodes " + link.endpoint(0) + " and " +
                           link.endpoint(1) + " got in touch!");
    }

    @Override
    public void onLinkRemoved(Link link) {
        System.out.println("Nodes " + link.endpoint(0) + " and " +
                           link.endpoint(1) + " left each other!");
    }
}
