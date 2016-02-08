package kit.edu.irobot.solver;

import java.util.List;
import java.util.ArrayList;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class StageController{
	
	public static void main(String [] args){
		StageSolver bossStage         = new BossStageSolver();
		StageSolver bridgeStage       = new BridgeStageSolver();
		StageSolver lineFollwingStage = new LineFollowingStageSolver();
		StageSolver mazeStage         = new MazeStageSolver();
		StageSolver rollingFieldStage = new RollingFieldStageSolver();
		StageSolver SeesawStage		  = new SeesawStageSolver();
		
		/*
		List<StageSolver> solver = new ArrayList<StageSolver>();
		solver.add(bossStage);
		solver.add(bridgeStage);
		solver.add(lineFollwingStage);
		solver.add(mazeStage);
		solver.add(rollingFieldStage);
		solver.add(SeesawStage);
		
		rollingFieldStage.solve();
		*/
		LCD.drawString("Starte GEDÖNSE", 0, 1);
		
		while (!Button.ESCAPE.isDown()){
			
		}
		
	}
	
}
