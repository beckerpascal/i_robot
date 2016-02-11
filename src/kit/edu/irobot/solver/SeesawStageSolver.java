package kit.edu.irobot.solver;

/**
 * Class for executing the seesaw part of the challenge
 * @author Pascal Becker
 *
 */
public class SeesawStageSolver extends StageSolver{

	public SeesawStageSolver() {
		super("Seesaw");
		requestResources(D_PILOT | TOUCH);
	}

	@Override
	public void solve() {
		diffPilot.setTravelSpeed(diffPilot.getMaxTravelSpeed());
		if (active()) diffPilot.forward();
		
		if (active()) waitForBounce();
		
		if (active()) diffPilot.travel(-30);
		if (active()) diffPilot.rotate(-90);
		if (active()) diffPilot.forward();
		
		if (active()) waitForBounce();
		
		diffPilot.stop();
	}
}
