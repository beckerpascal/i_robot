package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.subsumption.Behavior;

public class HitObstacle implements Behavior {
	   private boolean suppressed = false;
	   public Robot robot;
	   
	   private EV3TouchSensor touch;
	   private EV3UltrasonicSensor sonar;

	   private float[] sonar_samples;
	   private float[] touch_samples;
	   
	   public HitObstacle(){
		   sonar.getDistanceMode();
		   touch.getTouchMode();
		   sonar_samples = new float[sonar.sampleSize()];
		   touch_samples = new float[touch.sampleSize()];
	   }
	   
	   public boolean takeControl() {
	    	sonar.fetchSample(sonar_samples, 0);
	    	touch.fetchSample(touch_samples, 0);
	    	if(sonar_samples[0]< 0.05 || touch_samples[0] < 1.0 )
	    		return true;
	    	return false;
	   }

	   public void suppress() {
		   if(sonar_samples[0]> 0.05 && touch_samples[0] > 1.0 )
			   suppressed =  true;
	   }

	   public void action() {
	     suppressed = false;
	     
	     robot.setRobotSpeed(0.1f);
	     
	     robot.rotateLeft();
	     while( !suppressed )
	        Thread.yield();
	    
	     robot.stopMotion();
	     
	   }
}
