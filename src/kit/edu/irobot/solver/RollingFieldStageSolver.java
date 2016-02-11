package kit.edu.irobot.solver;


/**
 * Behavior for rolling field challenge
 * @author Pascal Becker
 *
 */
public class RollingFieldStageSolver extends BaseStageSolver{

	public RollingFieldStageSolver() {
		super("Swamp");
		requestResources(D_PILOT | TOUCH);
	}
	
	@Override
	protected void initArbitrator() {
		// not needed since we coded this hard
	}

	@Override
	public void solve() {
		diffPilot.setTravelSpeed(diffPilot.getMaxTravelSpeed());
		if (active()) diffPilot.forward();
		if (active()) waitForBounce();
		if (active()) diffPilot.travel(-30);
		if (active()) diffPilot.rotate(105);
	}

	@Override
	public void stopSolver() {
		super.stopSolver();
	}
	
	
}
