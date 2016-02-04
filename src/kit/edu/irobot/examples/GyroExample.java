package kit.edu.irobot.examples;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;

public class GyroExample {

	public static void main(String[] args) {
		EV3GyroSensor gyro = new EV3GyroSensor(SensorPort.S1);
		float[] values = new float[gyro.sampleSize()];
		gyro.fetchSample(values, 0);
		
		LCD.drawString("GyroExample", 0, 0);
		Button.waitForAnyPress();
		LCD.drawString("Sensing...", 0, 1);
		LCD.drawString(values.toString(),0, 2);

		
		Button.waitForAnyPress();
	}

}
