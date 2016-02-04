package kit.edu.irobot.solver;

import lejos.robotics.subsumption.Behavior;
import lejos.robotics.subsumption.Arbitrator;

import kit.edu.irobot.behaviors.FindLine;
import kit.edu.irobot.behaviors.FollowLine;

/**
 * Behavior for the line following during the challenge
 * @author Pascal Becker
 *
 */
public class LineFollowingStageSolver  extends StageSolver {
	
	private FindLine findLine;
	private FollowLine followLine; 
	
	public LineFollowingStageSolver() {
		super("LineFollowingStageSolver");
	}

	@Override
	public void run() {
		findLine = new FindLine(super.getRobot());
		followLine = new FollowLine(super.getRobot());
		
		Behavior [] behaviorPriority = {findLine, followLine};
	
	    Arbitrator arbitrator = new Arbitrator(behaviorPriority, true);
	    arbitrator.start();
	}

	@Override
	public void stopSolver() {
		followLine.terminate();
	}
}
