package kit.edu.irobot.behaviors;

import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class BehaviorController{
	
	public static void main(String [] args){
		Behavior boss = new BossBehavior();
		Behavior bridge = new BridgeBehavior();
		Behavior line = new LineFollowingBehavior();
		Behavior maze = new MazeBehavior();
		Behavior rolling = new RollingFieldBehavior();
		Behavior seesaw = new SeesawBehavior();
		
		Behavior[] bArray = {boss, bridge, line, maze, rolling, seesaw};
		Arbitrator arby = new Arbitrator(bArray);
		arby.start();
	}
	
}
