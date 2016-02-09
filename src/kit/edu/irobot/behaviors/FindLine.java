package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import kit.edu.irobot.solver.StageSolver.ExitCallback;
import lejos.hardware.lcd.LCD;
import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Behavior;
import kit.edu.irobot.utils.Constants;
import kit.edu.irobot.utils.Buffer;
import kit.edu.irobot.utils.UnregulatedPilot;

public class FindLine  extends RobotBehavior {
	private boolean exit = false; 

	private boolean suppressed = false;
	private Robot robot;
	
	private Buffer buffer = new Buffer(200);

	public FindLine(Robot robot, ExitCallback callback) {
		this.robot = robot;
		super.exitCallback = callback;
	}

	public boolean takeControl() {
		if (this.exit == true) {
			return false;
		} 
		
		SampleProvider sample =  robot.getSensorLight().getRedMode();
		float[] values = new float[sample.sampleSize()];
		sample.fetchSample(values, 0);
		
		this.buffer.add(values[0]);
		
		LCD.drawString("FIL: " + this.buffer.average(), 1, 6);
		
		if (buffer.average() < 0.3f) {
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
	    UnregulatedPilot pilot =  robot.getUnregulatedPilot();
	    
	    int degree_to_right = -110; 
	    int degree_to_left  = 110;
	    
	    int robot_speed = 90; 
	    
	    float[] values; 
	    boolean foundLine  = false; 
	    
	    
//	    while(!suppressed && !exit && !foundLine)
//	    {	
	    	pilot.reset();
	    	
	    	// Turn right 
	    	pilot.setPower(robot_speed, -robot_speed);
	    	while(pilot.getAngleIncrement() >= degree_to_right && !foundLine && !exit && !suppressed)
	    	{
				values = new float[sample.sampleSize()];
				sample.fetchSample(values, 0);
				
				if(values[0] > Constants.PID_OFFSET){
					pilot.stop();
					foundLine = true; 
				}
	    	}
	    	pilot.stop();
	
	    	// Turn left
	    	pilot.setPower(-robot_speed, robot_speed);
	    	while(pilot.getAngleIncrement() <= degree_to_left && !foundLine && !exit && !suppressed)
	    	{
				values = new float[sample.sampleSize()];
				sample.fetchSample(values, 0);
				
				if(values[0] > Constants.PID_OFFSET){
					pilot.stop();
					foundLine = true; 
				}
	    	}
	    	pilot.stop();
	    	
	    	// Turn back. 
	    	pilot.setPower(robot_speed, -robot_speed);
	    	while(pilot.getAngleIncrement() > 0 && !foundLine && !exit && !suppressed) {}
	    	pilot.stop();
	    	pilot.reset();
	    	
	    
	    	
	    	// Distance in mm 
//	    	pilot.setPower(robot_speed, robot_speed);
//	    	float distance =  100;
//	    	while(pilot.getDistanceIncrement() < distance && !foundLine && !exit && !suppressed) {
//		    	// Search for line 
//				values = new float[sample.sampleSize()];
//				sample.fetchSample(values, 0);
//				
//				if(values[0] > Constants.PID_OFFSET){
//					pilot.stop();
//					foundLine = true; 
//				}
//	    	}
//	    	pilot.stop();
//	    	pilot.reset();
	    	
	    	if(foundLine) {
	    		this.buffer.reset(1.f);
	    		LCD.drawString("Found Line !", 1, 5);
	    	}
	    	else 
	    	{
	    		pilot.stop();
	    		pilot.reset();
	    		super.requestArbitratorExit();
	    	}
//	    }
	}
	
	public void terminate() {
		this.exit = true;
	}
}