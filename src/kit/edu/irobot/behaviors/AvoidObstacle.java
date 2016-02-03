package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.subsumption.Behavior;

public class AvoidObstacle implements Behavior {
	   private boolean suppressed = false;
	   public Robot robot;
	   
	   private EV3UltrasonicSensor sonar;
	   
	   private float[] values;
	   
	   public AvoidObstacle(){
		   robot = Robot.getInstance();
	   }
	   
	   public boolean takeControl() {
	    	sonar.fetchSample(values, 0);
	    	if(values[0]< 0.2)
	    		return true;
	    	return false;
	   }

	   public void suppress() {
		  sonar.fetchSample(values, 0);
	      if(values[0]> 0.2)
	    	suppressed = true;
	      else
	    	suppressed = false;
	   }

	   public void action() {
	     suppressed = false;
	     
	     robot.setSpeed(0.1f);
	     
	     robot.rotateLeft();
	     while( !suppressed )
	        Thread.yield();
	    
	     robot.stopMotion();
	     
	   }
}
