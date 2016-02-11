package kit.edu.irobot.behaviors;

import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class DrivePlankBridge extends BaseBehavior {
	private static final boolean DEBUG = false;
	
	private static final float distance_max = 150.f;
	
	private final float[] lights;
	private final SampleProvider distanceProvider;
	private final SampleProvider lightProvider;

	private final float[] distances;
	private float distance;
	

	private EV3LargeRegulatedMotor motor_left, motor_right;
	
	public DrivePlankBridge(EV3ColorSensor color,EV3UltrasonicSensor sonar,EV3LargeRegulatedMotor left, EV3LargeRegulatedMotor right) {
		this.motor_left  = left;
		this.motor_right = right;
		
		lightProvider = color.getRedMode();
		lights = new float[lightProvider.sampleSize()];
		
		distanceProvider = sonar.getDistanceMode();
		distances = new float[distanceProvider.sampleSize()];
	}

	public boolean takeControl() {
		if (this.exit == true) {
			return false;
		}	
		distanceProvider.fetchSample(distances, 0);
		distance = distances[0]*1000.0f;
    	if(distance > distance_max){
    		return true;
    	}
		
		return false;
	}

	public void suppress() {
		suppressed = true;
	}
	

	public void action() {
		suppressed = false;
		
		if (DEBUG) {
			LCD.clear();
			LCD.drawString("drive forward...", 0, 0);
		}

		this.setRobotSpeed(1.0f, motor_left, motor_right);
		motor_left.forward();
		motor_right.forward();
		
		this.lightProvider.fetchSample(lights, 0);
		while(!exit && !suppressed && (lights[0] < 0.8f) ){
			Delay.msDelay(100);
			this.lightProvider.fetchSample(lights, 0);
		}

		motor_left.stop();
		motor_right.stop();
	}
}
