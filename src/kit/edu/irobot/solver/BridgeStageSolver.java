package kit.edu.irobot.solver;

import kit.edu.irobot.behaviors.DriveForward;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

/**
 * Behavior for driving over the bridge 
 * @author Pascal Becker
 *
 */

public class BridgeStageSolver extends StageSolver{	
	public BridgeStageSolver() {
		super("BridgeStageSolver");
		
		Behavior b1 = new DriveForward(super.getRobot());

		Behavior[] bArray = {b1};
		super.arby = new Arbitrator(bArray);
		super.arby.start();
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopSolver() {
		// TODO Auto-generated method stub
		
	}
}
