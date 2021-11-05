import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

import io.jbotsim.core.Node;
import io.jbotsim.core.Color;
import io.jbotsim.core.Message;

public class BroadcastNode extends Node{

    private boolean informed;

    public void onStart() {
        informed = false;
        setColor(null);
    }

    public void onSelection() {
        informed = true;
        setColor(Color.RED);
        sendAll(new Message("My message"));
    }

    public void onMessage(Message message) {
        if (! informed) {
            informed = true;
            setColor(Color.RED);
	    System.out.println("Received message, my id is: " + this.getID());
	    Message m = new Message(this.getID());
            sendAll(m);
        }
    }
}
