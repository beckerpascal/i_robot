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
	
	public static float PID_KP = 250;                             
	public static float PID_KI = 0.1f;//0.5f;                             
	public static float PID_KD = 0;//50;                           
	public static float PID_OFFSET =0.45f;     
	
}
