package kit.edu.irobot.examples;

import java.util.ArrayList;
import java.util.List;

import kit.edu.irobot.robot.Robot;
import kit.edu.irobot.solver.StageSolver;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class RobotTest {
	
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

		List<String> modes = new ArrayList<String>();
		modes.add("Forward");
		modes.add("Backward");
		modes.add("TurnLeft");
		modes.add("TurnRight");
		modes.add("TurnAround");

	
		robot.setRobotSpeed(speed);
		
		while(button != Button.ID_ESCAPE){
			if( button == Button.ID_UP){
				switch(modes.get(mode)){
					case "Forward":
						robot.moveForward();
						break;
					case "Backward": 
						robot.moveBackward();
						break;
					case "TurnLeft": 
						robot.rotateLeft();
						break;
					case "TurnRight": 
						robot.rotateRight();
						break;
					case "TurnAround": 
						robot.rotate(180.0f);
						break;
						
				}
			}else if( button == Button.ID_DOWN){
			}else if( button == Button.ID_LEFT){
				speed = speed - 0.05f;
				robot.setRobotSpeed(speed);
			}else if( button == Button.ID_RIGHT){
				speed = speed + 0.05f;
				robot.setRobotSpeed(speed);
			}else if( button == Button.ID_ENTER){
				mode = mode +1;
				if(mode >= modes.size()){
					mode = 0;
				}
				robot.stopMotion();
			}
			button = -1;
			
			LCD.clear();
			LCD.drawString("Mode: "   + modes.get(mode), 0, 0);
			LCD.drawString("Speed: "  + speed, 0, 2);
			button = Button.getButtons();
			Delay.msDelay(100);
		}
		robot.stopMotion();
    }
};
