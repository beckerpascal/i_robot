package kit.edu.irobot.solver;

import java.util.ArrayList;
import java.util.List;

import kit.edu.irobot.behaviors.LineCodeBehavior;
import kit.edu.irobot.robot.Robot;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class StageController2 {

	public static void main(String[] args) {

		StageSolver bossStage = new BossStageSolver();
		StageSolver bridgeStage = new BridgeStageSolver();
		//StageSolver lineFollwingStage = new LineFollowingStageSolver();
		StageSolver mazeStage = new MazeStageSolver();
		StageSolver rollingFieldStage = new RollingFieldStageSolver();
		StageSolver SeesawStage = new SeesawStageSolver();

		List<StageSolver> solver = new ArrayList<StageSolver>();
		solver.add(mazeStage);
		solver.add(bridgeStage);
		//solver.add(lineFollwingStage);
		solver.add(SeesawStage);
		solver.add(rollingFieldStage);
		solver.add(bossStage);

		Robot robot = Robot.getInstance(new boolean[] { true, true, true, true, true, true, false, false });
		LineCodeBehavior lcb = new LineCodeBehavior(robot);
		int button = 0, stage = 0;
		boolean changeStage = true;

		while (button != Button.ID_ESCAPE) {
			if (changeStage) { // stage changed
				switch (stage) {
				case -1:
					LCD.drawString("Errör!", 0, 5);
					stage = 0;
					break;
				case 0:
					LCD.drawString("Maze!", 0, 5);
					stopAll(solver);
					mazeStage.run();
					break;
				case 1:
					LCD.drawString("Bridge!", 0, 5);
					stopAll(solver);
					//bridgeStage.run();
					break;
				default:
					LCD.drawString("Default!", 0, 5);

					for (int i = 0; i < 5; i++) {
						//Sound.beepSequenceUp();
					}
					break;
				}
				changeStage = false;
				stage++;

			}
			changeStage = lcb.search();

			LCD.drawString("Cur Stage: " + stage, 0, 6);
			button = Button.getButtons();
			Delay.msDelay(2000);
		}

	}
	
	public static void stopAll(List<StageSolver> solver){
		for (int i = 0; i < solver.size(); i++) {
			solver.get(i).stopSolver();
		}
	}

}
