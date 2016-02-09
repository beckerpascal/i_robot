package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import kit.edu.irobot.solver.BetterArbitrator;
import kit.edu.irobot.solver.StageSolver;
import lejos.robotics.subsumption.Behavior;

public abstract class RobotBehavior implements Behavior{
	public boolean suppressed = false;
	public boolean exit = false;
	public Robot robot;
	
	protected StageSolver.ExitCallback exitCallback;
	
	public void terminate(){
		this.exit = true;
	}
	
	public void run(){
		while (!suppressed && !exit){			
			Thread.yield();
		}

		robot.stopMotion();
	}
	
	protected void requestArbitratorExit() {
		if (exitCallback != null) exitCallback.exitArby();
	}

	@Override
	public abstract boolean takeControl();

	@Override
	public abstract void action();

	@Override
	public abstract void suppress();
}