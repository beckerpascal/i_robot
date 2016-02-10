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
		
		int distanceBetweenStages = 300;
		
		int mode = 0;
		LCD.drawString("Press LEFT Button to continue!", 0, 0, true);
		int button = Button.waitForAnyEvent();
		
		List<String> modes = new ArrayList<String>();
		modes.add("Maze");
		modes.add("MazePowerMode");
		modes.add("Bridge");
		modes.add("Line");
		modes.add("Seesaw");
		modes.add("SeesawPowerMode");
		modes.add("Hanging Bridge");
		modes.add("Swamp");
		modes.add("Endboss");

		while (!Button.ESCAPE.isDown()) {
			
			if( button == Button.ID_UP){
				mode = mode +1;
				if(mode >= modes.size())mode = 0;
			}else if( button == Button.ID_DOWN){
				mode = mode -1;
				if(mode < 0)mode = modes.size()-1;
			}else if( button == Button.ID_ENTER){
				switch(modes.get(mode)){
				case "Maze":
					solveMaze(distanceBetweenStages);
					break;
				case "MazePowerMode":
					Robot.getInstance().getUnregulatedPilot().travel(4000, 100);
					solveMaze(distanceBetweenStages);
					break;
				case "Bridge": 
					solveBridge(0);
					break;
				case "Line": 
					solveLine(distanceBetweenStages);
					break;
				case "Seesaw": 
					solveSeesaw(distanceBetweenStages);
					break;
				case "SeesawPowerMode": 
					Robot.getInstance().getUnregulatedPilot().travel(1000, 100);
					solveSeesaw(distanceBetweenStages);
					break;
				case "Hanging Bridge": 
					solveHangingBridge(distanceBetweenStages);
					break;
				case "Swamp":
					solveSwamp(distanceBetweenStages);
					break;
				case "Endboss":
					solveBoss(distanceBetweenStages);
					break;					
				}
			}
			button = -1;
			
			LCD.clear();
			LCD.drawString("Mode: "   + modes.get(mode), 0, 0);
			button = Button.getButtons();
			Delay.msDelay(100);
			
						

			// line
//			lineFollowingStage.start();
//			LCD.clear();
//			LCD.drawString("line1", 0, 0);
//			waitForThreadAndDriveAfterwards(lineFollowingStage, robot, 300);

			// bridge
//			bridgeStage.start();
//			LCD.clear();
//			LCD.drawString("bridge", 0, 0);
//			waitForThreadAndDriveAfterwards(bridgeStage, robot, 0);
			
			// line
//			lineFollowingStage = new LineFollowingStageSolver();
//			lineFollowingStage.start();
//			LCD.clear();
//			LCD.drawString("line2", 0, 0);
//			waitForThreadAndDriveAfterwards(lineFollowingStage, robot, 500);

			// seesaw
//			lineFollowingStage = new LineFollowingStageSolver();
//			lineFollowingStage.start();
//			LCD.clear();
//			LCD.drawString("seesaw", 0, 0);
//			waitForThreadAndDriveAfterwards(lineFollowingStage, robot, 250);

			// line
//			lineFollowingStage = new LineFollowingStageSolver();
//			lineFollowingStage.start();
//			LCD.clear();
//			LCD.drawString("line3", 0, 0);
//			waitForThreadAndDriveAfterwards(lineFollowingStage, robot, 250);
//
//			// hanging bridge
//			LCD.clear();
//			LCD.drawString("hanging", 0, 0);
//			robot.getUnregulatedPilot().travel(2500);

			// line
//			lineFollowingStage = new LineFollowingStageSolver();
//			lineFollowingStage.start();
//			LCD.clear();
//			LCD.drawString("line4", 0, 0);
//			waitForThreadAndDriveAfterwards(lineFollowingStage, robot, 250);

			// swamp
//			LCD.clear();
//			LCD.drawString("swamp", 0, 0);
//			robot.getUnregulatedPilot().travel(1000);

			// line
//			lineFollowingStage = new LineFollowingStageSolver();
//			lineFollowingStage.start();
//			LCD.clear();
//			LCD.drawString("line5", 0, 0);
//			waitForThreadAndDriveAfterwards(lineFollowingStage, robot, 150);

			// maze
//			mazeStage = new MazeStageSolver();
//			mazeStage.start();
//			LCD.clear();
//			LCD.drawString("Maze2", 0, 0);
//			waitForThreadAndDriveAfterwards(mazeStage, robot,150);
		}

	}

	public static void waitForThreadAndDriveAfterwards(StageSolver t, Robot r, int distance) {
		while (t.isAlive()) {
			if(Button.UP.isDown()){
				t.stopSolver();				
			}
			Delay.msDelay(1);
		}
		r.getUnregulatedPilot().travel(distance);
		r.stopAndCloseEverything();
}
	
	public static void solveMaze(int distance){
		Robot r = Robot.getInstance();
		StartMazeStageSolver mazeStage = new StartMazeStageSolver();
		mazeStage.start();
		LCD.clear();
		LCD.drawString("Maze", 0, 0);
		waitForThreadAndDriveAfterwards(mazeStage, r, distance);
	}
	
	public static void solveBoss(int distance){
		Robot r = Robot.getInstance();
		BossStageSolver boss = new BossStageSolver();
		boss.start();
		LCD.clear();
		LCD.drawString("Boss", 0, 0);
		waitForThreadAndDriveAfterwards(boss, r, distance);
	}
	
	public static void solveBridge(int distance){
		Robot r = Robot.getInstance();
		BridgeStageSolver bridgeStage = new BridgeStageSolver();
		bridgeStage.start();
		LCD.clear();
		LCD.drawString("bridge", 0, 0);
		waitForThreadAndDriveAfterwards(bridgeStage, r, distance);
	}
	
	public static void solveLine(int distance){
		Robot r = Robot.getInstance();
		LineFollowingStageSolver lineFollowingStage = new LineFollowingStageSolver();
		lineFollowingStage.start();
		LCD.clear();
		LCD.drawString("line2", 0, 0);
		waitForThreadAndDriveAfterwards(lineFollowingStage, r, distance);
	}
	
	public static void solveSeesaw(int distance){
		Robot r = Robot.getInstance();
		LineFollowingStageSolver lineFollowingStage = new LineFollowingStageSolver();
		lineFollowingStage.start();
		LCD.clear();
		LCD.drawString("seesaw", 0, 0);
		waitForThreadAndDriveAfterwards(lineFollowingStage, r, distance);
	}
	
	public static void solveHangingBridge(int distance){
		Robot r = Robot.getInstance();
		LineFollowingStageSolver lineFollowingStage = new LineFollowingStageSolver();
		lineFollowingStage.start();
		LCD.clear();
		LCD.drawString("line3", 0, 0);
		waitForThreadAndDriveAfterwards(lineFollowingStage, r, distance);

		// hanging bridge
		LCD.clear();
		LCD.drawString("hanging", 0, 0);
		r.getUnregulatedPilot().travel(2500);
		r.stopAndCloseEverything();
	}
	
	public static void solveSwamp(int distance){
		Robot r = Robot.getInstance();
		LCD.clear();
		LCD.drawString("swamp", 0, 0);
		r.getUnregulatedPilot().travel(2000, 100);
		r.stopAndCloseEverything();
	}

}
