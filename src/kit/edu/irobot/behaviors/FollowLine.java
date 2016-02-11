package kit.edu.irobot.behaviors;

import kit.edu.irobot.utils.Constants;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import kit.edu.irobot.utils.UnregulatedPilot;

/**
 * NOTE: don't try to mess with PID. P is enough!
 * @author Tim
 *
 */
public class FollowLine extends BaseBehavior {
	private static final boolean DEBUG = false;

	private static final float IntegralBackoffLine = 0.2f;
	private static final float IntegralBackoffBlack = 1.f;
	private static final float IntegralDamping = 0.3f;
	private static final float IntegralFactorLine = 8.f;
	private static final float IntegralFactorBlack = 1.f;

	// SEHR GUT: V = 0.65, P = 0.006
	private static final float P = 0.005f;//0.002f;
	private static final float I = 0.f;//0.0005f;
	private static final float D = 0.f;//0.010f; 

	private static final float max_V = 0.45f; 

	private final SampleProvider provider;
	private final UnregulatedPilot pilot;
	private final float[] values;

	private float integral,last_error;

	public FollowLine(EV3ColorSensor color, UnregulatedPilot pilot) {
		this.pilot = pilot;

		provider = color.getRedMode();
		values = new float[1]; // safer to set 
	}

	public boolean takeControl() {
		if (this.exit == true) {
			return false;
		}

		return true;
	}

	public void suppress() {	   
		if (DEBUG) LCD.clear();
		suppressed = true;
	}

	@SuppressWarnings("unused")
	public void action() {
		suppressed = false; 
		LCD.clear();

		pilot.reset();
		last_error = Float.MAX_VALUE;
		integral = 0.f;

		while(!exit && !suppressed ){
			provider.fetchSample(values, 0);

			float lightValue = values[0];

			//error from -100 to 100
			float error = (float) ((lightValue - Constants.PID_OFFSET) * 200);

			if (I > 0.f) {
				/* dampen negative integral on line */
				if (error >= 0 && integral < 0) {
					integral *= IntegralBackoffLine;
				}

				/* dampen positive integral on black */
				if (error < 0 && integral > 0) {
					integral *= IntegralBackoffBlack;
				}

				/* dampen integral over time */
				integral *= IntegralDamping;

				/* add error to integral */
				if (error >= 0.f /* line */) {
					integral += error * IntegralFactorLine;
				} else /* black */ {
					integral += error * IntegralFactorBlack;
				}
			}


			/* D Part */
			/* init last_error */
			if(last_error == Float.MAX_VALUE){
				last_error = error;
			}

			float Turn   = P * error + I * integral + D * (error - last_error);

			// power is in range -100 (full back) to 100 (full forward)
			int powerA = (int)((max_V -  Turn) * 100);         
			int powerB = (int)((max_V +  Turn) * 100);         

			pilot.setPower(powerA, powerB);

			last_error = error;

			if (DEBUG) {
				LCD.drawString("LightV: " + lightValue, 1, 2);  
				LCD.drawString("Error: " + error, 1, 3);
				LCD.drawString("Turn: " + Turn, 1, 4);
				LCD.drawString("Power A: " + powerA, 1, 5);
				LCD.drawString("Power B: " + powerB, 1, 6);
			}
		}	

		pilot.stop();
	}



	public void terminate(){
		this.exit = true;
	}
}