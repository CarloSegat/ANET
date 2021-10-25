import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

public class TestCentralizedAlgo{
    public static void main(String[] args) {
	Topology tp = new Topology();
	new CentralizedAlgo(tp);
	new JViewer(tp);
	tp.start();
    }
}
