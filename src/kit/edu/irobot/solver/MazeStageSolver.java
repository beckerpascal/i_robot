package kit.edu.irobot.solver;

import kit.edu.irobot.behaviors.AvoidObstacle;
import kit.edu.irobot.behaviors.DriveForward;
import kit.edu.irobot.behaviors.HitObstacle;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

/**
 * Behavior for the maze
 * @author Pascal Becker
 *
 */
public class MazeStageSolver extends StageSolver{	
	
	/*behaviours*/
	public DriveForward driveForward;
	public HitObstacle hitObstacle;
	public AvoidObstacle avoidObstacle;
	
	public MazeStageSolver() {
		super("MazeStageSolver");
		
		driveForward = new DriveForward(super.getRobot());
		hitObstacle  = new HitObstacle(super.getRobot());
		avoidObstacle = new AvoidObstacle(super.getRobot());
		
		Behavior[] bArray = {driveForward, hitObstacle, avoidObstacle};
		super.arby = new Arbitrator(bArray,true);
	}

	@Override
	public void run() {
		super.arby.start();
	}
	
	@Override
	public void stopSolver() {
		driveForward.terminate();
		hitObstacle.terminate();
		avoidObstacle.terminate();
		super.getRobot().stopMotion();
		LCD.drawString("stop motion...", 1, 0);
		 
	}
	public static void main(String[] args) 
	{
		MazeStageSolver solver = new MazeStageSolver();
		solver.start();
		while (!Button.ESCAPE.isDown()) {	    	
			Delay.msDelay(500);
		}
		
		solver.stopSolver();
		solver.stopSolver();
		solver.stopSolver();
		solver.stopSolver();
		solver.stopSolver();
	}

	
}
