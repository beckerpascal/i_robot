package kit.edu.irobot.examples;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;

public class MotorDegreesExample {
 
      public static void main(String[] args) 
      {
          	EV3LargeRegulatedMotor motorA = new EV3LargeRegulatedMotor(MotorPort.A);
          	motorA.setSpeed(20);
          	int degrees = 90;
    	  	
          	LCD.drawString("MotorDegreesExample", 0, 0);
            Button.waitForAnyPress();
            LCD.drawString("Start rotating for " + degrees + " degrees! ", 0, 2);
	
            motorA.rotate(degrees);
            LCD.drawString("Motor rotated " + degrees + " degrees! ", 0, 3);

      }
 
 }