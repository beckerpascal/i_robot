package kit.edu.irobot.solver;

import lejos.robotics.navigation.DifferentialPilot;

public class StartMazeStageSolver extends MazeStageSolver {

	public void run() {
		DifferentialPilot pilot = robot.getDifferentialPilot();
		pilot.setTravelSpeed(pilot.getMaxTravelSpeed());
		pilot.setRotateSpeed(pilot.getMaxRotateSpeed()*0.5f);
		//pilot.rotate(90);
		pilot.forward();
		
		waitForBounce();
		pilot.stop();
		
		super.run();
	}
}
