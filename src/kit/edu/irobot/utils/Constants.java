package kit.edu.irobot.utils;

import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;

/**
 * Class for all constants used in the code
 * @author Pascal
 *
 */
public final class Constants {
	
	public static Port RIGHT_MOTOR = MotorPort.A;
	public static Port LEFT_MOTOR = MotorPort.B;
	
	
	public static float PID_KP = 10;                             
	public static float PID_KI = 0;//0.5f;                             
	public static float PID_KD = 0;//50;                           
	public static float PID_OFFSET =0.45f;     
	
}
