package kit.edu.irobot.solver;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;
import kit.edu.irobot.behaviors.FindLine;
import kit.edu.irobot.behaviors.FollowLine;
import kit.edu.irobot.behaviors.AvoidObstacle;

/**
 * Behavior for the line following during the challenge
 * @author Pascal Becker
 *
 */
public class LineFollowingStageSolver  extends StageSolver {
	
	private FindLine findLine;
	private FollowLine followLine; 
	private AvoidObstacle avoidObstacle;
	
	public LineFollowingStageSolver() {
		super("LineFollowingStageSolver");
	}

	@Override
	public void run() {
		findLine = new FindLine(super.getRobot());
		followLine = new FollowLine(super.getRobot());
		//avoidObstacle = new AvoidObstacle(super.getRobot());
		
		Behavior [] behaviorPriority = {followLine, findLine};
	
	    BetterArbitrator arbitrator = new BetterArbitrator(behaviorPriority, true);
	    arbitrator.start();
	}

	@Override
	public void stopSolver() {
		followLine.terminate();
		findLine.terminate();
		avoidObstacle.terminate();
		
		for( int i = 0; i< 10;i++) {
			super.arby.stop();
			Delay.msDelay(100);
		}
		super.getRobot().stopMotion();
	}
	
	public static void main(String[] args) 
	{
		LineFollowingStageSolver solver =  new LineFollowingStageSolver();
		
		LCD.drawString("Start with UP", 0, 1);
		while (!Button.UP.isDown()) {	    	
			Delay.msDelay(100);
		}
		solver.start();
		
		while (!Button.ESCAPE.isDown()) {	    	
			Delay.msDelay(100);
		}
		solver.stopSolver();
	}
}
