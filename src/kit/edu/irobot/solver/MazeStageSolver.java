package kit.edu.irobot.solver;

import kit.edu.irobot.behaviors.AvoidObstacle;
import kit.edu.irobot.behaviors.ExitOnLight;
import kit.edu.irobot.behaviors.HitObstacle;
import kit.edu.irobot.behaviors.BaseBehavior;

/**
 * Behavior for the maze
 * @author Pascal Becker
 *
 */
public class MazeStageSolver extends BaseStageSolver{	
	
	private BaseBehavior[] behaviors;
	
	public MazeStageSolver() {
		this("Maze");
	}
	
	public MazeStageSolver(String name) {
		super(name);
		requestResources(D_PILOT | MOTORS | TOUCH | COLOR | HEAD);
	}
	
	@Override
	protected void initArbitrator() {
		BaseBehavior b2 = new AvoidObstacle(distanceSensor, motorLeft, motorRight);
		BaseBehavior b3 = new HitObstacle(touchSensor, diffPilot);
		BaseBehavior b4 = new ExitOnLight(colorSensor, exitCallback);

		behaviors = new BaseBehavior[]{b2,b3,b4};
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
}
