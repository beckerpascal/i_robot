package kit.edu.irobot.utils;

import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;

/**
 * Class for all constants used in the code
 * @author Pascal
 *
 */
public final class Constants {
	
	public static Port RIGHT_MOTOR = MotorPort.A;
	public static Port LEFT_MOTOR = MotorPort.B;
	public static Port SPECIAL_MOTOR = MotorPort.D;

	public static Port LIGHT_SENSOR        = null; //SensorPort.S1;
	public static Port DISTANCE_SENSOR     = null; //SensorPort.S2;
	public static Port TOUCH_FRONT_SENSOR  = null;// SensorPort.S1;
	public static Port TOUCH_BACK_SENSOR   = null;// SensorPort.S1;
	public static Port GYROSCOP_SENSOR     = null;// SensorPort.S1;
	
	public static float PID_KP = 10;                             
	public static float PID_KI = 0;//0.5f;                             
	public static float PID_KD = 0;//50;                           
	public static float PID_OFFSET =0.45f;     
	
	public static boolean ULTRASONIC_SENSOR_ON_RIGHT_SIDE = true;
	public static float[] ULTRASONIC_MEAN_WEIGHTS = { 0.1f, 0.15f, 0.2f, 0.3f, 0.25f };
	public static double ULTRASONIC_DISTANCE_TARGET = 0.07;
	public static double ULTRASONIC_DISTANCE_ACTIVE = 0.25;
	public static double ULTRASONIC_DISTANCE_DELTA = 0.02;
	public static double ULTRASONIC_DISTANCE_MAX = 1;
	public static double ULTRASONIC_SPEED_TARGET = 0.5;
	public static double ULTRASONIC_SPEED_DIFFERENCE = 0.1;
	
}
