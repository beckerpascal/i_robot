package kit.edu.irobot.examples;

import kit.edu.irobot.robot.Robot;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class RobotTest {
	
	public static String modeToText(int mode){
		switch (mode){
			case 0:
				return "Forward";
			
			case 1:
				return "Backward";
			
			case 2:
				return "TurnLeft";

			case 3:
				return "TurnRight";
		}
		return "Wrong Mode";
	}
	
	public static void main(String[] args) 
    {
		Robot robot;
		robot = Robot.getInstance();

		LCD.clear();
		LCD.drawString("Press Enter to ch. Mode", 0, 0);
		
		int button = Button.waitForAnyEvent();
		
		float speed = 0.3f;
		int mode = 0;

		final int max_mode = 3;

		robot.setSpeed(speed);
		
		while(button != Button.ID_ESCAPE){
			if( button == Button.ID_UP){
				switch(mode){
					case 0:
						robot.moveForward();
						break;
					case 1: 
						robot.moveBackward();
						break;
					case 2: 
						robot.rotateLeft();
						break;
					case 3: 
						robot.rotateRight();
						break;
				}
			}else if( button == Button.ID_DOWN){
			}else if( button == Button.ID_LEFT){
				speed = speed - 0.05f;
				robot.setSpeed(speed);
			}else if( button == Button.ID_RIGHT){
				speed = speed + 0.05f;
				robot.setSpeed(speed);
			}else if( button == Button.ID_ENTER){
				mode = mode +1;
				if(mode > max_mode){
					mode = 0;
				}
				robot.stopMotion();
			}
			button = -1;
			
			LCD.clear();
			LCD.drawString("Mode: "   + modeToText(mode), 0, 0);
			LCD.drawString("Speed: "  + speed, 0, 2);
			button = Button.getButtons();
			Delay.msDelay(100);
		}
		robot.stopMotion();
    }
};
