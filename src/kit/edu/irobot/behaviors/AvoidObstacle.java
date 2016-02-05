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

	private float P,I,D,max_V;
	
	public AvoidObstacle(Robot robot) {
		
		this.robot = robot;
		sonar = robot.getSensorUltrasonic();
		sonar.getDistanceMode();
		average = new MeanFilter(sonar, Constants.ULTRASONIC_AVERAGE_AMOUNT);
		values = new float[average.sampleSize()];
		
		P = 400.f;
		I = 0.f;
		D = 0.f;
	
		max_V = 200;
	}

	public boolean takeControl() {
    	if(exit == true){
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
			sonar.fetchSample(values, 0);
			
			//sample.fetchSample(values, 0);
			
			float distance = values[0];
			if(distance > Constants.ULTRASONIC_DISTANCE_MAX)
				distance = (float)Constants.ULTRASONIC_DISTANCE_MAX;
			
			float error = (float) (distance - Constants.ULTRASONIC_DISTANCE_TARGET);
			
			float Turn   = P * error;
			
			Turn = (float) Math.max(0.1*max_V,Turn);
			
			float powerA = max_V +  Turn;         
			float powerB = max_V -  Turn;         
					
			this.robot.driveWithSpeed(powerA, powerB);	
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
