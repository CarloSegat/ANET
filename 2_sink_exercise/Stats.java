import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.ClockListener;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Stats implements ClockListener {
    private Topology tp;
    private int tick = 0;
    private Map<Integer, Integer> tickToCount = new HashMap();

    public Stats(Topology var1) {
        this.tp = var1;
    }

    public void onClock() {
        List<Node> nodes = this.tp.getNodes();
        tickToCount.put(tick, 0);
        for (Node n : nodes) {
            if ((boolean) n.getProperty("data")) {
                tickToCount.put(tick, tickToCount.get(tick) + 1);
            }
        }
        tick++;

        if(AdvisedWayPointNode.nbTransmission >= 100){
            String json = null;
            try {
                json = new ObjectMapper().writeValueAsString(tickToCount);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter("dataPoints.txt"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                writer.write(json);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
