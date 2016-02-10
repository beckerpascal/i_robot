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
		//robot.writeBehaviorNameToDisplay("HitObstacle tC");
		if (super.exit == true){
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
		suppressed = false;
		//robot.writeBehaviorNameToDisplay("HitObstacle");

		this.robot.getDifferentialPilot().travel(-5.0);
		this.robot.getDifferentialPilot().rotate(-90.0);
		//this.robot.getUnregulatedPilot().travel(-125,75);
		//this.robot.getUnregulatedPilot().rotate(-90,75);
		
	}
}
