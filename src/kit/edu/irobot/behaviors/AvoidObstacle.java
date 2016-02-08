package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import kit.edu.irobot.utils.Constants;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;
import lejos.utility.Delay;

public class AvoidObstacle extends RobotBehavior {

	private EV3UltrasonicSensor sonar;
	private SampleProvider average;
	private float[] values;

	private float P,I,D,max_V,distance,integral;
	
	public AvoidObstacle(Robot robot) {
		
		this.robot = robot;
		sonar = robot.getSensorUltrasonic();
		sonar.getDistanceMode();
		average = new MeanFilter(sonar, Constants.ULTRASONIC_AVERAGE_AMOUNT);
		values = new float[average.sampleSize()];
		
		P = 250.f;
		I = 15.f;
		D = 0.f;
	
		max_V = 350.f;
	}

	public boolean takeControl() {
    	if(super.exit == true){
    		return false;
    	}
		if(distance > Constants.ULTRASONIC_DISTANCE_MAX){
			integral = 0.0f;
			return false;
		}
		
		return true;
	}

	public void suppress() {
		suppressed = true;
	}

	public void action() {
		suppressed = false;
		
		while(!exit && !suppressed){
			//sonar.fetchSample(values, 0);
			average.fetchSample(values, 0);
			
			//sample.fetchSample(values, 0);
			float distance = values[0];
			if(distance > Constants.ULTRASONIC_DISTANCE_MAX)
				distance = (float)Constants.ULTRASONIC_DISTANCE_MAX;
			
			float error = (float) (distance - Constants.ULTRASONIC_DISTANCE_TARGET);
			
			if(Math.abs(error)<= 0.01){
				integral = 0.0f;
			}
			integral = integral + error;
			
			float Turn   = P * error + I * integral;
			
			if(Turn  > 0.00f){
				Turn = (float) Math.min(0.3*max_V,Turn);
			}else{
				Turn = (float) Math.max(-0.3*max_V,Turn);
			}
			float powerA = max_V -  Turn;         
			float powerB = max_V +  Turn;         
					
			this.robot.driveWithSpeed(powerA, powerB);	
			
			LCD.drawString("P" + P , 1, 1);
			LCD.drawString("Distance: " + values[0], 1, 2);  
			LCD.drawString("Error: " + error, 1, 3);
			LCD.drawString("Turn: " + Turn, 1, 4);
			LCD.drawString("Power A: " + powerA, 1, 5);
			LCD.drawString("Power B: " + powerB, 1, 6);
			
			/*leftMotor.setSpeed(powerA);
			leftMotor.forward(); 		

			rightMotor.setSpeed(powerB);
			rightMotor.forward(); 	*/
		/*		
		average.fetchSample(values, 0);
		if(Constants.ULTRASONIC_SENSOR_ON_RIGHT_SIDE){
			// turn left
			if(values[0] < Constants.ULTRASONIC_DISTANCE_TARGET - Constants.ULTRASONIC_DISTANCE_DELTA){
				robot.driveWithSpeed(Constants.ULTRASONIC_SPEED_TARGET - Constants.ULTRASONIC_SPEED_DIFFERENCE, 
									 Constants.ULTRASONIC_SPEED_TARGET + Constants.ULTRASONIC_SPEED_DIFFERENCE);
			// turn right
			}else if(values[0] > Constants.ULTRASONIC_DISTANCE_TARGET + Constants.ULTRASONIC_DISTANCE_DELTA){
				robot.driveWithSpeed(Constants.ULTRASONIC_SPEED_TARGET + Constants.ULTRASONIC_SPEED_DIFFERENCE, 
						 			 Constants.ULTRASONIC_SPEED_TARGET - Constants.ULTRASONIC_SPEED_DIFFERENCE);
			// forward
			}else{
				robot.driveWithSpeed(Constants.ULTRASONIC_SPEED_TARGET, Constants.ULTRASONIC_SPEED_TARGET);
			}
		}else{
			// turn right
			if(values[0] < Constants.ULTRASONIC_DISTANCE_TARGET - Constants.ULTRASONIC_DISTANCE_DELTA){
				robot.driveWithSpeed(Constants.ULTRASONIC_SPEED_TARGET + Constants.ULTRASONIC_SPEED_DIFFERENCE, 
									 Constants.ULTRASONIC_SPEED_TARGET - Constants.ULTRASONIC_SPEED_DIFFERENCE);
			// turn left
			}else if(values[0] > Constants.ULTRASONIC_DISTANCE_TARGET + Constants.ULTRASONIC_DISTANCE_DELTA){
				robot.driveWithSpeed(Constants.ULTRASONIC_SPEED_TARGET - Constants.ULTRASONIC_SPEED_DIFFERENCE, 
						 			 Constants.ULTRASONIC_SPEED_TARGET + Constants.ULTRASONIC_SPEED_DIFFERENCE);
			// forward
			}else{
				robot.driveWithSpeed(Constants.ULTRASONIC_SPEED_TARGET, Constants.ULTRASONIC_SPEED_TARGET);
			}
		}*/
		}
	}
}
