package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import lejos.robotics.subsumption.Behavior;

public class RobotBehavior implements Behavior{
	public boolean suppressed = false;
	public boolean exit = false;
	public Robot robot;
	
	public void terminate(){
		this.exit = true;
	}
	
	public void run(){
		while (!suppressed && !exit){			
			Thread.yield();
		}

		robot.stopMotion();
	}

	@Override
	public boolean takeControl() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}
}