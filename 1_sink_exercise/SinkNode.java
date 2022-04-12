import io.jbotsim.core.*;
import io.jbotsim.core.event.*;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.Queue;
//import java.awt.*;

public class SinkNode extends Node {

	public void onStart() {
		this.setProperty("data", true);
		this.setProperty("sink", true);
		this.setProperty("distanceToSink", 0.0);
		this.setCommunicationRange(30);
        this.setColor(Color.blue);

	}

}
