package kit.edu.irobot.solver;

import kit.edu.irobot.behaviors.AvoidObstacle;
import kit.edu.irobot.behaviors.DriveForward;
import kit.edu.irobot.behaviors.HitObstacle;
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
	public DriveForward driveForward;
	public HitObstacle hitObstacle;
	public AvoidObstacle avoidObstacle;
	
	public boolean run = true;
	
	public MazeStageSolver() {
		super("MazeStageSolver");
		
		driveForward = new DriveForward(super.getRobot());
		avoidObstacle = new AvoidObstacle(super.getRobot());
		hitObstacle  = new HitObstacle(super.getRobot());
		
		Behavior[] bArray = {driveForward, avoidObstacle, hitObstacle};
		super.arby = new BetterArbitrator(bArray);
	}

	@Override
	public void run() {
		super.arby.start();
	}
	
	@Override
	public void stopSolver() {

		run = false;
		hitObstacle.terminate();
		driveForward.terminate();
		avoidObstacle.terminate();
		
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
		while (!Button.ESCAPE.isDown()) {	    	
			Delay.msDelay(100);
		}
		solver.stopSolver();
	}
}
