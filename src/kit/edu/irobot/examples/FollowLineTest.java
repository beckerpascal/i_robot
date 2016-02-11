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
			    
		float integral = 0;                          
		float lastError = 0;                         
		float derivative = 0;    
		

	
		boolean abort = false; 
		int button = -1; 
		
		while(!Button.ESCAPE.isDown() && !abort)
		{			
			float[] values = new float[sample.sampleSize()];
			sample.fetchSample(values, 0);
			
			float lightValue = values[0];
			float error = lightValue - Constants.PID_OFFSET;
			
			integral = integral + error; 
			derivative = error - lastError; 
			
			float Turn   = Constants.PID_KP * error + Constants.PID_KI * integral + Constants.PID_KD * derivative;
			
			float powerA = Constants.TARGET_SPEED +  Turn;         
			float powerB = Constants.TARGET_SPEED -  Turn;         
						
			leftMotor.setSpeed(powerA);
			leftMotor.forward(); 		

			rightMotor.setSpeed(powerB);
			rightMotor.forward(); 		
		
			LCD.drawString("P" + Constants.PID_KP + " I" +  Constants.PID_KI + "D" + Constants.PID_KD, 1, 1);
			LCD.drawString("Light Value: " + values[0], 1, 2);  
			LCD.drawString("Error: " + error, 1, 3);
			LCD.drawString("Turn: " + Turn, 1, 4);
			LCD.drawString("Power A: " + powerA, 1, 5);
			LCD.drawString("Power B: " + powerB, 1, 6);
			
			lastError = error;
			
			button = Button.getButtons();
			
			if(button == Button.ID_UP)
			{
				 integral = 0;                          
				 lastError = 0;                         
				 derivative = 0;   
				
				 Constants.PID_KP += 10;
				 Delay.msDelay(1000);
			}
			
			if(button == Button.ID_DOWN)
			{
				 integral = 0;                          
				 lastError = 0;                         
				 derivative = 0;   
				
				 Constants.PID_KI += 0.01f;
				 Delay.msDelay(5000);
			}
			
			if(button == Button.ID_LEFT)
			{
				 integral = 0;                          
				 lastError = 0;                         
				 derivative = 0;
				 
				 Constants.PID_KD += 1;
				 Delay.msDelay(5000);
			}
		}
		leftMotor.close();
		rightMotor.close();
		light.close();
	}

}