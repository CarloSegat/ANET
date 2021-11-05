package moving_node_ex;
import io.jbotsim.core.Node;

public class MovingNode extends Node{
    @Override
    public void onStart(){
        setDirection(Math.random()*2*Math.PI);
    }
    @Override
    public void onClock(){
        move(1);
        wrapLocation();
    }
}
