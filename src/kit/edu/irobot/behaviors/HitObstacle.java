package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class HitObstacle implements Behavior {
	private boolean suppressed = false;
	public Robot robot;

	private EV3TouchSensor touch;

	private float[] touch_samples;
	private boolean exit = false;

	
	public int x = 0;
	public void terminate() {
		this.exit = true;
	}

	public HitObstacle(Robot robot) {

		this.robot = robot;

		touch = robot.getSensorTouchBack();
		touch.getTouchMode();
		touch_samples = new float[touch.sampleSize()];
	}

	public boolean takeControl() {
		if (this.exit == true)
			return false;
		x++;
		LCD.drawString("x:"+x, 0, 4);
		
		touch.fetchSample(touch_samples, 0);
		if (touch_samples[0] == 1.0)
			return true;
		
		suppress();
		return false;
		
	}

	public void suppress() {
		suppressed = true;
	}

	public void action() {
		suppressed = false;
		robot.writeBehaviorNameToDisplay("HitObstacleBeh");
		LCD.clear();
		LCD.drawString("bumper...", 0, 0);

		robot.setRobotSpeed(0.2f);
		robot.moveRobotBackward();
		Delay.msDelay(1000);
		robot.stopMotion();
		robot.setRobotSpeed(0.2f);
		robot.rotateRobotLeft();
		Delay.msDelay(2000);
		
		
		while(!suppressed && !exit){
			Thread.yield();
		}
		robot.stopMotion();
		
		x++;
		LCD.drawString("x:"+x, 2, 4);
	}
}
