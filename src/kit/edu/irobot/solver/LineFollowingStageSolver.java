package kit.edu.irobot.solver;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
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
	
	public LineFollowingStageSolver() {
		super("LineFollowingStageSolver");
		findLine = new FindLine(super.getRobot(), super.exitCallback);
		followLine = new FollowLine(super.getRobot());
		
		RobotBehavior[] behaviorPriority = {followLine, findLine};
	
	    super.arby = new BetterArbitrator(behaviorPriority, false);	
	}

	@Override
	public void run() {
		super.arby.start();
		}

	@Override
	public void stopSolver() {
		findLine.terminate();
		followLine.terminate();
		
		for( int i = 0; i< 10;i++) {
			super.arby.stop();
			Delay.msDelay(100);
		}
		super.getRobot().stopMotion();
	}
	
	public static void main(String[] args) 
	{
		LineFollowingStageSolver solver = new LineFollowingStageSolver();
		
		LCD.drawString("Start Solver with UP", 0, 1);
		while (!Button.UP.isDown()) {	    	
			Delay.msDelay(100);
		}
		
		solver.start();
		Button.waitForAnyPress();
		solver.stopSolver();
	}
}
