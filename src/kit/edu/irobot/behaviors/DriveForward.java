package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import lejos.hardware.lcd.LCD;
public class DriveForward extends RobotBehavior {

	public DriveForward(Robot robot) {
		this.robot = robot;
	}

	public boolean takeControl() {
		robot.writeBehaviorNameToDisplay("DriveForward tC");
		if (this.exit == true) {
			return false;
		}		
		return true;
	}

	public void suppress() {
		robot.writeBehaviorNameToDisplay("DriveForward s");
		suppressed = true;
	}
	

	public void action() {
		suppressed = false;
		robot.writeBehaviorNameToDisplay("DriveForward a");

		LCD.clear();
		LCD.drawString("drive forward...", 0, 0);

		robot.setRobotSpeed(0.3f);
		robot.moveRobotForward();
		
		while(!suppressed && !exit){
			Thread.yield();
		}
		
		robot.stopMotion();
	}
}
