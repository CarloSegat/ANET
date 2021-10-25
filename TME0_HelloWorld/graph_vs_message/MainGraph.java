package graph_vs_message;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;
import graph_vs_message.*;

public class MainGraph{
    public static void main(String[] args){
        Topology tp = new Topology();
        tp.setDefaultNodeModel(LonerGraphBased.class);
        new JViewer(tp);
	tp.setTimeUnit(500);
        tp.start();
    }
}
