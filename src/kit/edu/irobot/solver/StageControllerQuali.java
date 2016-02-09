package kit.edu.irobot.solver;

import java.util.ArrayList;
import java.util.List;

import kit.edu.irobot.robot.Robot;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class StageControllerQuali {

	public static void main(String[] args) {

		// StageSolver bossStage = new BossStageSolver();
		StageSolver bridgeStage = new BridgeStageSolver();
		StageSolver lineFollowingStage = new LineFollowingStageSolver();
		StageSolver mazeStage = new MazeStageSolver();
		// StageSolver rollingFieldStage = new RollingFieldStageSolver();
		// StageSolver SeesawStage = new SeesawStageSolver();

		List<StageSolver> solver = new ArrayList<StageSolver>();
		solver.add(mazeStage);
		solver.add(bridgeStage);
		// solver.add(lineFollwingStage);
		// solver.add(SeesawStage);
		// solver.add(rollingFieldStage);
		// solver.add(bossStage);

		Robot robot = Robot.getInstance();

		while (!Button.ESCAPE.isDown()) {

			// maze
			mazeStage.start();
			LCD.clear();
			LCD.drawString("Maze", 0, 0);
			waitForThreadAndDriveAfterwards(mazeStage, robot, 300);

			// line
			lineFollowingStage.start();
			LCD.clear();
			LCD.drawString("line1", 0, 0);
			waitForThreadAndDriveAfterwards(lineFollowingStage, robot, 300);

			// bridge
			bridgeStage.start();
			LCD.clear();
			LCD.drawString("bridge", 0, 0);
			waitForThreadAndDriveAfterwards(bridgeStage, robot, 0);
			
			// line
			lineFollowingStage = new LineFollowingStageSolver();
			lineFollowingStage.start();
			LCD.clear();
			LCD.drawString("line2", 0, 0);
			waitForThreadAndDriveAfterwards(lineFollowingStage, robot, 500);

			// seesaw
			lineFollowingStage = new LineFollowingStageSolver();
			lineFollowingStage.start();
			LCD.clear();
			LCD.drawString("seesaw", 0, 0);
			waitForThreadAndDriveAfterwards(lineFollowingStage, robot, 250);

			// line
			lineFollowingStage = new LineFollowingStageSolver();
			lineFollowingStage.start();
			LCD.clear();
			LCD.drawString("line3", 0, 0);
			waitForThreadAndDriveAfterwards(lineFollowingStage, robot, 250);

			// hanging bridge
			LCD.clear();
			LCD.drawString("hanging", 0, 0);
			robot.getUnregulatedPilot().travel(2500);

			// line
			lineFollowingStage = new LineFollowingStageSolver();
			lineFollowingStage.start();
			LCD.clear();
			LCD.drawString("line4", 0, 0);
			waitForThreadAndDriveAfterwards(lineFollowingStage, robot, 250);

			// swamp
			LCD.clear();
			LCD.drawString("swamp", 0, 0);
			robot.getUnregulatedPilot().travel(1000);

			// line
			lineFollowingStage = new LineFollowingStageSolver();
			lineFollowingStage.start();
			LCD.clear();
			LCD.drawString("line5", 0, 0);
			waitForThreadAndDriveAfterwards(lineFollowingStage, robot, 150);

			// maze
			mazeStage = new MazeStageSolver();
			mazeStage.start();
			LCD.clear();
			LCD.drawString("Maze2", 0, 0);
			waitForThreadAndDriveAfterwards(mazeStage, robot,150);
		}

	}

	public static void waitForThreadAndDriveAfterwards(Thread t, Robot r, int distance) {
		while (t.isAlive()) {
			Delay.msDelay(1);
		}
		r.getUnregulatedPilot().travel(distance);
	}

}
