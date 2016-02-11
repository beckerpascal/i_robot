package kit.edu.irobot.behaviors;

import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

/**
 * Follow wall
 * @author Andi
 *
 */
public class AvoidObstacle extends BaseBehavior {
	private static final boolean DEBUG = false;
	
	private static final float P = 0.004f;
	private static final float I = 0.f;
	private static final float D = 0.04f;
	
	private static final float distance_max = 250.f; 		//mm
	private static final float distance_target = 125.f; 	//mm
	private static final float max_V = 0.85f; 				//fraction from max speed

	private final EV3LargeRegulatedMotor motor_left, motor_right;
	private final SampleProvider provider;
	private final float[] values;
	
	private float integral = 0.f;
	private float last_error = Float.MAX_VALUE;
	
	
	public AvoidObstacle(EV3UltrasonicSensor sonar,EV3LargeRegulatedMotor left, EV3LargeRegulatedMotor right) {
		
		this.motor_left = left;
		this.motor_right= right;
		
		provider = sonar.getDistanceMode();
		values = new float[provider.sampleSize()];
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
		integral = 0.f;
		
		while(!exit && !suppressed){
			provider.fetchSample(values, 0);

			// distance in mm
			float distance = values[0]*1000.0f;
			if(distance > distance_max)
				distance = distance_max;
			
			//error distance from -100 - 100
			float error = (float) (distance - distance_target);
			
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

			this.setMotorSpeed(powerA,motor_left);
			this.setMotorSpeed(powerB,motor_right);

			motor_left.forward();
			motor_right.forward();
			
			last_error = error;
			
			if (DEBUG) {
				LCD.drawString("Distance: " + distance, 1, 2);  
				LCD.drawString("Error: " + error, 1, 3);
				LCD.drawString("Turn: " + Turn, 1, 4);
				LCD.drawString("Power A: " + powerA, 1, 5);
				LCD.drawString("Power B: " + powerB, 1, 6);
			}
		}
		motor_left.stop(true);
		motor_right.stop(true);
	}
}
