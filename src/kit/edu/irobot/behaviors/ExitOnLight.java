package kit.edu.irobot.behaviors;

import kit.edu.irobot.solver.BaseStageSolver.ExitCallback;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

/**
 * Behavior to exit the arbitrator when a line is found.
 * NOTE: is buggy in latest version
 * TODO fix bug
 * 
 * @author Fabian
 *
 */
public class ExitOnLight extends BaseBehavior {
	private final SampleProvider lightProvider;
	private final float[] lightValue;

	public ExitOnLight(EV3ColorSensor color, ExitCallback callback) {
		this.lightProvider = color.getRedMode();
		this.lightValue = new float[1]; //TODO why is there an error on if set to lightProvider.sampleSize()
		super.exitCallback = callback;
	}

	public boolean takeControl() {
    	if(super.exit == true){
    		return false;
    	}
    	
    	this.lightProvider.fetchSample(lightValue, 0);
    	
		// line crossed
    	// TODO look at more then one sample
		if (lightValue[0] > 0.60f) {
			return true;
		} else {
			return false;
		}
	}

	public void suppress() {
		suppressed = true;
	}

	public void action() {
		requestArbitratorExit();
		//TODO stop the robot here?
	}

}
