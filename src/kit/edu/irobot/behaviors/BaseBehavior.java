package kit.edu.irobot.behaviors;

import kit.edu.irobot.solver.BaseStageSolver;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.subsumption.Behavior;

/**
 * Base class for custom Behaviors.
 * Implements a way to exit the arbitrator.
 * @author Fabian
 *
 */
public abstract class BaseBehavior implements Behavior{
	public boolean suppressed = false;
	public boolean exit = false;
	
	protected BaseStageSolver.ExitCallback exitCallback;
	
	/**
	 * Can be used to terminate the arbitrator.
	 * TODO: this should be unnecessary with the exit callback
	 */
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
	
	/**
	 * Helper method to set the motor speed in the scale of [0..1] TODO: is this [-1..1]
	 * 
	 * @param speed in scale [0..1]
	 * @param motor motor to set the speed of
	 */
	public void setMotorSpeed(float speed, RegulatedMotor motor) {
		// speed = Math.abs(speed);
		speed = Math.min(speed, 1.0f);

		float max_speed = motor.getMaxSpeed();
		float new_speed = max_speed * speed;
		motor.setSpeed((int) new_speed);
	}

	/**
	 * Helper method to set the motor speeds in the scale of [0..1] TODO: is this [-1..1]
	 * @param speed in scale [0..1]
	 * @param left left motor to set the speed of
	 * @param right right motor to set the speed of
	 */
	public void setRobotSpeed(float speed, RegulatedMotor left, RegulatedMotor right) {
		setMotorSpeed(speed, left);
		setMotorSpeed(speed, right);
	}
}