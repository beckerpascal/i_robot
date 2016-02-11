package kit.edu.irobot.solver;

import kit.edu.irobot.behaviors.BaseBehavior;
/**
 * Behavior for driving over the bridge 
 * @author Pascal Becker
 *
 */

public class PlankBridgeStageSolver extends BaseStageSolver{	
	private BaseBehavior[] behaviors;
	
	public PlankBridgeStageSolver() {
		super("Planks");
		requestResources(D_PILOT | TOUCH | COLOR);
		
	}

	@Override
	protected void initArbitrator() {
		// not needed since we coded this hard
	}
	
	@Override
	public void solve() {
		
		diffPilot.setTravelSpeed(diffPilot.getMaxTravelSpeed());
		/* in position and still on ramp */
		if (active()) diffPilot.forward();
		if (active()) waitForBounce();
		if (active()) diffPilot.travel(-18);
		if (active()) diffPilot.rotate(-97);
		if (active()) diffPilot.forward();
		
		if (active()) waitForBounce();
		if (active()) diffPilot.travel(-14);
		if (active()) diffPilot.rotate(90);
		diffPilot.stop();
	}
	
	@Override
	public void stopSolver() {
		super.stopSolver();
		
		for( int i = 0; i < behaviors.length; i++)		
			behaviors[i].terminate();
	}
}
