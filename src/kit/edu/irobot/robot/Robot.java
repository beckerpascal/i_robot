package kit.edu.irobot.robot;

import kit.edu.irobot.utils.Constants;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
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
	private EV3LargeRegulatedMotor  motorLeft;
	private EV3LargeRegulatedMotor  motorRight;
	private EV3MediumRegulatedMotor motorSpecial;

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
		
		if(Constants.SPECIAL_MOTOR != null)motorSpecial = new EV3MediumRegulatedMotor(Constants.SPECIAL_MOTOR);
		else 							   motorSpecial = null;
		
		if(Constants.LIGHT_SENSOR != null)sensorLight       = new EV3ColorSensor(Constants.LIGHT_SENSOR);
		else 							  sensorLight       = null;
		
		if(Constants.DISTANCE_SENSOR != null)sensorDistance = new EV3UltrasonicSensor(Constants.DISTANCE_SENSOR);
		else 							     sensorDistance = null;
		
		if(Constants.TOUCH_FRONT_SENSOR != null)sensorTouch_1   = new EV3TouchSensor(Constants.TOUCH_FRONT_SENSOR);
		else 							    sensorTouch_1   = null;

		if(Constants.TOUCH_BACK_SENSOR != null)sensorTouch_2   = new EV3TouchSensor(Constants.TOUCH_BACK_SENSOR);
		else 							    sensorTouch_2   = null;
		
	}

	public EV3MediumRegulatedMotor getMotorSpecial(){
		return motorSpecial;
	}
	
	public EV3UltrasonicSensor getSensorUltrasonic(){
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
	
	public EV3LargeRegulatedMotor getMotorLeft(){
		return motorLeft;
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
	
	public void setLEDPattern(int pattern){
		Button.LEDPattern(pattern);
		
	}

	/**
	    * Uses the initialized motors and drives with the given speed.
	    * @param motorA 
	    * @param motorB
	    */
	   public void driveWithSpeed(double motorLeft, double motorRight){
		   this.motorLeft.setSpeed((float) motorLeft);
		   this.motorRight.setSpeed((float) motorRight);
		   
		   this.moveRobotForward();
	   }
	   
	   public void setMotorSpeed(float speed,RegulatedMotor motor){
		   speed = Math.abs(speed);
		   speed = Math.min(speed, 1.0f);

		   float max_speed= motor.getMaxSpeed();
		   float new_speed = max_speed*speed;
		   motor.setSpeed((int)new_speed);
	   }
	   //speed = [0-1]
	   public void setRobotSpeed(float speed){
		   setMotorSpeed(speed,this.motorLeft);
		   setMotorSpeed(speed,this.motorRight);
	   }
	   
	   public void moveRobotForward(){
		   //RegulatedMotor[] motors = {this.motorRight};
		   //this.motorLeft.synchronizeWith(motors);
		   //this.motorLeft.startSynchronization();
		   if(direction == BACKWARD){
			 this.motorLeft.backward();
			 this.motorRight.backward();     
		   }else{
			 this.motorLeft.forward();
			 this.motorRight.forward(); 
		   }
	   }

	   public void moveRobotBackward(){
		   if(direction == BACKWARD){
			 this.motorLeft.forward();
			 this.motorRight.forward();     
		   }else{
			 this.motorLeft.backward();
			 this.motorRight.backward(); 
		   }
	   }
	   public void rotateRobotLeft(){
		   if(direction == BACKWARD){
		      this.motorLeft.backward();
		      this.motorRight.forward(); 
		   }else{
			  this.motorLeft.forward();
			  this.motorRight.backward();     
		   }
	   }

	   public void rotateRobotRight(){
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
		   
		   this.motorLeft.endSynchronization();
	   }
	   
	   public void rotateRobot(float angle){
		   float ang_vel = this.motorLeft.getSpeed();
		   float rotation_time = angle/ang_vel;
		   
		   if(angle < 0.f){
			   rotateRobotRight();
		   }else{
			   rotateRobotLeft();
		   }
		   Delay.msDelay((int)(rotation_time* 1000.f));
		   stopMotion();		   
	   }
	   
	   public void turnAround(){
		   rotateRobot(180.f);}
	   
	   public void writeBehaviorNameToDisplay(String name){
		   LCD.drawString("I am in " + name, 1, 5);
	   }
}
