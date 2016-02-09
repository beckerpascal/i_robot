package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import kit.edu.irobot.solver.StageSolver.ExitCallback;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class ExitOnLight extends RobotBehavior {
	private EV3ColorSensor sensor = null;
	private SampleProvider lightProv = null;
	private float[] lightValue;

	public ExitOnLight(Robot robot, ExitCallback callback) {
		this.robot = robot;
		this.sensor = this.robot.getSensorLight();
		this.lightProv = sensor.getRedMode();
		this.lightValue = new float[this.lightProv.sampleSize()];
		super.exitCallback = callback;
	}

	public boolean takeControl() {
    	if(super.exit == true){
    		return false;
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
		this.robot.getUnregulatedPilot().stop();
		requestArbitratorExit();
		this.robot.stopMotion();
	}

}
