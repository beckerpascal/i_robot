package kit.edu.irobot.examples;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class UltrasonicExampleTest {

	public static void main(String[] args) {
		EV3UltrasonicSensor sonic = new EV3UltrasonicSensor(SensorPort.S1);
		sonic.enable();
		
		LCD.clear();
		LCD.drawString("Modes: " + sonic.getAvailableModes().toString(), 1, 1);
		LCD.drawString("Cur Mode: " + sonic.getCurrentMode(), 1, 2);
		LCD.drawString("Sample Size: " + sonic.sampleSize(), 1, 2);
 
		SampleProvider sample = sonic.getDistanceMode();
		
	    while (!Button.ESCAPE.isDown()) {	    	
	    	float[] values = new float[sample.sampleSize()];
	    	sample.fetchSample(values, 0);
	    	for (int i = 0; i < sample.sampleSize(); i++) {
	    		LCD.drawString("Dist: " + values[i], 1,4 + i);
			}
	    	Delay.msDelay(500);			
	    }
	    
	    sonic.disable();

	}

}
