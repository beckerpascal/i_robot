package kit.edu.irobot.solver;

import kit.edu.irobot.behaviors.FindLine;
import kit.edu.irobot.behaviors.FollowLine;
import kit.edu.irobot.behaviors.BaseBehavior;

/**
 * Behavior for the line following during the challenge
 * @author Pascal Becker
 *
 */
public class LineFollowingStageSolver  extends BaseStageSolver {
	
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
	protected void initArbitrator() {
		findLine = new FindLine(colorSensor, unregPilot, exitCallback);
		followLine = new FollowLine(colorSensor, unregPilot);
		
		BaseBehavior[] behaviorPriority = {followLine, findLine};
	    super.arby = new BetterArbitrator(behaviorPriority, false);	
	}
	
	@Override
	public void solve() {
		unregPilot.travel(((int)startOffset * 10));
		
		super.arby.start();
		unregPilot.stop();
	}

	@Override
	public void stopSolver() {
		super.stopSolver();
		
		findLine.terminate();
		followLine.terminate();
	}
}
