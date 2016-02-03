package kit.edu.irobot.examples;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class TouchSensor {
	public static void main(String[] args) 
    {
		EV3TouchSensor touch = new EV3TouchSensor(SensorPort.S2);
		touch.getTouchMode();
		
		LCD.clear();
		while (!Button.ESCAPE.isDown()) {	    	
	    	float[] values = new float[touch.sampleSize()];
	    	touch.fetchSample(values, 0);
	    	for (int i = 0; i < touch.sampleSize(); i++) {
	    		LCD.drawString("Dist: " + values[i], 1,4 + i);
			}
	    	Delay.msDelay(500);		
		}
		
    }

};
