package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Behavior;


public class DriveForward implements Behavior {
	private boolean suppressed = false;

	public boolean exit = false;
	public Robot robot;

	public int x = 0;
	public void terminate() {
		this.exit = true;
	}

	public DriveForward(Robot robot) {
		this.robot = robot;
	}

	public boolean takeControl() {
		if (this.exit == true) {
			return false;
		}
		x++;
		LCD.drawString("x:"+x, 1,2);
		
		return true;
	}

	public void suppress() {
		suppressed = true;
	}
	

	public void action() {
		suppressed = false;
		robot.writeBehaviorNameToDisplay("DriveForwardBeh");

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
