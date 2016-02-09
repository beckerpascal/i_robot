package kit.edu.irobot.solver;

import kit.edu.irobot.behaviors.LineCodeBehavior;
import kit.edu.irobot.robot.Robot;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class StageController2 {

	public static void main(String[] args) {
//		StageSolver bossStage         = new BossStageSolver();
//		StageSolver bridgeStage       = new BridgeStageSolver();
//		StageSolver lineFollwingStage = new LineFollowingStageSolver();
//		StageSolver mazeStage         = new MazeStageSolver();
//		StageSolver rollingFieldStage = new RollingFieldStageSolver();
//		StageSolver SeesawStage		  = new SeesawStageSolver();
//		
		/*
		 * StageSolver bossStage = new BossStageSolver(); StageSolver
		 * bridgeStage = new BridgeStageSolver(); StageSolver lineFollwingStage
		 * = new LineFollowingStageSolver(); StageSolver mazeStage = new
		 * MazeStageSolver(); StageSolver rollingFieldStage = new
		 * RollingFieldStageSolver(); StageSolver SeesawStage = new
		 * SeesawStageSolver();
		 */

		// List<StageSolver> solver = new ArrayList<StageSolver>();
		// solver.add(bossStage);
		// solver.add(bridgeStage);
		// solver.add(lineFollwingStage);
		// solver.add(mazeStage);
		// solver.add(rollingFieldStage);
		// solver.add(SeesawStage);

		// rollingFieldStage.solve();
		Robot robot = Robot.getInstance(new boolean[] { true, true, false, true, false, false, false, false });
		LineCodeBehavior lcb = new LineCodeBehavior(robot);
		int button = 0;

		while (button != Button.ID_ESCAPE) {
			int val = lcb.search(button);
			LCD.drawString("Found mode: " + val, 0, 5);
			button = Button.getButtons();
			Delay.msDelay(50);
		}

	}

}
