package kit.edu.irobot.behaviors;

import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;

/**
 * Attempt to level straight in the entry of the plank bridge.
 * This didn't work that great, we coded that part hard.
 * @author Andi
 *
 */
public class PlankBridgeGetInPosition extends BaseBehavior {
	private static final boolean DEBUG = false;
	
	private static final float P = 0.00600f;;
	private static final float I = 0.f;
	private static final float D = 0.0600f;
	
	private static final float distance_max = 180.f;
	private static final float distance_target = 135.f;
	private static final float max_V = 0.2f;

	private final RegulatedMotor left, right;
	private final SampleProvider provider;
	private final float[] values;

	private float distance, integral, last_error, derivate;
	
	public PlankBridgeGetInPosition(EV3UltrasonicSensor sonar, RegulatedMotor left, RegulatedMotor right) {
		this.left = left;
		this.right = right;
		provider = sonar.getDistanceMode();
		values = new float[provider.sampleSize()];
	}

	public boolean takeControl() {
    	if(super.exit == true){
    		return false;
    	}

		provider.fetchSample(values, 0);
		distance = values[0]*1000.0f;
    	if(distance < distance_max){
    		return true;
    	}
		return false;
	}

	public void suppress() {
		suppressed = true;
	}

	public void action() {
		suppressed = false;

		last_error = Float.MAX_VALUE;
		integral   = 0.f;
		derivate   = Float.MAX_VALUE;
		
		while(!exit && !suppressed && ((Math.abs(last_error) > 1.f) || (Math.abs(derivate) > 0.1f))){
			provider.fetchSample(values, 0);
			
			distance = values[0]*1000.0f;
			if(distance > distance_max) {
				distance = distance_max;
				break;
			}
			
			//error distance from -100 - 100
			float error = (float) (distance - distance_target);
			derivate = error - last_error;
			
			if(Math.abs(error)<= 0.01){
				integral = 0.0f;
			}
			integral = integral + error;
			
			if(last_error == Float.MAX_VALUE){
				last_error = error;
			}
			
			float Turn   = P * error + I * integral + D * derivate;
			
			/*
			if(Turn  > 0.00f){
				Turn = (float) Math.min(0.3*max_V,Turn);
			}else{
				Turn = (float) Math.max(-0.3*max_V,Turn);
			}
			*/
			float powerA = max_V -  Turn;         
			float powerB = max_V +  Turn;         

			this.setMotorSpeed(powerA, left);
			this.setMotorSpeed(powerB, right);
			left.forward();
			right.forward();
			
			last_error = error;
			
			if (DEBUG) {
				LCD.drawString("Distance: " + distance, 1, 2);  
				LCD.drawString("Error: " + error, 1, 3);
				LCD.drawString("Turn: " + Turn, 1, 4);
				LCD.drawString("Power A: " + powerA, 1, 5);
				LCD.drawString("Power B: " + powerB, 1, 6);
			}
			
		}
	}
}
