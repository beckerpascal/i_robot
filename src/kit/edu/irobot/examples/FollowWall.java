package kit.edu.irobot.examples;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;
import lejos.utility.Delay;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import kit.edu.irobot.utils.Constants;

public class FollowWall{

	public static void main(String[] args) {

		EV3LargeRegulatedMotor leftMotor  = new EV3LargeRegulatedMotor(MotorPort.B);
		EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.A);
		
		EV3UltrasonicSensor sonar = new EV3UltrasonicSensor(SensorPort.S1);
		MeanFilter average = new MeanFilter(sonar, Constants.ULTRASONIC_AVERAGE_AMOUNT);
		
		SampleProvider sample = sonar.getDistanceMode();

		float P = 300.f;
		float I = 0.f;
		float D = 0.f;
	
		float max_V = 300;
		
		boolean abort = false; 
		int button = -1; 
		
		float[] values = new float[sample.sampleSize()];
		
		while(!Button.ESCAPE.isDown())
		{			
			
			sample.fetchSample(values, 0);
			
			//sample.fetchSample(values, 0);
			
			float distance = values[0];
			if(distance > Constants.ULTRASONIC_DISTANCE_MAX)
				distance = (float)Constants.ULTRASONIC_DISTANCE_MAX;
			
			float error = (float) (distance - (Constants.ULTRASONIC_DISTANCE_TARGET*2.0));
			
			float Turn   = P * error;
			
			Turn = (float) Math.min(0.2*max_V,Turn);
			
			float powerA = max_V -  Turn;         
			float powerB = max_V +  Turn;         
						
			leftMotor.setSpeed(powerA);
			leftMotor.forward(); 		

			rightMotor.setSpeed(powerB);
			rightMotor.forward(); 		
		
			LCD.drawString("P" + P , 1, 1);
			LCD.drawString("Distance: " + values[0], 1, 2);  
			LCD.drawString("Error: " + error, 1, 3);
			LCD.drawString("Turn: " + Turn, 1, 4);
			LCD.drawString("Power A: " + powerA, 1, 5);
			LCD.drawString("Power B: " + powerB, 1, 6);
			
			
			button = Button.getButtons();
			
			if(button == Button.ID_UP)
				 P += 10.0;
			
			if(button == Button.ID_DOWN)
				 P -= 10.0;
						
		}
	}

}