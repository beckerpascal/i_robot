package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import lejos.robotics.subsumption.Behavior;

public class DriveForward  implements Behavior {
	   private boolean suppressed = false;
	   public Robot robot;
	   
	   public boolean takeControl() {
	      return true;
	   }

	   public void suppress() {
	      suppressed = true;
	   }

	   public void action() {
	     suppressed = false;
	     
	     robot.setSpeed(0.3f);
	     
	     robot.moveForward();
	     while( !suppressed )
	        Thread.yield();
	    
	     robot.stopMotion();
	     
	   }
}
