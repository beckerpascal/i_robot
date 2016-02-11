package kit.edu.irobot.robot;

import lejos.hardware.Sound;

/**
 * Class for handling all things done with the robot like controlling motors,
 * reading sensors or communicate with bluetooth or wifi. Implemented as
 * singleton to be able to get only one instance.
 * 
 * NOTE: This turned out to be bad for threads, so we switched to maintain Sensors and Motors in StageSolvers
 * 
 * @author Pascal Becker
 *
 */

public class Robot {

	public static void beep() {
		Sound.playTone(4000, 333);
	}
}
