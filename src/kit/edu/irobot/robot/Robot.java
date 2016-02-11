package kit.edu.irobot.robot;

import java.util.Arrays;

import kit.edu.irobot.utils.Constants;
import kit.edu.irobot.utils.UnregulatedPilot;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.ArcRotateMoveController;
import lejos.robotics.navigation.DifferentialPilot;
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

	public static final int MOTOR_L = 1 << 0;
	public static final int MOTOR_R = 1 << 1;
	public static final int MOTOR_H = 1 << 2;
	public static final int SENSOR_LIGHT = 1 << 3;
	public static final int SENSOR_SONIC = 1 << 4;
	public static final int SENSOR_TOUCH_FRONT = 1 << 5;
	public static final int SENSOR_TOUCH_BACK = 1 << 6;
	public static final int SENSOR_GYRO = 1 << 7;

	public static final int MOTORS = MOTOR_R | MOTOR_L;
	public static final int LINE = SENSOR_LIGHT | MOTORS;
	public static final int ALL = ~(SENSOR_GYRO | SENSOR_TOUCH_BACK);

	private int motorA = 0;
	private int motorB = 1;
	private int motorC = 2;
	private int lightSensor = 3;
	private int sonicSensor = 4;
	private int touchSensor1 = 5;
	private int touchSensor2 = 6;
	@SuppressWarnings("unused")
	private int gyroSensor = 7;

	private boolean[] curActSens = new boolean[8];

	int direction = FORWARD;

	private DifferentialPilot diffPilot;
	private UnregulatedPilot unregPilot;


	/**
	 * Returns an instance of the robot as a Singleton.
	 * 
	 * Use like this:</br>
	 * robot = Robot.getInstance(MOTOR_R | MOROT_L | SENSOR_LIGHT);
	 * 
	 * @param flags
	 *            bitflags for sensors and actors to enable
	 * @return instance of robot
	 */
	public static Robot getInstance(int flags) {
		if (instance == null) {
			instance = new Robot(flags);
		}
		return instance;
	}

	public static Robot getInstance() {
		return getInstance(ALL);
	}


	private Robot(int flags) {
/*
		if ((flags & MOTOR_L) != 0) {
			motorLeft = new EV3LargeRegulatedMotor(Constants.LEFT_MOTOR);
		}
		if ((flags & MOTOR_R) != 0) {
			motorRight = new EV3LargeRegulatedMotor(Constants.RIGHT_MOTOR);
		}
		if ((flags & MOTOR_H) != 0) {
			motorSpecial = new EV3MediumRegulatedMotor(Constants.SPECIAL_MOTOR);
		}
		if ((flags & SENSOR_LIGHT) != 0) {
			sensorLight = new EV3ColorSensor(Constants.LIGHT_SENSOR);
		}
		if ((flags & SENSOR_SONIC) != 0) {
			sensorSonic = new EV3UltrasonicSensor(Constants.DISTANCE_SENSOR);
		}
		if ((flags & SENSOR_TOUCH_FRONT) != 0) {
			sensorTouch_1 = new EV3TouchSensor(Constants.TOUCH_FRONT_SENSOR);
		}
		if ((flags & SENSOR_TOUCH_BACK) != 0) {
			sensorTouch_2 = new EV3TouchSensor(Constants.TOUCH_BACK_SENSOR);
		}
		if ((flags & SENSOR_GYRO) != 0) {
			sensorGyro = new EV3GyroSensor(Constants.GYROSCOP_SENSOR);
		}*/
	}

	public void setLEDPattern(int pattern) {
		Button.LEDPattern(pattern);
	}

	public void writeBehaviorNameToDisplay(String name) {
		LCD.drawString("I am in " + name, 1, 5);
	}

	public void writeErrorToDisplay(String error1, String error2) {
		LCD.drawString(error1, 0, 6);
		LCD.drawString(error2, 0, 7);
	}

	public void beep() {
		Sound.playTone(4000, 333);
	}
}
