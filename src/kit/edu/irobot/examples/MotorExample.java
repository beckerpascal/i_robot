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
           LCD.drawString("MotorExample", 0, 0);
           Button.waitForAnyPress();
           EV3LargeRegulatedMotor motorA = new EV3LargeRegulatedMotor(MotorPort.A);
           EV3LargeRegulatedMotor motorB = new EV3LargeRegulatedMotor(MotorPort.B);
           
           RegulatedMotor[] list = {motorB};
           
           motorA.synchronizeWith(list);
           motorA.startSynchronization();
           motorB.startSynchronization();
           
           motorA.setSpeed(motorA.getMaxSpeed());
           LCD.drawString("Max Speed A: " + motorA.getMaxSpeed(), 0, 1);
           motorA.backward();
           motorB.setSpeed(motorB.getMaxSpeed());
           LCD.drawString("Max Speed B: " + motorB.getMaxSpeed(), 0, 2);
           motorB.backward();

           Delay.msDelay(4000);
           
           motorA.endSynchronization();

           Delay.msDelay(4000);

           
           Button.waitForAnyPress();
      }
 
 }