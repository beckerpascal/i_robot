package kit.edu.irobot.solver;

import kit.edu.irobot.behaviors.ExitOnLight;
import kit.edu.irobot.behaviors.RobotBehavior;
/**
 * Behavior for driving over the bridge 
 * @author Pascal Becker
 *
 */

public class PlankBridgeStageSolver extends StageSolver{	
	private RobotBehavior[] behaviors;
	
	public PlankBridgeStageSolver() {
		super("Planks");
		requestResources(D_PILOT | TOUCH);
		
		//RobotBehavior b2 = new GrindtheCrack(super.getRobot());
		RobotBehavior b3 = new ExitOnLight(robot, exitCallback);

		behaviors = new RobotBehavior[] {b3};
		super.arby = new BetterArbitrator(behaviors);
	}
	
	
	@Override
	public void solve() {
		diffPilot.setTravelSpeed(diffPilot.getMaxTravelSpeed());
		/* in position and still on ramp */
		
		if (active()) diffPilot.travel(-20);
		if (active()) diffPilot.rotate(-90);
		if (active()) diffPilot.forward();
		
		if (active()) waitForBounce();
		if (active()) diffPilot.travel(-20);
		if (active()) diffPilot.rotate(-90);
		diffPilot.stop();
	}
	
	@Override
	public void stopSolver() {
		super.stopSolver();
		
		for( int i = 0; i < behaviors.length; i++)		
			behaviors[i].terminate();
	}
	
	/*public static void main(String[] args) 
	{
		BridgeStageSolver solver = new BridgeStageSolver();
		
		LCD.drawString("Starte BridgeStageSolver mit UP", 0, 1);
		if (Button.waitForAnyPress() == Button.ID_ESCAPE) return;
		
		//solver.setDaemon(true);
		solver.start();
		Button.ESCAPE.waitForPressAndRelease();
		solver.stopSolver();
		LCD.drawString("solver finished :)", 0, 1);
		Delay.msDelay(2000);
	}*/
}
