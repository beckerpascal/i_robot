package kit.edu.irobot.solver;

import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

/**
 * Class for executing the seesaw part of the challenge
 * @author Pascal Becker
 *
 */
public class SeesawStageSolver extends StageSolver{

	public SeesawStageSolver() {
		super("SeesawStageSolver");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		DifferentialPilot pilot = robot.getDifferentialPilot();
		pilot.setTravelSpeed(pilot.getMaxTravelSpeed());
		pilot.forward();
		
		waitForBounce();
		
		pilot.travel(-30);
		pilot.rotate(-90);
		pilot.forward();
		waitForBounce();
		pilot.stop();
	}

	@Override
	public void stopSolver() {
		// TODO Auto-generated method stub
		
	}
}
