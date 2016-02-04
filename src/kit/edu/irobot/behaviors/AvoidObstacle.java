package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import kit.edu.irobot.utils.Constants;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;
import lejos.robotics.subsumption.Behavior;

public class AvoidObstacle implements Behavior {
	private boolean suppressed = false;
	public boolean exit;

	public Robot robot;

	private EV3UltrasonicSensor sonar;
	private SampleProvider average;

	private float[] values;

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

		sonar.fetchSample(values, 0);
		if (values[0] < Constants.ULTRASONIC_DISTANCE_MAX) {
			return true;
		}
		return false;
	}

	public void suppress() {
		average.fetchSample(values, 0);
		if (values[0] > 0.2) {
			suppressed = true;
		} else {
			suppressed = false;
		}
	}

	public void action() {
		robot.setLEDPattern(5);
		robot.writeBehaviorNameToDisplay("AvoidObstacleBeh");
		suppressed = false;
		
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

		while (!suppressed && !exit){			
			Thread.yield();
		}

		robot.stopMotion();

	}
}
