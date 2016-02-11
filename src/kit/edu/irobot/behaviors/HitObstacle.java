package kit.edu.irobot.behaviors;

import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;

/**
 * If bounced into anything with touch sensor, set back and rotate right.
 * @author Andi
 *
 */
public class HitObstacle extends BaseBehavior {
	
	private final SampleProvider touch;
	private final float[] touch_samples;
	private final DifferentialPilot pilot;
	
	public HitObstacle(EV3TouchSensor touch, DifferentialPilot pilot) {
		this.touch = touch.getTouchMode();
		this.pilot = pilot;
		touch_samples = new float[1]; // safer to set size
	}

	public boolean takeControl() {
		if (super.exit == true){
			return false;
		}
		
		touch.fetchSample(touch_samples, 0);
		if (touch_samples[0] == 1.0f){
			return true;
		}		
		return false;		
	}

	public void suppress() {
		suppressed = true;
	}

	public void action() {
		suppressed = false;

		pilot.travel(-5.0);
		pilot.rotate(-90.0);	
	}
}
