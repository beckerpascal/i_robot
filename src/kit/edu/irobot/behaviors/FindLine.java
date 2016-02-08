package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import lejos.hardware.lcd.LCD;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;
import lejos.robotics.subsumption.Behavior;
import kit.edu.irobot.utils.Constants;

public class FindLine  implements Behavior {
	private boolean exit = false; 

	private boolean suppressed = false;
	private Robot robot;

	public FindLine(Robot robot) {
		this.robot = robot;
	}

	public boolean takeControl() {
		if (this.exit == true) {
			return false;
		} 
		
		SampleProvider average = new MeanFilter(robot.getSensorLight().getRedMode(), 100);
		float[] values = new float[average.sampleSize()];
		
//		if (values[0] < Constants.LIGHT_VALUE_BLACK) {
//			return true;
//		}

		return false;
	}

	public void suppress() {
		suppressed = true;
	}

	public void action() {
		suppressed = false;
		
	    LCD.clear();
	    LCD.drawString("Mode: Find Line: ", 1, 0);
	    
	    robot.getMotorLeft().setSpeed(0);
	    robot.getMotorRight().setSpeed(100);
	    robot.moveRobotForward();
	}

	public void terminate() {
		this.exit = true;
	}
}