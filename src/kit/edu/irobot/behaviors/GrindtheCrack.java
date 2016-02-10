package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import kit.edu.irobot.solver.StageSolver.ExitCallback;
import kit.edu.irobot.utils.Constants;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;
import lejos.utility.Delay;

public class GrindtheCrack extends RobotBehavior {

	private EV3UltrasonicSensor sonar;
	private SampleProvider average;
	private float[] values;

	private float P,I,D,distance,integral,last_error;
	
	private final float distance_max = 120.f;
	private final float distance_target = 95.f;
	private final float max_V = 0.75f;
	private final float reg_V = 0.5f;
	
	private final float travel_distance;
	
	private SampleProvider provider;
	
	public GrindtheCrack(Robot robot) {
		this(robot, Float.MAX_VALUE, null);
	}
	
	public GrindtheCrack(Robot robot, float travel_distance, ExitCallback callback) {
		this.robot = robot;
		this.travel_distance = travel_distance;
		this.exitCallback = callback;
		
		sonar = robot.getSensorUltrasonic();
		provider = sonar.getDistanceMode();
	
		values = new float[provider.sampleSize()];
		
		P = 0.01f;
		I = 0.f;
		D = 0.1f;
		
		last_error = Float.MAX_VALUE;
		integral   = 0.f;
	}

	public boolean takeControl() {
		
    	if(super.exit == true){
    		return false;
    	}
		
		return true;
	}

	public void suppress() {
		suppressed = true;
	}

	public void action() {
		suppressed = false;
		robot.getDifferentialPilot().reset();
		
		last_error = Float.MAX_VALUE;
		while(!exit && !suppressed){
			//sonar.fetchSample(values, 0);
			provider.fetchSample(values, 0);
			
			//sample.fetchSample(values, 0);
			float distance = values[0]*1000.0f;
			if(distance > distance_max)
				distance = distance_max;
			
			//error distance from -100 - 100
			float error = (float) ( distance_target - distance );
			
			if(Math.abs(error)<= 0.01){
				integral = 0.0f;
			}
			integral = integral + error;
			
			if(last_error == Float.MAX_VALUE){
				last_error = error;
			}
			
			float Turn   = P * error + I * integral + D * (error - last_error);
			
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
			
			/*float traveled = robot.getDifferentialPilot().getMovementIncrement();
			if (traveled > travel_distance) {
				exitCallback.exitArby();
			}*/
			
			/*LCD.drawString("Distance: " + distance, 1, 2);  
			LCD.drawString("Error: " + error, 1, 3);
			LCD.drawString("Turn: " + Turn, 1, 4);
			LCD.drawString("Power A: " + powerA, 1, 5);
			LCD.drawString("Power B: " + powerB, 1, 6);
			LCD.drawString("Travel : " + traveled, 1, 0);*/
			
		}
		this.robot.stopMotion();
	}
}
