package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import lejos.hardware.lcd.LCD;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;
import lejos.robotics.subsumption.Behavior;
import kit.edu.irobot.utils.Constants;

public class FindLine  implements Behavior {
	private boolean exit = false; 

	private boolean suppressed = false;
	private Robot robot;

	public FindLine(Robot robot) {
		this.robot = robot;
	}

	public boolean takeControl() {
		if (this.exit == true) {
			return false;
		} 
		
		SampleProvider average = new MeanFilter(robot.getSensorLight().getRedMode(), 200);
		float[] values = new float[average.sampleSize()];
		
		if (values[0] < Constants.PID_OFFSET) {
			return false;
		}

		return false;
	}

	public void suppress() {
		suppressed = true;
	}

	public void action() {
		suppressed = false;
		
	    LCD.clear();
	    LCD.drawString("Mode: Find Line", 1, 0);
	    
	    SampleProvider sample = robot.getSensorLight().getRedMode();
	    
	    boolean foundLine = false; 
	    
	    int degree_to_right = 90; 
	    int degree_to_left  = -90;
	    
	    while(!suppressed && !foundLine)
	    {
	    	float[] values = new float[sample.sampleSize()];
			sample.fetchSample(values, 0);
			
			if(values[0] < Constants.PID_OFFSET)
			{
				robot.getPilot().stop();
				foundLine = true; 
			}
			
			for (float degree = 0; degree < degree_to_right; degree = degree + 5)
			{
		    	robot.getPilot().rotate(degree, false);
				
				values = new float[sample.sampleSize()];
				sample.fetchSample(values, 0);
				
				if(values[0] < Constants.PID_OFFSET)
				{
					robot.getPilot().stop();
					foundLine = true; 
				}
			}
			
			for (float degree = degree_to_right; degree < degree_to_left; degree = degree + 5)
			{
				robot.getPilot().rotate(degree, false);
		    	
				values = new float[sample.sampleSize()];
				sample.fetchSample(values, 0);
				
				if(values[0] < Constants.PID_OFFSET)
				{
					robot.getPilot().stop();
					foundLine = true; 
				}
			}
			
			if(!foundLine)
			{
				robot.getPilot().travel(10);
			}
	    }
	}

	public void terminate() {
		this.exit = true;
	}
}