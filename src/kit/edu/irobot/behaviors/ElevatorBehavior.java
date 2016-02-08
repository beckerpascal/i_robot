package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.utility.Delay;

public class ElevatorBehavior extends RobotBehavior {
	private EV3ColorSensor sensor = null;
	private EV3TouchSensor touch  = null;
	private float[] color;
	
	public ElevatorBehavior(Robot robot){
		this.robot = robot;
		this.sensor = this.robot.getSensorLight();
		this.touch = this.robot.getSensorTouchFront();
		this.sensor.setFloodlight(false);
		this.sensor.getColorID();
		this.color = new float[this.sensor.sampleSize()];
	}
	
	public boolean takeControl() {
		this.sensor.fetchSample(color, 0);
		if(color[0] == 2 || color[0] == 3 || color[0] == 5){ // blue, green or red
			return true;
		}
		return false;
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
		this.sensor.fetchSample(color, 0);
		
		// wait until light is green
		while(!(color[0] == 3)){
			this.sensor.fetchSample(color, 0);
		}
		
		// drive forward until bumper reacts
		float[] bumperVal = new float[this.touch.sampleSize()];
		this.touch.fetchSample(bumperVal, 0);
		while(!(bumperVal[0] == 1)){
			this.robot.getPilot().travel(3);
			this.touch.fetchSample(bumperVal, 0);
		}	
		
		// call elevator to drive downstairs
		// elevater.blabla()
		
		// wait x seconds
		Delay.msDelay(15000);
		
		// drive forward and detect barcode
		this.robot.getPilot().travel(15);
	}

}
