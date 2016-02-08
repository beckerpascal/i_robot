package kit.edu.irobot.utils;

import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;

/**
 * Class for all constants used in the code
 * 
 * @author Pascal
 *
 */
public final class Constants {

	public static float TARGET_SPEED = 250;

	/** travel speed in cm/s */
	public static double TRAVEL_SPEED = 25;

	/** roation speed in °/s */
	public static double ROTATE_SPEED = 90;

	public static Port RIGHT_MOTOR = MotorPort.A;
	public static Port LEFT_MOTOR = MotorPort.B;
	public static Port SPECIAL_MOTOR = MotorPort.C;

	public static Port LIGHT_SENSOR = SensorPort.S3;
	public static Port DISTANCE_SENSOR = SensorPort.S1;
	public static Port TOUCH_FRONT_SENSOR = SensorPort.S4;
	public static Port TOUCH_BACK_SENSOR = SensorPort.S2;
	public static Port GYROSCOP_SENSOR = null;// SensorPort.S2;

	public static float PID_KP = 300;
	public static float PID_KI = 2.5f;
	public static float PID_KD = 50;
	public static float PID_OFFSET = 0.45f;
	public static float PID_KI_MAX = 100f;

	public static boolean ULTRASONIC_SENSOR_ON_RIGHT_SIDE = true;
	public static int ULTRASONIC_AVERAGE_AMOUNT = 5;
	public static double ULTRASONIC_DISTANCE_TARGET = 0.07;
	public static double ULTRASONIC_DISTANCE_ACTIVE = 0.25;
	public static double ULTRASONIC_DISTANCE_DELTA = 0.02;
	public static double ULTRASONIC_DISTANCE_MAX = 1;
	public static double ULTRASONIC_SPEED_TARGET = 0.5;
	public static double ULTRASONIC_SPEED_DIFFERENCE = 0.1;

	public static float LIGHT_VALUE_WHITE = 0.7f;
	public static float LIGHT_VALUE_BLACK = 0.05f;
	public static int LIGHT_AVERAGE_AMOUNT = 100;

}
