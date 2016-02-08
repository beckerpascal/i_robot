package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class ElevatorBehavior extends RobotBehavior {
	private EV3ColorSensor sensor = null;
	private SampleProvider lightProv = null;
	private SampleProvider touchProv = null;
	private EV3TouchSensor touch = null;
	private float[] color;

	public ElevatorBehavior(Robot robot) {
		this.robot = robot;
		this.sensor = this.robot.getSensorLight();
		this.lightProv = sensor.getRedMode();
		this.touch = this.robot.getSensorTouchFront();
		this.touchProv = this.touch.getTouchMode();
		this.sensor.setFloodlight(false);
		this.color = new float[this.sensor.sampleSize()];
	}

	public boolean takeControl() {
    	if(super.exit == true){
    		return false;
    	}
		// line crossed
		if (true) {
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
		// call elevator with given api
		// elevator.blabla()

		// center robot on platform depending on side of sonic sensor
		robot.getPilot().rotate(-90);
		robot.getPilot().travel(15); // close to center of platform
		robot.getPilot().rotate(90);
		
		this.lightProv.fetchSample(color, 0);		
		while(color[0] <= 0.5){
			this.lightProv.fetchSample(color, 0);		
		}

		// drive forward until bumper reacts
		float[] bumperVal = new float[this.touch.sampleSize()];
		this.touch.fetchSample(bumperVal, 0);
		while (!(bumperVal[0] == 1)) {
			this.robot.getPilot().travel(3);
			this.touchProv.fetchSample(bumperVal, 0);
		}

		// call elevator to drive downstairs
		// elevater.blabla()

		// wait x seconds
		Delay.msDelay(15000);

		// drive forward and detect barcode
		this.robot.getPilot().travel(15);
	}

}
