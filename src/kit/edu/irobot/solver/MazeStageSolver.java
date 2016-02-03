package kit.edu.irobot.solver;

import kit.edu.irobot.behaviors.DriveForward;
import kit.edu.irobot.robot.Robot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

/**
 * Behavior for the maze
 * @author Pascal Becker
 *
 */
public class MazeStageSolver extends StageSolver{	
	
	public MazeStageSolver() {
		super("MazeStageSolver");
		
		Behavior b1 = new DriveForward();

		Behavior[] bArray = {b1};
		super.arby = new Arbitrator(bArray);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void solve() {
		// TODO Auto-generated method stub
		
	}
}
