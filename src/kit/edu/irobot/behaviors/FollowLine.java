package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;

import kit.edu.irobot.utils.Constants;
import kit.edu.irobot.utils.Buffer;
import lejos.hardware.lcd.LCD;
import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;
import lejos.hardware.Button;
import kit.edu.irobot.utils.UnregulatedPilot;

public class FollowLine extends RobotBehavior {
	   private boolean exit = false; 
	   private boolean suppressed = false;
	   private Robot robot;
	   
		private float integral,last_error;
		
		private static final float IntegralBackoffLine = 0.2f;
		private static final float IntegralBackoffBlack = 1.f;
		private static final float IntegralDamping = 0.3f;
		private static final float IntegralFactorLine = 8.f;
		private static final float IntegralFactorBlack = 1.f;
		
		// SEHR GUT: V = 0.65, P = 0.006
		private static final float P = 0.005f;//0.002f;
		private static final float I = 0.f;//0.0005f;
		private static final float D = 0.f;//0.010f; 
		
		private final float max_V = 0.45f;
		
	
		private SampleProvider provider;
		private UnregulatedPilot pilot;
	   
	   public FollowLine(Robot robot) {
		   		this.robot = robot;
		   		this.pilot = robot.getUnregulatedPilot();
	   }
	   	   
	   public boolean takeControl() {
			if (this.exit == true) {
				return false;
			} 
						
//			SampleProvider sample = robot.getSensorLight().getRedMode(); 
//			float[] values = new float[sample.sampleSize()];
//			
//			this.buffer.add(values[0]);
//			
//			LCD.drawString("FOL A" + this.buffer.average(), 1, 4);
//			
//			if (this.buffer.average() > Constants.PID_OFFSET) {
//				return true;
//			}
//			
			return true;
	   }

	   public void suppress() {	   
		  LCD.clear();
	      suppressed = true;
	   }

	   public void action() {
		    suppressed = false; 
			LCD.clear();
		    		   
//		    int button = -1; 
		    
//		    LCD.clear();
//		    LCD.drawString("Setup - Follow Line: ", 1, 0);
		    
//		    SampleProvider sample = robot.getSensorLight().getRedMode();
		    
//		    boolean setupDone = false; 
//		    while(!setupDone)
//		    {
//				float[] values = new float[sample.sampleSize()];
//				sample.fetchSample(values, 0);
//		    	LCD.drawString("Light Vaulue: " + values[0], 1, 1);
//				LCD.drawString("Black Value : " + Constants.LIGHT_VALUE_BLACK, 1, 2);
//		    	LCD.drawString("White Value : " + Constants.LIGHT_VALUE_WHITE, 1, 3);
//		    	LCD.drawString("Offset: " + Constants.PID_OFFSET, 1, 4);
//		    	LCD.drawString("PRESS ENTER", 1, 5);
//		    	
//		    	button = Button.getButtons();
//		    	
//				if(button == Button.ID_UP)
//				{
//					Constants.LIGHT_VALUE_BLACK = values[0];
//					Constants.PID_OFFSET = (Constants.LIGHT_VALUE_WHITE- Constants.LIGHT_VALUE_BLACK )*0.5f;
//					Delay.msDelay(1000);
//				}
//				
//				if(button == Button.ID_LEFT)
//				{
//					Constants.LIGHT_VALUE_WHITE = values[0];
//					Constants.PID_OFFSET = (Constants.LIGHT_VALUE_WHITE- Constants.LIGHT_VALUE_BLACK )*0.5f;
//					Delay.msDelay(1000);
//				}
//				
//				if(button == Button.ID_ENTER)
//				{
//					setupDone = true;
//				}
//				
//				Delay.msDelay(100);
//		    }
		 
			pilot.reset();
			
		    provider = robot.getSensorLight().getRedMode();
		    float[] values = new float[provider.sampleSize()];
			
			last_error = Float.MAX_VALUE;
			
			while(!exit && !suppressed ){
				provider.fetchSample(values, 0);

				float lightValue = values[0];
				
				//error from -100 to 100
				float error = (float) ((lightValue - Constants.PID_OFFSET) * 200);
				
				/* dampen negative integral on line */
				if (error >= 0 && integral < 0) {
					integral *= IntegralBackoffLine;
				}
				
				/* dampen positive integral on black */
				if (error < 0 && integral > 0) {
					integral *= IntegralBackoffBlack;
				}
				
				/* dampen integral over time */
				integral *= IntegralDamping;
				
				/* add error to integral */
				if (error >= 0.f /* line */) {
					integral += error * IntegralFactorLine;
				} else /* black */ {
					integral += error * IntegralFactorBlack;
				}
				
				
				/* D Part */
				/* init last_error */
				if(last_error == Float.MAX_VALUE){
					last_error = error;
				}
				
				float Turn   = P * error + I * integral + D * (error - last_error);
				
				// power is in range -100 (full back) to 100 (full forward)
				int powerA = (int)((max_V -  Turn) * 100);         
			    int powerB = (int)((max_V +  Turn) * 100);         
			    
				pilot.setPower(powerA, powerB);
								
				last_error = error;
				
				LCD.drawString("LightV: " + lightValue, 1, 2);  
				LCD.drawString("Error: " + error, 1, 3);
				LCD.drawString("Turn: " + Turn, 1, 4);
				LCD.drawString("Power A: " + powerA, 1, 5);
				LCD.drawString("Power B: " + powerB, 1, 6);
			}	
			
			pilot.stop();
		}
	   

	   
	   public void terminate(){
		   this.exit = true;
	   }
}