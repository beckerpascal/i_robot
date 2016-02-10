package kit.edu.irobot.solver;

import lejos.robotics.navigation.DifferentialPilot;

/**
 * Behavior for the final enemy!
 * 
 * @author Pascal Becker
 *
 */
public class BossStageSolver extends MazeStageSolver {

	@Override
	public void run() {
		DifferentialPilot pilot = robot.getDifferentialPilot();
		pilot.setTravelSpeed(pilot.getMaxTravelSpeed());
		pilot.setRotateSpeed(pilot.getMaxRotateSpeed()*0.5f);
		pilot.forward();
		
		waitForBounce();
		pilot.stop();
		
		super.run();

	}
}
