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
	EV3LargeRegulatedMotor motorLeft, motorRight;

	/*
	public EV3UltrasonicSensor  sensorDistance;
	public EV3ColorSensor 		sensorLight; 
	public EV3TouchSensor 		sensorTouch_1;
	public EV3TouchSensor 		sensorTouch_2;
	*/
	
	final int BACKWARD = -1;
	final int FORWARD  =  1; 
	
	int direction = FORWARD;
	private Robot() {

		motorLeft = new EV3LargeRegulatedMotor(Constants.LEFT_MOTOR);
		motorRight = new EV3LargeRegulatedMotor(Constants.RIGHT_MOTOR);
		
		/*
		sensorDistance = new EV3UltrasonicSensor(SensorPort.S1);
		sensorLight    = new EV3ColorSensor(SensorPort.S2);
		sensorTouch_1  = new EV3TouchSensor(SensorPort.S3);
		sensorTouch_2  = new EV3TouchSensor(SensorPort.S4);
		// TODO: Init all other sensors
		*/
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
		   float speed = this.motorLeft.getRotationSpeed();
		   float rotation_time = angle/speed;
		   
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
