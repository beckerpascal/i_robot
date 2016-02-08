package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class RollingField  implements Behavior {
	private boolean exit = false; 

	private boolean suppressed = false;
	
	private Robot robot;

	public RollingField(Robot robot) {
		this.robot = robot;
	}

	public boolean takeControl() {
		if (this.exit == true) {
			return false;
		} 
	
		return true;
	}

	public void suppress() {
		suppressed = true;
	}

	public void action() {
		suppressed =  false; 
		
		while(!suppressed)
		{
			robot.driveWithSpeed(1000, 1000);
		    robot.moveRobotForward();
		    Delay.msDelay(3000);
			robot.driveWithSpeed(0, 0);
		    robot.moveRobotForward();
		}
	}

	public void terminate() {
		this.exit = true;
	}
}