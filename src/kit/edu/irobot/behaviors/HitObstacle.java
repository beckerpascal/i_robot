package kit.edu.irobot.behaviors;

import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;

public class HitObstacle extends RobotBehavior {
	
	private SampleProvider touch;
	private float[] touch_samples;
	private DifferentialPilot pilot;
	
	public HitObstacle(EV3TouchSensor touch, DifferentialPilot pilot) {
		this.touch = touch.getTouchMode();
		this.pilot = pilot;
		touch_samples = new float[this.touch.sampleSize()];
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

		pilot.travel(-5.0);
		pilot.rotate(-90.0);
		//this.robot.getUnregulatedPilot().travel(-125,75);
		//this.robot.getUnregulatedPilot().rotate(-90,75);
		
	}
}
