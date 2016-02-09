package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import lejos.hardware.lcd.LCD;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;
import lejos.robotics.subsumption.Behavior;
import kit.edu.irobot.utils.Constants;
import kit.edu.irobot.utils.Buffer;

public class FindLine  implements Behavior {
	private boolean exit = false; 

	private boolean suppressed = false;
	private Robot robot;
	
	private Buffer buffer = new Buffer(100);

	public FindLine(Robot robot) {
		this.robot = robot;
	}

	public boolean takeControl() {
		if (this.exit == true) {
			return false;
		} 
		
		SampleProvider sample =  robot.getSensorLight().getRedMode();
		float[] values = new float[sample.sampleSize()];
		
		this.buffer.add(values[0]);
		
		LCD.drawString("FIL A: " + buffer.average(), 1, 6);
		
		if (buffer.average() < Constants.PID_OFFSET) {
			return true;
		}

		return false;
	}

	public void suppress() {
		suppressed = true;
	}

	public void action() {
		suppressed = false;
		
		LCD.drawString("Find Line", 1, 5);
	    
	    SampleProvider sample = robot.getSensorLight().getRedMode();
	    
	    int degree_to_right = -90; 
	    int degree_to_left  = 180;
	    
	    float[] values; 
	    
	    boolean foundLine  = false; 
	    
	    boolean turn_left  = false;
	    boolean turn_right = true; 
	    
	    
	    while(!suppressed && !exit && !foundLine)
	    {	
	    	// Turn right - 90 degrees 
	    	if(turn_right && !turn_left && !foundLine) { 
	    		robot.getPilot().rotate(degree_to_right, false);
	    		
	    		if(!robot.getPilot().isMoving()) {
	    			turn_right = false; 
	    			turn_left = true;
	    		}
	    	}
	    	
	    	// Turn left - 90 degrees 
	    	if(!turn_right && turn_left && !foundLine) { 
	    		robot.getPilot().rotate(degree_to_left, false);
	    		
	    		if(!robot.getPilot().isMoving()) {
	    			turn_right = false; 
	    			turn_left = false;
	    		}
	    	}
	    	
	    	// Move forward
	    	if(!turn_right && !turn_left && !foundLine) {
	    		robot.getPilot().travel(10);
	    		
	    		if(!robot.getPilot().isMoving()) {
	    			turn_right = true; 
	    			turn_left = false;
	    		}
	    	}
	    	
	    	// Search for line 
			values = new float[sample.sampleSize()];
			sample.fetchSample(values, 0);
			
			if(values[0] > Constants.PID_OFFSET){
				robot.getPilot().stop();
				LCD.drawString("Found Line !", 1, 5);
				foundLine = true; 
			}
	    }
	}

	public void terminate() {
		this.exit = true;
	}
}