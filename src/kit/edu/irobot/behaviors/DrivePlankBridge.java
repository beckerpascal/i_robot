package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
public class DrivePlankBridge extends RobotBehavior {
	private EV3ColorSensor color = null;
	private EV3UltrasonicSensor sonar;
	
	private float[] lightValue;
	private SampleProvider provider;
	private SampleProvider lightProv = null;

	private float[] values;
	private float distance;
	private float distance_max = 150.f;
	

	private EV3LargeRegulatedMotor motor_left, motor_right;
	
	public DrivePlankBridge(EV3ColorSensor color,EV3UltrasonicSensor sonar,EV3LargeRegulatedMotor left, EV3LargeRegulatedMotor right) {

		this.color = color;
		this.sonar = sonar;
		this.motor_left  = left;
		this.motor_right = right;
		
		this.lightProv = color.getRedMode();
		this.lightValue = new float[this.lightProv.sampleSize()];
		
		provider = sonar.getDistanceMode();
		values = new float[provider.sampleSize()];
	}

	public boolean takeControl() {
		if (this.exit == true) {
			return false;
		}	
		provider.fetchSample(values, 0);
		distance = values[0]*1000.0f;
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
		
		LCD.clear();
		LCD.drawString("drive forward...", 0, 0);

		this.setRobotSpeed(1.0,motor_left,motor_right);
		motor_left.forward();
		motor_right.forward();
		
		this.lightProv.fetchSample(lightValue, 0);
		while(!exit && !suppressed && (lightValue[0] < 0.8f) ){
			Delay.msDelay(100);
			this.lightProv.fetchSample(lightValue, 0);
		}

		motor_left.stop();
		motor_right.stop();
	}
}
