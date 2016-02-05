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
		robot.writeBehaviorNameToDisplay("AvoidObstacle tC");
		LCD.drawString("Values:"+values[0], 0, 2);
		if (values[0] < Constants.ULTRASONIC_DISTANCE_ACTIVE) {
			return true;
		}
		return false;
	}

	public void suppress() {
		suppressed = true;
	}

	public void action() {
		suppressed = false;
		
		robot.setLEDPattern(5);
		robot.writeBehaviorNameToDisplay("AvoidObstacle a");		
		
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
		
	}
}
