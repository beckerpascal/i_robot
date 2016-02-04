package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import kit.edu.irobot.utils.Constants;
import lejos.hardware.lcd.LCD;
import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Behavior;

public class FollowLine  implements Behavior {
	   private boolean exit = false; 
	   private boolean suppressed = false;
	   private Robot robot;
	   
	   public FollowLine(Robot robot) {
		   		this.robot = robot;
	   }
	   	   
	   public boolean takeControl() {
		   if(this.exit == true) 
		   {
			   return false;
		   }
		   
		   return true;
	   }

	   public void suppress() {
	      suppressed = true;
	   }

	   public void action() {
		    suppressed = false; 
		    LCD.clear();
		    LCD.drawString("Running Follow Line: ", 1, 0);
		    
		    SampleProvider sample = robot.getSensorLight().getRedMode();
					
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
				derivative = error - lastError; 
				
				float Turn = Constants.PID_KP * error + Constants.PID_KI * integral + Constants.PID_KD * derivative;
				
				float powerA = Constants.TARGET_SPEED +  Turn;         
				float powerB = Constants.TARGET_SPEED -  Turn;         
				
				robot.getMotorLeft().setSpeed(powerA);
				robot.getMotorRight().setSpeed(powerB);
				
				robot.moveRobotForward();
				
				lastError = error;
			}
			
			robot.stopMotion();
	   }
	   
	   public void terminate(){
		   this.exit = true;
	   }
}