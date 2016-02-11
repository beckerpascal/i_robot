package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
public class DriveForward extends RobotBehavior {


	private EV3LargeRegulatedMotor motor_left, motor_right;
	
	public DriveForward(EV3LargeRegulatedMotor left, EV3LargeRegulatedMotor right) {
		motor_left = left;
		motor_right = right;
	}

	public boolean takeControl() {
		if (this.exit == true) {
			return false;
		}		
		return true;
	}

	public void suppress() {
		suppressed = true;
	}
	

	public void action() {
		suppressed = false;
		
		LCD.clear();
		LCD.drawString("drive forward...", 0, 0);

		robot.setRobotSpeed(0.3f);
		robot.moveRobotBackward();
		
		while(!suppressed && !exit){
			Thread.yield();
		}
		

		motor_left.stop(true);
		motor_right.stop(true);
	}
}
