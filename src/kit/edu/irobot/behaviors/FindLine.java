package kit.edu.irobot.behaviors;

import kit.edu.irobot.solver.BaseStageSolver.ExitCallback;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import kit.edu.irobot.utils.Constants;
import kit.edu.irobot.utils.Buffer;
import kit.edu.irobot.utils.UnregulatedPilot;

public class FindLine  extends BaseBehavior {
	private static final boolean DEBUG = false;

	private static final int degree_to_right = -120; 
	private static final int degree_to_left  = 120;

	private final UnregulatedPilot pilot;
	private final SampleProvider sample;
	private final float[] values;

	private final Buffer buffer = new Buffer(140);

	public FindLine(EV3ColorSensor color, UnregulatedPilot pilot, ExitCallback callback) {
		this.pilot = pilot;
		super.exitCallback = callback;
		sample = color.getRedMode();
		values = new float[1]; //safer to set size
	}

	public boolean takeControl() {
		if (this.exit == true) {
			return false;
		} 

		sample.fetchSample(values, 0);

		this.buffer.add(values[0]);

		if (DEBUG) LCD.drawString("FIL: " + this.buffer.average(), 1, 6);

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

		if (DEBUG) LCD.drawString("Find Line", 1, 5);


		int robot_speed = 90; 
		boolean foundLine  = false; 


		//	    while(!suppressed && !exit && !foundLine)
		//	    {	
		pilot.reset();

		// Turn right 
		pilot.setPower(robot_speed, -robot_speed);
		while(pilot.getAngleIncrement() >= degree_to_right && !foundLine && !exit && !suppressed)
		{
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
			if (DEBUG) LCD.drawString("Found Line !", 1, 5);
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