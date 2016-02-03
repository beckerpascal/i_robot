package kit.edu.irobot.solver;

import kit.edu.irobot.behaviors.DriveForward;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

/**
 * Behavior for the final enemy!
 * @author Pascal Becker
 *
 */
public class BossStageSolver extends  StageSolver{
	
	public BossStageSolver() {
		super("BossStageSolver");
		
		Behavior b1 = new DriveForward();

		Behavior[] bArray = {b1};
		super.arby = new Arbitrator(bArray);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void solve() {
		arby.start();
			// TODO Auto-generated method stub
	}

}
