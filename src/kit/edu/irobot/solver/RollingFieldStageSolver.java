package kit.edu.irobot.solver;


/**
 * Behavior for rolling field challenge
 * @author Pascal Becker
 *
 */
public class RollingFieldStageSolver extends StageSolver{

	public RollingFieldStageSolver() {
		super("Swamp");
		requestResources(D_PILOT);
	}

	@Override
	public void solve() {
		diffPilot.setTravelSpeed(diffPilot.getMaxTravelSpeed());
		if (active()) diffPilot.travel(200);
	}

	@Override
	public void stopSolver() {
		super.stopSolver();
	}
	
	
}
