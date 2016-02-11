package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import kit.edu.irobot.solver.StageSolver.ExitCallback;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class ExitOnLight extends RobotBehavior {
	private EV3ColorSensor sensor = null;
	private SampleProvider lightProv = null;
	private float[] lightValue;

	public ExitOnLight(EV3ColorSensor color, ExitCallback callback) {
		this.sensor = color;
		this.lightProv = sensor.getRedMode();
		this.lightValue = new float[1];
		super.exitCallback = callback;
	}

	public boolean takeControl() {
		this.lightProv = sensor.getRedMode();
    	if(super.exit == true){
    		return false;
    	}
    	if(lightValue.length <= 0){
    		this.lightValue = new float[this.lightProv.sampleSize()];
    	}
    	this.lightProv.fetchSample(lightValue, 0);
    	
		// line crossed
		if (lightValue[0] > 0.7f) {
			return true;
		} else {
			return false;
		}
	}

	public void suppress() {
		suppressed = true;
	}

	public void action() {
		// end arby
//		this.robot.getUnregulatedPilot().stop();
		requestArbitratorExit();
//		this.robot.stopMotion();
	}

}
