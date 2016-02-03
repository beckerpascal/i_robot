package kit.edu.irobot.examples;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import lejos.hardware.sensor.EV3ColorSensor;

import lejos.hardware.port.MotorPort;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

import kit.edu.irobot.utils.Constants;

public class FollowLineTest {

	public static void main(String[] args) {

	
		EV3LargeRegulatedMotor leftMotor  = new EV3LargeRegulatedMotor(MotorPort.A);
		EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.B);
		
		EV3ColorSensor light = new EV3ColorSensor(SensorPort.S1);
		SampleProvider sample = light.getRedMode();
		
		LCD.clear();
		
		float TARGET_SPEED = 120;
	    float MAX_SPEED = 200; 
	    
		float integral = 0;                          
		float lastError = 0;                         
		float derivative = 0;                        
	
		float max_light = 0;
		
		boolean abort = false; 
		
		while(!Button.ESCAPE.isDown() && !abort)
		{			
			float[] values = new float[sample.sampleSize()];
			sample.fetchSample(values, 0);
			
			float lightValue = values[0];
			LCD.drawString("Light Value 1: " + values[0], 1, 1);
			
			//max_light = Math.max(lightValue, max_light);
			
			float error = lightValue - Constants.PID_OFFSET;  
			LCD.drawString("Error: " + error, 1, 2);
			
			integral = integral + error; 
			derivative = error - lastError; 
			LCD.drawString("LT: " + integral + " DE " +  derivative, 1, 3);
			
			float Turn = Constants.PID_KP * error + Constants.PID_KI * integral + Constants.PID_KD * derivative;
			LCD.drawString("Turn: " + Turn, 1, 4);
			
			float powerA = TARGET_SPEED +  Turn;         
			float powerB = TARGET_SPEED -  Turn;         
			
			// powerA = Math.min(powerA, MAX_SPEED);
			// powerB = Math.min(powerB, MAX_SPEED);
			   
			LCD.drawString("Power A: " + powerA, 1, 5);
			LCD.drawString("Power B: " + powerB, 1, 6);
					
			leftMotor.setSpeed(powerA);
			leftMotor.backward(); 		

			rightMotor.setSpeed(powerB);
			rightMotor.backward(); 		
		
			lastError = error;             
		}
	}

}