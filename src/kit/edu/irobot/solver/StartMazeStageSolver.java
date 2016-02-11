package kit.edu.irobot.solver;

public class StartMazeStageSolver extends MazeStageSolver {
	
	public void solve() {
		diffPilot.setTravelSpeed(diffPilot.getMaxTravelSpeed());
		diffPilot.setRotateSpeed(diffPilot.getMaxRotateSpeed()*0.5f);
		//if (active()) diffPilot.rotate(90);
		if (active()) diffPilot.forward();
		
		if (active()) waitForBounce();
		diffPilot.stop();
		
		super.solve();
	}
}
