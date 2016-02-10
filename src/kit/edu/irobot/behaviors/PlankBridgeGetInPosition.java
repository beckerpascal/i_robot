package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import kit.edu.irobot.utils.Constants;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;
import lejos.utility.Delay;

public class PlankBridgeGetInPosition extends RobotBehavior {

	private EV3UltrasonicSensor sonar;
	private float[] values;

	private float P,I,D,distance,integral,last_error,derivate;
	
	private final float distance_max = 180.f;
	private final float distance_target = 135.f;
	private final float max_V = 0.2f;
	
	private SampleProvider provider;
	
	public PlankBridgeGetInPosition(Robot robot) {
		
		this.robot = robot;
		sonar = robot.getSensorUltrasonic();
		provider = sonar.getDistanceMode();
		values = new float[provider.sampleSize()];
		
		P = 0.00600f;
		I = 0.f;
		D = 0.0600f;
		last_error = Float.MAX_VALUE;
		integral   = 0.f;
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

			this.robot.setMotorSpeed(powerA, this.robot.getMotorLeft());
			this.robot.setMotorSpeed(powerB, this.robot.getMotorRight());
			this.robot.moveRobotForward();
			
			last_error = error;
			
			LCD.drawString("Distance: " + distance, 1, 2);  
			LCD.drawString("Error: " + error, 1, 3);
			LCD.drawString("Turn: " + Turn, 1, 4);
			LCD.drawString("Power A: " + powerA, 1, 5);
			LCD.drawString("Power B: " + powerB, 1, 6);
			
		}
	}
}
