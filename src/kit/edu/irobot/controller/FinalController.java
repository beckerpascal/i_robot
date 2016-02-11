package kit.edu.irobot.controller;

import kit.edu.irobot.solver.BossStageSolver;
import kit.edu.irobot.solver.BridgeStageSolver;
import kit.edu.irobot.solver.LineFollowingStageSolver;
import kit.edu.irobot.solver.PlankBridgeStageSolver;
import kit.edu.irobot.solver.RollingFieldStageSolver;
import kit.edu.irobot.solver.SeesawStageSolver;
import kit.edu.irobot.solver.BaseStageSolver;
import kit.edu.irobot.solver.StartMazeStageSolver;

/**
 * Final Controller with stages for race day.
 * 
 * @author Fabian
 * 
 */
public class FinalController extends BaseController {

	@Override
	protected BaseStageSolver[] createStages() {
		BaseStageSolver maze = new StartMazeStageSolver();
		BaseStageSolver maze_bridge = new LineFollowingStageSolver(30.f);
		BaseStageSolver bridge = new BridgeStageSolver();
		BaseStageSolver line2 = new LineFollowingStageSolver();
		BaseStageSolver seesaw = new SeesawStageSolver();
		BaseStageSolver planks = new PlankBridgeStageSolver();
		BaseStageSolver swamp = new RollingFieldStageSolver();
		BaseStageSolver boss = new BossStageSolver();
		
		return new BaseStageSolver[] {
				maze, 
				maze_bridge,
				bridge,
				line2,
				seesaw,
				planks,
				swamp,
				boss
				};
	}
	
	public static void main(String[] args) {
		FinalController controller = new FinalController();
		controller.menu(false);
	}

}
