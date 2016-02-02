package kit.edu.irobot.behaviors;

import java.util.List;
import java.util.ArrayList;

import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class StageController{
	
	public static void main(String [] args){
		StageSolver bossStage         = new BossStageSolver();
		StageSolver bridgeStage       = new BridgeStageSolver();
		StageSolver lineFollwingStage = new LineFollowingStageSolver();
		StageSolver mazeStage         = new MazeStageSolver();
		StageSolver rollingFieldStage = new RollingFieldStageSolver();
		StageSolver SeesawStage		  = new SeesawStageSolver();
		
		List<StageSolver> solver = new ArrayList<StageSolver>();
		solver.add(bossStage);
		solver.add(bridgeStage);
		solver.add(lineFollwingStage);
		solver.add(mazeStage);
		solver.add(rollingFieldStage);
		solver.add(SeesawStage);
		
		rollingFieldStage.solve();
		
	}
	
}
