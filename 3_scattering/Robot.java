import io.jbotsim.core.Topology;
import io.jbotsim.core.event.CommandListener;
import io.jbotsim.ui.JViewer;
import io.jbotsim.core.Node;

import io.jbotsim.core.Point;

import java.util.*;


public class Robot extends Node{

	private static Topology topology_handle;
	private static final double THRESHOLD = 0.1;
	ArrayList<Point> locations;

	static int NB = 300; // Number of robots
	static int finishedCount = NB;
	// To see a scattering that take the size of the robots into account, set EPS to 10
	static double EPS = 0.000001;
	private int myMultiplicity;
	private boolean isWaitingForCentroidToClear = false;
	private static long startTime;


	@Override
	public void onPreClock() {
		isWaitingForCentroidToClear = false;
		locations = new ArrayList<Point>();
		myMultiplicity = 0;

		for (Node node : getTopology().getNodes() ) {
			if(node == this){
				continue;
			}
			if(node.getLocation().distance(this.getLocation()) <= THRESHOLD){
				myMultiplicity++;
			} else {
				locations.add(node.getLocation());
			}
		}
		Point centroid = getCentroid(locations, this.getLocation());
		for (Node node : getTopology().getNodes() ) {
			if(centroid.distance(node.getLocation()) < THRESHOLD){
				// one or more nodes are the centroid
				isWaitingForCentroidToClear = true;
			}
		}
	}

	@Override
	public void onClock(){
		Point centroid = getCentroid(locations, this.getLocation());
		if(this.isWaitingForCentroidToClear){
			if(this.getLocation().distance(centroid) < THRESHOLD){
				Point safeLocation = getLeader(locations, this.getLocation());
				this.setLocation(safeLocation);
			}
			return;
		}

		if(myMultiplicity > 0){
			finishedCount = NB; // reset finish count
			if( (new Random()).nextInt(2) == 0){
				Point safeDestination = generateDestinations(1, locations, this.getLocation(), centroid).get(0);
				this.setLocation(safeDestination);
			}
		} else {
			finishedCount--;
		}
		if(finishedCount <= 0){
			System.out.println("Finished scattering, all nodes are at unique positions");
			topology_handle.pause();
			long endTime = System.nanoTime();
			System.out.println("Took " + (endTime - startTime) / 1000000 + " ms");
		}
	}

	private Point getLeader(ArrayList<Point> locations, Point myLocation) {
		Point maxPoint = myLocation;
		double maxX = myLocation.x;
		double maxY = myLocation.y;
		for (Point p: locations) {
			if(p.x > maxX){
				maxPoint = p;
				maxX = maxPoint.x;
			}
			if(p.y > maxY){
				maxPoint = p;
				maxY = maxPoint.y;
			}
		}
		return maxPoint;
	}

	private List<Point> generateDestinations(int n, List<Point> locations, Point myLocation, Point centroid){
		List<Point> result = new ArrayList<>();
		double dp = centroid.distance(myLocation);
		List<Point> pointsOnMyRay = getPointsOnMyRay(centroid, myLocation, locations);
		Point maxP;
		if(pointsOnMyRay.size() > 0){
			maxP = pointsOnMyRay.stream()
					.max(Comparator.comparing(p -> p.distance(centroid)))
					.get();
		} else {
			maxP = myLocation;
		}

		double d = maxP.distance(centroid);
		int myOrder = pointsOnMyRay.size() - getPointsOnMyRay(myLocation, maxP, locations).size();
		if(pointsOnMyRay.size() == 0){
			myOrder = 1;
		}
		final double SCALE = 0.11;
		for(int i = 0; i < n; i++){
			double deltaX = maxP.x - centroid.x;
			double deltaY = maxP.y - centroid.y;
			double percX = deltaX / (deltaX + deltaY);
			double percY = deltaY / (deltaX + deltaY);
			double destination_x = myLocation.x + d*SCALE*percX + (i*percX);
			double destination_y = myLocation.y + d*(myOrder)*SCALE*percY + (i*percY) + myOrder;

			result.add(new Point(destination_x, destination_y));
		}
		return result;
	}

	private List<Point> getPointsOnMyRay(Point centroid, Point myLocation, List<Point> locations) {
		List<Point> result = new ArrayList<>();
		for (Point p: locations) {
			double x_ratio = (centroid.x - myLocation.x) / (centroid.x - p.x);
			double y_ratio = (centroid.y - myLocation.y) / (centroid.y - p.y);
			boolean isOnRay = Math.abs(x_ratio - y_ratio) < THRESHOLD;
			isOnRay &= (p.x - centroid.x) * (myLocation.x - centroid.x) > 0;
			isOnRay &= (p.y - centroid.y) * (myLocation.y - centroid.y) > 0;
			if( isOnRay){
				result.add(p);
			}
		}
		return result;
	}

	private Point getCentroid(List<Point> locations, Point myLocation) {
		double sumX = myLocation.x;
		double sumY = myLocation.y;
		for (Point p: locations) {
			sumX += p.getX();
			sumY += p.getY();
		}
		return new Point(sumX / (locations.size() + 1), sumY / (locations.size() + 1));
	}


	// Start the simulation
	public static void main(String[] args){

		// Create the Topology (a plane of size 800x400)
		Topology tp = new Topology(800, 800);
		topology_handle = tp;

		tp.addCommandListener(new CommandListener() {
			@Override
			public void onCommand(String command) {
				if(command.equals("Start execution")){
					startTime = System.nanoTime();
				}
			}
		});
		// Create the simulation window
		new JViewer(tp);

		// set the default node to be our Robot class 
		// (When the user click in the simulation window,
		//  a default node is automatically added to the topology)
		tp.setDefaultNodeModel(Robot.class);

		// Robots cannot communicate
		tp.disableWireless();

		// Here we remove the sensing range since the robots have unlimited visibility
		tp.setSensingRange(0);

		// Add NB Robots to the topology (with random positions)
		Random random = new Random();
		tp.addNode(500, 500);
		// Worst case scenario all nodes excpet one at one location
		for (int i = 0; i < NB; i++)

			// the nodes start not at the same place but some share the same locaiton
			tp.addNode(400, 400);
//			tp.addNode((i % 2) * 10 + 400 + random.nextInt(2), (i % 2) * 10 + 400 + random.nextInt(2));


		//The clock click every 0.5 sec (so that you can see the evolution slowly)
		tp.setTimeUnit(500);

		// We pause the simulation 
		// (to start it, you'll have to right click on the window and resume it)
		tp.pause();
	}
}
