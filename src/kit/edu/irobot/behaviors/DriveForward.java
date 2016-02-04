package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Behavior;

public class DriveForward  implements Behavior {
	   private boolean suppressed = false;
	   
	   public boolean exit;
	   public Robot robot;
	   
	   public void terminate(boolean exit){
		   this.exit = exit;
	   }
	   
	   public DriveForward(Robot robot){
		   this.robot = robot;
	   }
	   
	   public boolean takeControl() {
		   if(this.exit == true) {
			   suppressed = true;
			   return false;
		   }
		   
		   return true;
	   }

	   public void suppress() {
	      suppressed = true;
	   }

	   public void action() {
	     LCD.clear();
	     LCD.drawString("still active...", 0, 0);
		   
		 
	     robot.setRobotSpeed(0.3f);
	     robot.moveRobotForward();
	     
	   }
}
