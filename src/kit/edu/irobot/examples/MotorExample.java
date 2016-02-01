package kit.edu.irobot.examples;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class MotorExample {
 
      public static void main(String[] args) 
      {
          EV3LargeRegulatedMotor motorA = new EV3LargeRegulatedMotor(MotorPort.A);
          EV3LargeRegulatedMotor motorB = new EV3LargeRegulatedMotor(MotorPort.B);
         
    	  
           LCD.drawString("MotorExample", 0, 0);
           Button.waitForAnyPress();

           
           RegulatedMotor[] list = {motorB};
           
           motorA.synchronizeWith(list);
           motorA.startSynchronization();
           motorB.startSynchronization();
           
           motorA.setSpeed(motorA.getMaxSpeed());
           LCD.drawString("Max Speed A: " + motorA.getMaxSpeed()/2, 0, 1);
           motorA.forward();
           motorB.setSpeed(motorB.getMaxSpeed());
           LCD.drawString("Max Speed B: " + motorB.getMaxSpeed()/2, 0, 2);
           motorB.forward();

           Delay.msDelay(4000);
           
           motorA.endSynchronization();

           Delay.msDelay(4000);
           
           LCD.drawString("Motor blocked! ", 0, 2);

      }
 
 }