package kit.edu.irobot.solver;

/**
 * Behavior for the final enemy!
 * 
 * @author Pascal Becker
 *
 */
public class BossStageSolver extends MazeStageSolver {

	@Override
	public void solve() {
		diffPilot.setTravelSpeed(diffPilot.getMaxTravelSpeed());
		diffPilot.setRotateSpeed(diffPilot.getMaxRotateSpeed()*0.5f);
		
		if (active()) diffPilot.forward();
		
		if (active()) waitForBounce();
		
		if (active()) diffPilot.rotate(90);
		if (active()) diffPilot.forward();
		if (active()) waitForBounce();
		diffPilot.stop();
		
		super.solve();
	}
	
	public void stopSolver() {
		super.stopSolver();
	}
}
