package kit.edu.irobot.solver;

import java.util.ArrayList;
import java.util.List;

import kit.edu.irobot.behaviors.AvoidObstacle;
import kit.edu.irobot.behaviors.DriveForward;
import kit.edu.irobot.behaviors.HitObstacle;
import kit.edu.irobot.behaviors.RobotBehavior;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

/**
 * Behavior for the maze
 * @author Pascal Becker
 *
 */
public class MazeStageSolver extends StageSolver{	
	
	/*behaviours*/
	public boolean run = true;

	private List<RobotBehavior> behaviors;
	
	public MazeStageSolver() {
		super("MazeStageSolver");

		RobotBehavior b1 = new DriveForward(super.getRobot());
		RobotBehavior b2 = new AvoidObstacle(super.getRobot());
		RobotBehavior b3 = new HitObstacle(super.getRobot());

		behaviors = new ArrayList<RobotBehavior>();
		behaviors.add(b1);
		behaviors.add(b2);
		behaviors.add(b3);
		RobotBehavior[] temp = new RobotBehavior[behaviors.size()];
		behaviors.toArray(temp);
		super.arby = new BetterArbitrator(temp);
	}

	@Override
	public void run() {
		super.arby.start();
	}
	
	@Override
	public void stopSolver() {

		run = false;
		for( int i = 0; i < behaviors.size(); i++)
			behaviors.get(i).terminate();
		
		for( int i = 0; i< 10;i++) {
			super.arby.stop();
			Delay.msDelay(100);
		}
		super.getRobot().stopMotion();
		
		LCD.drawString("stop motion...", 1, 0);
		 
	}
	public static void main(String[] args) 
	{
		MazeStageSolver solver = new MazeStageSolver();
		
		LCD.drawString("Starte MazeSolver mit UP", 0, 1);
		while (!Button.UP.isDown()) {	    	
			Delay.msDelay(100);
		}
		solver.start();
		Button.waitForAnyPress();
		solver.stopSolver();
	}
}
