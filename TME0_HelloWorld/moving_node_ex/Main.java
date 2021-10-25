package moving_node_ex;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;
import moving_node_ex.*;

public class Main{
    public static void main(String[] args){
        Topology tp = new Topology();
        tp.setDefaultNodeModel(MovingNode.class);
        new JViewer(tp);
	tp.setTimeUnit(75);
        tp.start();
    }
}
