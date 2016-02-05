package kit.edu.irobot.examples;

import java.util.ArrayList;
import java.util.List;

import kit.edu.irobot.robot.Robot;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import kit.edu.irobot.solver.*;

public class RobotTest2 {
	
	public static void main(String[] args) 
    {
		Robot robot;
		robot = Robot.getInstance();

		LCD.clear();
		LCD.drawString("Press Enter to ch. Mode", 0, 0);
		
		int button = Button.waitForAnyEvent();
		
		float speed = 0.3f;
		int mode = 0;

		List<String> modes = new ArrayList<String>();
		modes.add("Forward");
		modes.add("Backward");
		modes.add("TurnLeft");
		modes.add("TurnRight");
		modes.add("TurnAround");
		modes.add("FollowLine");
		modes.add("MazeSolver");

	
		robot.setRobotSpeed(speed);
		
		while(button != Button.ID_ESCAPE){
			if( button == Button.ID_UP){
				mode = mode +1;
				if(mode >= modes.size())mode = 0;
				robot.stopMotion();	
			}else if( button == Button.ID_DOWN){
				mode = mode -1;
				if(mode < 0)mode = modes.size()-1;
				robot.stopMotion();
			}else if( button == Button.ID_LEFT){
				speed = speed - 0.05f;
				robot.setRobotSpeed(speed);
			}else if( button == Button.ID_RIGHT){
				speed = speed + 0.05f;
				robot.setRobotSpeed(speed);
			}else if( button == Button.ID_ENTER){
				switch(modes.get(mode)){
				case "Forward":
					robot.moveRobotForward();
					break;
				case "Backward": 
					robot.moveRobotBackward();
					break;
				case "TurnLeft": 
					robot.rotateRobotLeft();
					break;
				case "TurnRight": 
					robot.rotateRobotRight();
					break;
				case "TurnAround": 
					robot.rotateRobot(180.0f);
					break;
				case "FollowLine":
					LineFollowingStageSolver solverLine = new LineFollowingStageSolver();
					solverLine.start();
					while (!Button.DOWN.isDown()) {	    	
						Delay.msDelay(100);
					}
					solverLine.stopSolver();
					break;
				case "MazeSolver":
					MazeStageSolver maze = new MazeStageSolver();
					maze.start();
					while (!Button.DOWN.isDown()) {	    	
						Delay.msDelay(100);
					}
					maze.stopSolver();
					break;	
				}
			}
			button = -1;
			
			LCD.clear();
			LCD.drawString("Mode: "   + modes.get(mode), 0, 0);
			LCD.drawString("Speed: "  + speed, 0, 2);
			button = Button.getButtons();
			Delay.msDelay(100);
			}
	}
};
