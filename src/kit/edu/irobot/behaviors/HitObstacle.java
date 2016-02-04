package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.utility.Delay;

public class HitObstacle extends RobotBehavior {
	
	private EV3TouchSensor touch;
	private float[] touch_samples;
	private boolean touched = false;

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
		
		if(touched){
			return true;
		}
		
		touch.fetchSample(touch_samples, 0);
		if (touch_samples[0] == 1.0){
			touched = true;
			return true;
		}
		
		return false;		
	}

	public void suppress() {
		if(!touched){
			robot.writeBehaviorNameToDisplay("HitObstacle s");
			robot.stopMotion();
		}
	}

	public void action() {
		robot.writeBehaviorNameToDisplay("HitObstacle a");
		LCD.clear();
		LCD.drawString("bumper...", 0, 0);

		robot.setRobotSpeed(0.2f);
		robot.moveRobotBackward();
		Delay.msDelay(1000);
		robot.stopMotion();
		robot.setRobotSpeed(0.2f);
		robot.rotateRobotLeft();
		Delay.msDelay(2000);
		touched = false;
	}
}
