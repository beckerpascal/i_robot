package kit.edu.irobot.solver;

import kit.edu.irobot.behaviors.DriveForward;
import lejos.robotics.subsumption.Behavior;

/**
 * Behavior for the final enemy!
 * @author Pascal Becker
 *
 */
public class BossStageSolver extends  StageSolver{
	
	public BossStageSolver() {
		super("BossStageSolver");
		
		Behavior b1 = new DriveForward(super.getRobot());

		Behavior[] bArray = {b1};
		super.arby = new BetterArbitrator(bArray, false);
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
