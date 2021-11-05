package graph_vs_message;
import io.jbotsim.core.Color;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

public class LonerMessageBased extends Node {
    private boolean heardSomeone = false;

    @Override
    public void onClock(){
        if (heardSomeone){
            setColor(Color.red);
        } else {
            setColor(Color.green);
        }
        heardSomeone = false;
        sendAll(new Message());
    }

    @Override
    public void onMessage(Message msg) {
        heardSomeone = true;
    }
}
