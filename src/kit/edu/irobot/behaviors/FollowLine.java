package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;

import kit.edu.irobot.utils.Constants;
import lejos.hardware.lcd.LCD;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;
import lejos.hardware.Button;

public class FollowLine  implements Behavior {
	   private boolean exit = false; 
	   private boolean suppressed = false;
	   private Robot robot;
	   
	   public FollowLine(Robot robot) {
		   		this.robot = robot;
	   }
	   	   
	   public boolean takeControl() {
			if (this.exit == true) {
				return false;
			} 
			
			return true;
	   }

	   public void suppress() {
	      suppressed = true;
	   }

	   public void action() {
		    suppressed = false; 
			   
		    int button = -1; 
		    
		    LCD.clear();
		    LCD.drawString("Setup - Follow Line: ", 1, 0);
		    
		    SampleProvider sample = robot.getSensorLight().getRedMode();
		    
		    boolean setupDone = false; 
		    while(!setupDone)
		    {
				float[] values = new float[sample.sampleSize()];
				sample.fetchSample(values, 0);
		    	LCD.drawString("Light Vaulue: " + values[0], 1, 1);
				LCD.drawString("Black Value : " + Constants.LIGHT_VALUE_BLACK, 1, 2);
		    	LCD.drawString("White Value : " + Constants.LIGHT_VALUE_WHITE, 1, 3);
		    	LCD.drawString("Offset: " + Constants.PID_OFFSET, 1, 4);
		    	LCD.drawString("PRESS ENTER", 1, 5);
		    	
		    	button = Button.getButtons();
		    	
				if(button == Button.ID_UP)
				{
					Constants.LIGHT_VALUE_BLACK = values[0];
					Constants.PID_OFFSET = (Constants.LIGHT_VALUE_WHITE- Constants.LIGHT_VALUE_BLACK )*0.5f;
					Delay.msDelay(1000);
				}
				
				if(button == Button.ID_DOWN)
				{
					Constants.LIGHT_VALUE_WHITE = values[0];
					Constants.PID_OFFSET = (Constants.LIGHT_VALUE_WHITE- Constants.LIGHT_VALUE_BLACK )*0.5f;
					Delay.msDelay(1000);
				}
				
				if(button == Button.ID_ENTER)
				{
					setupDone = true;
				}
				
				Delay.msDelay(100);
		    }
		    
		    
		   		
			float integral   = 0;                          
			float lastError  = 0;                         
			float derivative = 0;   
			
			LCD.clear();
			LCD.drawString("Mode: follow line", 0, 0);
						
			while(!suppressed && !this.exit)
			{			
				float[] values = new float[sample.sampleSize()];
				sample.fetchSample(values, 0);
				
				float lightValue = values[0];
				float error = lightValue - Constants.PID_OFFSET;
				
				integral = integral + error; 

				
				
//				if (integral > 0)
//				{
//					integral = Math.min(integral, Constants.PID_KI_MAX);
//				}
//				else 
//				{
//					integral = Math.max(integral, -Constants.PID_KI_MAX);
//				}
				
//				if(Math.abs(error) )
//				{
//					integral = 0f;
//				}
				
				derivative = error - lastError; 
				
				float Turn = Constants.PID_KP * error + Constants.PID_KI * integral + Constants.PID_KD * derivative;
				
//				Turn = Math.min(Turn, 2 * Constants.TARGET_SPEED);
				
				float powerA = Constants.TARGET_SPEED +  Turn;         
				float powerB = Constants.TARGET_SPEED -  Turn;
				
				
				
				if (powerA > 0)
				{
					robot.getMotorLeft().setSpeed(powerA);
					LCD.drawString("Power A : " +  powerA, 1, 2);
					robot.getMotorLeft().forward();
				} else {
					robot.getMotorLeft().setSpeed(-1 * powerA);
					LCD.drawString("Power A : " + -1 * powerA, 1, 2);
					robot.getMotorLeft().backward();
				}
				
				if (powerB > 0)
				{
					robot.getMotorRight().setSpeed(powerB);
					LCD.drawString("Power B: "  +  powerB, 1, 3);
					robot.getMotorRight().forward();
				} else {
					robot.getMotorRight().setSpeed(-1 * powerB);
					LCD.drawString("Power B: "  +  -1 * powerB, 1, 3);
					robot.getMotorRight().backward();
				}                                  			
	
				LCD.drawString("Turn: " +  Turn, 1, 1);
				LCD.drawString("Integral: "  +  integral, 1, 4);
				LCD.drawString("KP: "  +  Constants.PID_KP, 1, 5);
				LCD.drawString("KI: "  +  Constants.PID_KI, 1, 6);
		
				
				lastError = error;
				
				button = Button.getButtons();
				if(button == Button.ID_UP)
				{
					Constants.PID_KP = Constants.PID_KP + 10f;
					Delay.msDelay(100);
					button = -1;
				}
				
				if(button == Button.ID_DOWN)
				{
					Constants.PID_KP = Constants.PID_KP - 10f;
					Delay.msDelay(100);
					button = -1;
				}
				
				if(button == Button.ID_RIGHT)
				{
					Constants.PID_KI = Constants.PID_KI + 0.1f;
					Delay.msDelay(100);
					button = -1;
				}
				
				if(button == Button.ID_LEFT)
				{
					Constants.PID_KI = Constants.PID_KI - 0.1f;
					Delay.msDelay(100);
					button = -1;
				}
			}
			
			robot.stopMotion();
	   }
	   

	   
	   public void terminate(){
		   this.exit = true;
	   }
}