package kit.edu.irobot.solver;

import kit.edu.irobot.behaviors.FindLine;
import kit.edu.irobot.behaviors.FollowLine;
import kit.edu.irobot.behaviors.RobotBehavior;

/**
 * Behavior for the line following during the challenge
 * @author Pascal Becker
 *
 */
public class LineFollowingStageSolver  extends StageSolver {
	
	private FindLine findLine;
	private FollowLine followLine; 
	private float startOffset;
	
	public LineFollowingStageSolver() {
		this(0.f);
	}
	
	public LineFollowingStageSolver(float startOffset) {
		super("Line");
		requestResources(U_PILOT | COLOR);
		this.startOffset = startOffset;
	}

	@Override
	public void solve() {
		findLine = new FindLine(colorSensor, unregPilot, exitCallback);
		followLine = new FollowLine(colorSensor, unregPilot);
		
		RobotBehavior[] behaviorPriority = {followLine, findLine};
	
		unregPilot.travel(((int)startOffset * 10));
	    super.arby = new BetterArbitrator(behaviorPriority, false);	
		
		super.arby.start();
		unregPilot.stop();
	}

	@Override
	public void stopSolver() {
		super.stopSolver();
		
		findLine.terminate();
		followLine.terminate();
	}
	
	/*public static void main(String[] args) 
	{
		LineFollowingStageSolver solver = new LineFollowingStageSolver();
		
		LCD.drawString("Start Solver with UP", 0, 1);
		while (!Button.UP.isDown()) {	    	
			Delay.msDelay(100);
		}
		
		solver.start();
		Button.waitForAnyPress();
		solver.stopSolver();
	}*/
}
