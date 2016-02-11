package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import kit.edu.irobot.solver.BetterArbitrator;
import kit.edu.irobot.solver.StageSolver;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.subsumption.Behavior;

public abstract class RobotBehavior implements Behavior{
	public boolean suppressed = false;
	public boolean exit = false;
	public Robot robot;
	
	protected StageSolver.ExitCallback exitCallback;
	
	public void terminate(){
		this.exit = true;
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
	
	public void setMotorSpeed(float speed, RegulatedMotor motor) {
		// speed = Math.abs(speed);
		speed = Math.min(speed, 1.0f);

		float max_speed = motor.getMaxSpeed();
		float new_speed = max_speed * speed;
		motor.setSpeed((int) new_speed);
	}

	// speed = [0-1]
	public void setRobotSpeed(float speed, RegulatedMotor left, RegulatedMotor right) {
		setMotorSpeed(speed, left);
		setMotorSpeed(speed, right);
	}
}