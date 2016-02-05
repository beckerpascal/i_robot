package kit.edu.irobot.robot;

import java.util.Arrays;

import kit.edu.irobot.utils.Constants;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

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
	private EV3LargeRegulatedMotor motorLeft = null;
	private EV3LargeRegulatedMotor motorRight = null;
	private EV3MediumRegulatedMotor motorSpecial = null;

	private EV3UltrasonicSensor sensorSonic = null;
	private EV3ColorSensor sensorLight = null;
	private EV3TouchSensor sensorTouch_1 = null;
	private EV3TouchSensor sensorTouch_2 = null;
	private EV3GyroSensor sensorGyro = null;

	final int BACKWARD = -1;
	final int FORWARD = 1;

	private int motorA = 0;
	private int motorB = 1;
	private int motorC = 2;
	private int lightSensor = 3;
	private int sonicSensor = 4;
	private int touchSensor1 = 5;
	private int touchSensor2 = 6;
	private int gyroSensor = 7;

	private boolean[] curActSens = new boolean[8];

	int direction = FORWARD;
	

	/**
	 * Returns an instance of the robot as a Singleton</br>
	 * Init like this:</br>
	 * robot = Robot.getInstance(new boolean[]{true,true,true,false,false,false,false,false});</br> 
	 * Current assignment enables 3 motors, no sensors</br> 
	 * Bools stands for: motorLeft, motorRight, motorC, lightSensor, sonicSensor, touchSensor1, touchSensor2, gyroSensor.</br></br> 
	 * If size of given array does not match size of sensor and actuator, all will be set to true
	 * @param enableArr Array for enabling/disabling actuators and sensors
	 * @return instance of robot
	 */
	public static Robot getInstance(boolean[] enableArr) {
		if (instance == null) {
			instance = new Robot(enableArr);
		}
		return instance;
	}

	private Robot(boolean actSens[]) {

		if (curActSens.length == actSens.length) {
			curActSens = actSens;
		} else {
			writeErrorToDisplay("Error! Not all sensors defined!","Setting all to true!");
			Arrays.fill(curActSens, Boolean.TRUE);
		}

		if (this.curActSens[motorA]) {
			motorLeft = new EV3LargeRegulatedMotor(Constants.LEFT_MOTOR);
		}
		if (this.curActSens[motorB]) {
			motorRight = new EV3LargeRegulatedMotor(Constants.RIGHT_MOTOR);
		}
		if (this.curActSens[motorC]) {
			motorSpecial = new EV3MediumRegulatedMotor(Constants.SPECIAL_MOTOR);
		}
		if (this.curActSens[lightSensor]) {
			sensorLight = new EV3ColorSensor(Constants.LIGHT_SENSOR);
		}
		if (this.curActSens[sonicSensor]) {
			sensorSonic = new EV3UltrasonicSensor(Constants.DISTANCE_SENSOR);
		}
		if (this.curActSens[touchSensor1]) {
			sensorTouch_1 = new EV3TouchSensor(Constants.TOUCH_FRONT_SENSOR);
		}
		if (this.curActSens[touchSensor2]) {
			sensorTouch_2 = new EV3TouchSensor(Constants.TOUCH_BACK_SENSOR);
		}
		if (this.curActSens[motorA]) {
			sensorGyro = new EV3GyroSensor(Constants.GYROSCOP_SENSOR);
		}

	}

	public EV3MediumRegulatedMotor getMotorSpecial() {
		return motorSpecial;
	}

	public EV3UltrasonicSensor getSensorUltrasonic() {
		return sensorSonic;
	}

	public EV3ColorSensor getSensorLight() {
		return sensorLight;
	}

	public EV3TouchSensor getSensorTouchFront() {
		return sensorTouch_1;
	}

	public EV3TouchSensor getSensorTouchBack() {
		return sensorTouch_2;
	}
	
	public EV3GyroSensor getGyroSensor(){
		return sensorGyro;
	}

	public EV3LargeRegulatedMotor getMotorLeft() {
		return motorLeft;
	}

	public EV3LargeRegulatedMotor getMotorRight() {
		return motorRight;
	}

	public void setLEDPattern(int pattern) {
		Button.LEDPattern(pattern);
	}

	/**
	 * Uses the initialized motors and drives with the given speed.
	 * 
	 * @param motorA
	 * @param motorB
	 */
	public void driveWithSpeed(double motorLeft, double motorRight) {
		this.motorLeft.setSpeed((float) motorLeft);
		this.motorRight.setSpeed((float) motorRight);

		this.moveRobotForward();
	}

	public void setMotorSpeed(float speed, RegulatedMotor motor) {
		speed = Math.abs(speed);
		speed = Math.min(speed, 1.0f);

		float max_speed = motor.getMaxSpeed();
		float new_speed = max_speed * speed;
		motor.setSpeed((int) new_speed);
	}

	// speed = [0-1]
	public void setRobotSpeed(float speed) {
		setMotorSpeed(speed, this.motorLeft);
		setMotorSpeed(speed, this.motorRight);
	}

	public void moveRobotForward() {
		// RegulatedMotor[] motors = {this.motorRight};
		// this.motorLeft.synchronizeWith(motors);
		// this.motorLeft.startSynchronization();
		if (direction == BACKWARD) {
			this.motorLeft.backward();
			this.motorRight.backward();
		} else {
			this.motorLeft.forward();
			this.motorRight.forward();
		}
	}

	public void moveRobotBackward() {
		if (direction == BACKWARD) {
			this.motorLeft.forward();
			this.motorRight.forward();
		} else {
			this.motorLeft.backward();
			this.motorRight.backward();
		}
	}

	public void rotateRobotLeft() {
		if (direction == BACKWARD) {
			this.motorLeft.backward();
			this.motorRight.forward();
		} else {
			this.motorLeft.forward();
			this.motorRight.backward();
		}
	}

	public void rotateRobotRight() {
		if (direction == BACKWARD) {
			this.motorLeft.forward();
			this.motorRight.backward();
		} else {
			this.motorLeft.backward();
			this.motorRight.forward();
		}
	}

	public void stopMotion() {

		this.motorLeft.stop();
		this.motorRight.stop();

		this.motorLeft.endSynchronization();
	}

	public void rotateRobot(float angle) {
		float ang_vel = this.motorLeft.getSpeed();
		float rotation_time = angle / ang_vel;

		if (angle < 0.f) {
			rotateRobotRight();
		} else {
			rotateRobotLeft();
		}
		Delay.msDelay((int) (rotation_time * 1000.f));
		stopMotion();
	}

	public void turnAround() {
		rotateRobot(180.f);
	}

	public void writeBehaviorNameToDisplay(String name) {
		LCD.drawString("I am in " + name, 1, 5);
	}
	
	public void writeErrorToDisplay(String error1, String error2){
		LCD.drawString(error1, 0, 6);
		LCD.drawString(error2, 0, 7);
	}
}
