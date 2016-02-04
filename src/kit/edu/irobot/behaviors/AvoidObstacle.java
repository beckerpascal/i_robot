package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import kit.edu.irobot.utils.Constants;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.subsumption.Behavior;

public class AvoidObstacle implements Behavior {
	private boolean suppressed = false;
	public boolean exit;

	public Robot robot;

	private EV3UltrasonicSensor sonar;

	private float[] values;
	private float[] history = { 0f, 0f, 0f, 0f, 0f };

	public void terminate(boolean exit) {
		this.exit = exit;
	}

	public AvoidObstacle() {
		robot = Robot.getInstance();
		sonar = robot.getSensorUltrasonic();
		sonar.getDistanceMode();
	}

	public boolean takeControl() {
		sonar.fetchSample(values, 0);
		if (values[0] < Constants.ULTRASONIC_DISTANCE_MAX) {
			return true;
		}
		return false;
	}

	public void suppress() {
		sonar.fetchSample(values, 0);
		if (values[0] > 0.2) {
			suppressed = true;
		} else {
			suppressed = false;
		}
	}

	public void action() {
		robot.setLEDPattern(5);
		suppressed = false;
		
		double mean = calcMean();
		if(Constants.ULTRASONIC_SENSOR_ON_RIGHT_SIDE){
			if(mean < Constants.ULTRASONIC_DISTANCE_TARGET - Constants.ULTRASONIC_DISTANCE_DELTA){ 			//turn left
				robot.driveWithSpeed(Constants.ULTRASONIC_SPEED_TARGET - Constants.ULTRASONIC_SPEED_DIFFERENCE, 
									 Constants.ULTRASONIC_SPEED_TARGET + Constants.ULTRASONIC_SPEED_DIFFERENCE);
			}else if(mean > Constants.ULTRASONIC_DISTANCE_TARGET + Constants.ULTRASONIC_DISTANCE_DELTA){ 	// turn right
				robot.driveWithSpeed(Constants.ULTRASONIC_SPEED_TARGET + Constants.ULTRASONIC_SPEED_DIFFERENCE, 
						 			 Constants.ULTRASONIC_SPEED_TARGET - Constants.ULTRASONIC_SPEED_DIFFERENCE);
			}else{																							// just forward
				robot.driveWithSpeed(Constants.ULTRASONIC_SPEED_TARGET, Constants.ULTRASONIC_SPEED_TARGET);
			}
		}else{
			if(mean < Constants.ULTRASONIC_DISTANCE_TARGET - Constants.ULTRASONIC_DISTANCE_DELTA){ 			//turn right
				robot.driveWithSpeed(Constants.ULTRASONIC_SPEED_TARGET + Constants.ULTRASONIC_SPEED_DIFFERENCE, 
									 Constants.ULTRASONIC_SPEED_TARGET - Constants.ULTRASONIC_SPEED_DIFFERENCE);
			}else if(mean > Constants.ULTRASONIC_DISTANCE_TARGET + Constants.ULTRASONIC_DISTANCE_DELTA){ 	// turn left
				robot.driveWithSpeed(Constants.ULTRASONIC_SPEED_TARGET - Constants.ULTRASONIC_SPEED_DIFFERENCE, 
						 			 Constants.ULTRASONIC_SPEED_TARGET + Constants.ULTRASONIC_SPEED_DIFFERENCE);
			}else{																							// just forward
				robot.driveWithSpeed(Constants.ULTRASONIC_SPEED_TARGET, Constants.ULTRASONIC_SPEED_TARGET);
			}
		}

		while (!suppressed){
			Thread.yield();
		}

		robot.stopMotion();

	}

	private double calcMean() {
		refreshHistory();
		double mean = 0;
		for (int i = 0; i < history.length; i++) {
			mean += Constants.ULTRASONIC_MEAN_WEIGHTS[i] * history[i];
		}

		return mean;
	}

	private void refreshHistory() {
		float tmp = values[0];
		if (255 <= tmp) {
			for (int i = 0; i < history.length - 1; i++) {
				history[i] = history[i + 1]; // Shift everything to the left
			}
			history[history.length - 1] = tmp;
		} else {
			// greater 255, infinite distance measured or rubbish
		}
	}
}
