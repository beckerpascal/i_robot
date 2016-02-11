package kit.edu.irobot.behaviors;

import kit.edu.irobot.solver.BaseStageSolver.ExitCallback;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;

/**
 * aka: follow edge of bridge
 * 
 * @author Andi
 *
 */
public class GrindtheCrack extends BaseBehavior {
	private static final boolean DEBUG = false;
	
	private static final float P = 0.01f;
	private static final float I = 0.f;
	private static final float D = 0.1f;
	
	private static final float distance_max = 120.f;   //mm
	private static final float distance_target = 95.f; //mm
	private static final float max_V = 0.75f;			//fraction of max speed

	private final float[] values;
	private final RegulatedMotor left;
	private final RegulatedMotor right;
	private final SampleProvider provider;
	
	private float integral;
	private float last_error;
	
	public GrindtheCrack(EV3UltrasonicSensor sonar, RegulatedMotor left, RegulatedMotor right, ExitCallback callback) {
		this.left = left;
		this.right = right;
		this.exitCallback = callback;
		
		provider = sonar.getDistanceMode();
		values = new float[provider.sampleSize()]; //TODO this was a problem in some of the other behaviors
	}

	public boolean takeControl() {
		
    	if(super.exit == true){
    		return false;
    	}
		
		return true;
	}

	public void suppress() {
		suppressed = true;
	}

	public void action() {
		suppressed = false;

		last_error = Float.MAX_VALUE;
		integral   = 0.f;
		
		while(!exit && !suppressed){
			provider.fetchSample(values, 0);
			
			// distance in mm
			float distance = values[0]*1000.0f;
			if(distance > distance_max)
				distance = distance_max;
			
			//error distance from -100 - 100
			float error = (float) ( distance_target - distance );
			
			if(Math.abs(error)<= 0.01){
				integral = 0.0f;
			}
			integral = integral + error;
			
			if(last_error == Float.MAX_VALUE){
				last_error = error;
			}
			
			float Turn   = P * error + I * integral + D * (error - last_error);
			
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
		// TODO synchronize?
		left.stop();
		right.stop();
	}
}
