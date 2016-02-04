package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import kit.edu.irobot.utils.Constants;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class AvoidObstacle implements Behavior {
	private boolean suppressed = false;
	public boolean exit = false;

	public Robot robot;

	private EV3UltrasonicSensor sonar;
	private SampleProvider average;

	private float[] values;

	public int x = 0;
	public void terminate(){
		this.exit = true;
	}

	public AvoidObstacle(Robot robot) {
		this.robot = robot;
		sonar = robot.getSensorUltrasonic();
		sonar.getDistanceMode();
		average = new MeanFilter(sonar, Constants.ULTRASONIC_AVERAGE_AMOUNT);
		values = new float[average.sampleSize()];
	}

	public boolean takeControl() {
    	if(exit == true){
    		return false;
    	}
		average.fetchSample(values, 0);
		LCD.drawString("Values:"+values[0], 0, 2);
		if (values[0] < Constants.ULTRASONIC_DISTANCE_ACTIVE) {
			return true;
		}
		x++;
		LCD.drawString("x:"+x,8, 4);
		return false;
	}

	public void suppress() {
		/*
		average.fetchSample(values, 0);
		if (suppressed == false && (values[0] > Constants.ULTRASONIC_DISTANCE_MAX) ) {
			suppressed = true;
			
			
		}else {
			suppressed = false;
		}

		LCD.clear();
		LCD.drawString("suppress AVOID...", 0, 2);
		x++;
		LCD.drawString("x:"+x, 5, 4);
		*/

		x++;
		LCD.drawString("x:"+x ,5, 4);
		robot.stopMotion();
	}

	public void action() {
		suppressed = false;
		
		robot.setLEDPattern(5);
		robot.writeBehaviorNameToDisplay("AvoidObstacleBeh");
		
		
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
		}
		Delay.msDelay(1000);
		x++;
		LCD.drawString("x:"+x, 2, 4);
		
	}
}
