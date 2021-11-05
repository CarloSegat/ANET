import io.jbotsim.core.*;
import io.jbotsim.core.event.*;
import io.jbotsim.ui.JViewer;

import io.jbotsim.core.Point;
import java.util.LinkedList;
import java.util.Queue;

public class DTN {

	public static void main(String[] args) {
		Topology tp = new Topology(600,600);
		tp.addClockListener(new Stats(tp));
		tp.setTimeUnit(1);
		new JViewer(tp);
		tp.setDefaultNodeModel(WayPointNode.class);

		SinkNode sink = new SinkNode();
		tp.addNode(300, 300, sink);

		AdvisedWayPointNode.Sink = sink;

		for(int i = 0; i < 100; i++)
		{
			tp.addNode(-1, -1, new AdvisedWayPointNode());
		}
	}
}
