package kit.edu.irobot.robot;

import kit.edu.irobot.utils.Constants;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.utility.Delay;

/**
 * Class for handling all things done with the robot like controlling motors,
 * reading sensors or communicate with bluetooth or wifi. Implemented as
 * singleton to be able to get only one instance.
 * 
 * @author Pascal Becker
 *
 */

public class Robot {
	private static Robot instance = null;
	private EV3LargeRegulatedMotor motorLeft;
	private EV3LargeRegulatedMotor motorRight;

	private EV3UltrasonicSensor  sensorDistance;
	private EV3ColorSensor 		 sensorLight; 
	private EV3TouchSensor 		 sensorTouch_1;
	private EV3TouchSensor 		 sensorTouch_2;
	
	final int BACKWARD = -1;
	final int FORWARD  =  1; 
	
	int direction = FORWARD;
	private Robot(){

		motorLeft = new EV3LargeRegulatedMotor(Constants.LEFT_MOTOR);
		motorRight = new EV3LargeRegulatedMotor(Constants.RIGHT_MOTOR);
		
		if(Constants.LIGHT_SENSOR != null)sensorLight       = new EV3ColorSensor(Constants.LIGHT_SENSOR);
		else 							  sensorLight       = null;
		
		if(Constants.DISTANCE_SENSOR != null)sensorDistance = new EV3UltrasonicSensor(Constants.LIGHT_SENSOR);
		else 							     sensorDistance = null;
		
		if(Constants.TOUCH_FRONT_SENSOR != null)sensorTouch_1   = new EV3TouchSensor(Constants.TOUCH_FRONT_SENSOR);
		else 							    sensorTouch_1   = null;

		if(Constants.TOUCH_BACK_SENSOR != null)sensorTouch_2   = new EV3TouchSensor(Constants.TOUCH_BACK_SENSOR);
		else 							    sensorTouch_2   = null;
		
	}

	public EV3UltrasonicSensor getSensorDistance(){
		return sensorDistance;
	}
	
	public EV3ColorSensor getSensorLight(){
		return sensorLight;
	}
	
	public EV3TouchSensor getSensorTouchFront(){
		return sensorTouch_1;
	}
	
	public EV3TouchSensor getSensorTouchBack(){
		return sensorTouch_2;
	}
	
	public EV3LargeRegulatedMotor getMotorRight(){
		return motorRight;
	}
	
	public static Robot getInstance() {
		if (instance == null) {
			instance = new Robot();
		}
		return instance;
	}

	/**
	    * Uses the initialized motors and drives with the given speed.
	    * @param motorA 
	    * @param motorB
	    */
	   public void driveWithSpeed(float motorLeft, float motorRight){
		   this.motorLeft.setSpeed(motorLeft);
		   this.motorRight.setSpeed(motorRight);
	   }
	   
	   //speed = [0-1]
	   public void setSpeed(float speed){
		   speed = Math.abs(speed);
		   speed = Math.min(speed, 1.0f);
		   
		   float maxspeed_left= this.motorLeft.getMaxSpeed();
		   float maxspeed_right = this.motorLeft.getMaxSpeed();
		   
		   float new_speed = Math.min(maxspeed_left, maxspeed_right)*speed;
		   
		   driveWithSpeed(new_speed,new_speed);
	   }
	   
	   public void moveForward(){
		   if(direction == BACKWARD){
			 this.motorLeft.backward();
			 this.motorRight.backward();     
		   }else{
			 this.motorLeft.forward();
			 this.motorRight.forward(); 
		   }
	   }

	   public void moveBackward(){
		   if(direction == BACKWARD){
			 this.motorLeft.forward();
			 this.motorRight.forward();     
		   }else{
			 this.motorLeft.backward();
			 this.motorRight.backward(); 
		   }
	   }
	   public void rotateLeft(){
		   if(direction == BACKWARD){
		      this.motorLeft.backward();
		      this.motorRight.forward(); 
		   }else{
			  this.motorLeft.forward();
			  this.motorRight.backward();     
		   }
	   }

	   public void rotateRight(){
		   if(direction == BACKWARD){
				  this.motorLeft.forward();
				  this.motorRight.backward();
		   }else{     
			      this.motorLeft.backward();
			      this.motorRight.forward(); 
		   }
	   }
	   
	   public void stopMotion(){
		   this.motorLeft.stop();
		   this.motorRight.stop();
	   }
	   
	   public void rotate(float angle){
		   float ang_vel = this.motorLeft.getSpeed();
		   float rotation_time = angle/ang_vel;
		   
		   if(angle < 0.f){
			   rotateRight();
		   }else{
			   rotateLeft();
		   }
		   Delay.msDelay((int)(rotation_time* 1000.f));
		   stopMotion();		   
	   }
	   
	   public void turnAround(){
		   rotate(180.f);
	   }
}
