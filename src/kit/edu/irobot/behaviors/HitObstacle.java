package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class HitObstacle implements Behavior {
	   private boolean suppressed = false;
	   public Robot robot;
	   
	   private EV3TouchSensor touch;

	   private float[] touch_samples;
	   
	   public HitObstacle(Robot robot){
		   
		   this.robot = robot;
		   
		   touch = robot.getSensorTouchFront();
		   touch.getTouchMode();
		   touch_samples = new float[touch.sampleSize()];
	   }
	   
	   public boolean takeControl() {
	    	touch.fetchSample(touch_samples, 0);
	    	if(touch_samples[0] == 1.0 )
	    		return true;
	    	return false;
	   }

	   public void suppress() {
		   suppressed = true;
	   }

	   public void action() {
	     suppressed = false;

	     robot.setRobotSpeed(0.2f);
	     robot.moveRobotBackward();
	     Delay.msDelay(1000);
	     robot.stopMotion();
	     
	     robot.setRobotSpeed(0.2f);
	     robot.rotateRobot(-90.0f);
	     while( !suppressed )
	        Thread.yield();
	    
	     robot.stopMotion();
	     
	   }
}
