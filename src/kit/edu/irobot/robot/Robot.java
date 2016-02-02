package kit.edu.irobot.robot;

import kit.edu.irobot.utils.Constants;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;

/**
 * Class for handling all things done with the robot like controlling motors,
 * reading sensors or communicate with bluetooth or wifi. Implemented as
 * singleton to be able to get only one instance.
 * 
 * @author Pascal Becker
 *
 */

public class Robot {
	private static Robot instance = null;
	EV3LargeRegulatedMotor motorLeft, motorRight;

	private Robot() {

		motorLeft = new EV3LargeRegulatedMotor(Constants.LEFT_MOTOR);
		motorRight = new EV3LargeRegulatedMotor(Constants.RIGHT_MOTOR);

		// TODO: Init all other sensors

	}

	public static Robot getInstance() {
		if (instance == null) {
			instance = new Robot();
		}
		return instance;
	}

	public void setMotorSpeed(float motorA, float motorB) {

	}

	/**
	    * Uses the initialized motors and drives with the given speed.
	    * @param motorA 
	    * @param motorB
	    */
	   public void driveWithSpeed(float motorLeft, float motorRight){
		   this.motorLeft.setSpeed(motorLeft);
		   this.motorRight.setSpeed(motorRight);
	   }
}
