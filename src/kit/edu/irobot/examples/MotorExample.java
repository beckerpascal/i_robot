package kit.edu.irobot.examples;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.utility.Delay;

public class MotorExample {
 
      public static void main(String[] args) 
      {
           LCD.drawString("MotorExample", 0, 0);
           Button.waitForAnyPress();
           Motor.A.setSpeed(7200);
           Motor.B.setSpeed(720);
           Motor.A.forward();
           Motor.B.forward();

//           LCD.clear();
//           Delay.msDelay(2000);
//           LCD.drawInt(Motor.A.getTachoCount(),0,0);
//           Motor.A.stop();
//           LCD.drawInt(Motor.A.getTachoCount(),0,1);
//           Motor.A.backward();
//           while (Motor.A.getTachoCount()>0); 
//           LCD.drawInt(Motor.A.getTachoCount(),0,2);
//           Motor.A.stop();
//           LCD.drawInt(Motor.A.getTachoCount(),0,3);
           Button.waitForAnyPress();
      }
 
 }