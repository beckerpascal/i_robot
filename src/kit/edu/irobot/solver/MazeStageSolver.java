package kit.edu.irobot.solver;

import kit.edu.irobot.behaviors.AvoidObstacle;
import kit.edu.irobot.behaviors.ExitOnLight;
import kit.edu.irobot.behaviors.HitObstacle;
import kit.edu.irobot.behaviors.RobotBehavior;

/**
 * Behavior for the maze
 * @author Pascal Becker
 *
 */
public class MazeStageSolver extends StageSolver{	
	
	private RobotBehavior[] behaviors;
	
	public MazeStageSolver() {
		super("Maze");
		requestResources(D_PILOT | TOUCH | COLOR | HEAD);

		RobotBehavior b2 = new AvoidObstacle(super.robot);
		RobotBehavior b3 = new HitObstacle(super.robot);
		RobotBehavior b4 = new ExitOnLight(super.robot, super.exitCallback);

		behaviors = new RobotBehavior[]{b2,b3,b4};
		super.arby = new BetterArbitrator(behaviors);
	}
	
	@Override
	public void solve() {
		headUp();
		
		super.arby.start();
		diffPilot.stop();
	}
	
	@Override
	public void stopSolver() {
		super.stopSolver();
		for( int i = 0; i < behaviors.length; i++)
			behaviors[i].terminate();
	}
	
	/*public static void main(String[] args) 
	{
		MazeStageSolver solver = new MazeStageSolver();

		LCD.drawString("Starte MazeSolver mit UP", 0, 1);
		while (!Button.UP.isDown()) {	    	
			Delay.msDelay(100);
		}
		
		Button.waitForAnyPress();
		solver.start();
		Button.ESCAPE.waitForPressAndRelease();
		solver.stopSolver();
	}*/
}
