package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.utility.Delay;

public class HitObstacle extends RobotBehavior {
	
	private EV3TouchSensor touch;
	private float[] touch_samples;

	public HitObstacle(Robot robot) {

		this.robot = robot;
		touch = robot.getSensorTouchFront();
		touch.getTouchMode();
		touch_samples = new float[touch.sampleSize()];
	}

	public boolean takeControl() {
		robot.writeBehaviorNameToDisplay("HitObstacle tC");
		if (this.exit == true){
			return false;
		}
		
		touch.fetchSample(touch_samples, 0);
		if (touch_samples[0] == 1.0){
			return true;
		}		
		return false;		
	}

	public void suppress() {
		suppressed = true;
	}

	public void action() {
		robot.writeBehaviorNameToDisplay("HitObstacle a");
		LCD.clear();

		robot.setRobotSpeed(0.2f);
		robot.moveRobotBackward();
		Delay.msDelay(1000);
		robot.stopMotion();
		robot.setRobotSpeed(0.2f);
		robot.rotateRobotLeft();
		Delay.msDelay(2000);
		robot.stopMotion();
	}
}
