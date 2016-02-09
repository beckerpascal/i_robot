package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import kit.edu.irobot.solver.StageSolver.ExitCallback;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class ExitOnLight extends RobotBehavior {
	private EV3ColorSensor sensor = null;
	private SampleProvider lightProv = null;
	private SampleProvider touchProv = null;
	private EV3TouchSensor touch = null;
	private float[] lightValue;

	public ExitOnLight(Robot robot, ExitCallback callback) {
		this.robot = robot;
		this.sensor = this.robot.getSensorLight();
		this.lightProv = sensor.getRedMode();
		this.touch = this.robot.getSensorTouchFront();
		this.touchProv = this.touch.getTouchMode();
		this.sensor.setFloodlight(false);
		this.lightValue = new float[this.lightProv.sampleSize()];
		super.exitCallback = callback;
	}

	public boolean takeControl() {
    	if(super.exit == true){
    		return false;
    	}
    	this.lightProv.fetchSample(lightValue, 0);
    	
		// line crossed
		if (lightValue[0] > 0.8f) {
			return true;
		} else {
			return false;
		}
	}

	public void suppress() {
		suppressed = true;
		this.sensor.setFloodlight(true);
	}

	public void action() {
		// end arby
		this.robot.getUnregulatedPilot().stop();
		requestArbitratorExit();
		this.robot.stopMotion();
	}

}
