package kit.edu.irobot.solver;

import java.util.ArrayList;
import java.util.List;

import kit.edu.irobot.behaviors.AvoidObstacle;
import kit.edu.irobot.behaviors.DriveForward;
import kit.edu.irobot.behaviors.ElevatorBehavior;
import kit.edu.irobot.behaviors.GrindtheCrack;
import kit.edu.irobot.behaviors.RobotBehavior;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

/**
 * Behavior for driving over the bridge 
 * @author Pascal Becker
 *
 */

public class BridgeStageSolver extends StageSolver{	
	private List<RobotBehavior> behaviors;
	
	public BridgeStageSolver() {
		super("BridgeStageSolver");
		
		RobotBehavior b1 = new DriveForward(super.getRobot());
		RobotBehavior b2 = new GrindtheCrack(super.getRobot());
		//RobotBehavior b3 = new ElevatorBehavior(super.getRobot());

		behaviors = new ArrayList<RobotBehavior>();
		behaviors.add(b1);
		behaviors.add(b2);
		//behaviors.add(b3);
		
		RobotBehavior[] temp = new RobotBehavior[behaviors.size()];
		behaviors.toArray(temp);
		super.arby = new BetterArbitrator(temp);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		super.arby.start();
	}
	
	@Override
	public void stopSolver() {
		for( int i = 0; i < behaviors.size(); i++)		
			behaviors.get(i).terminate();
		
		for( int i = 0; i < 100; i++){
			super.arby.stop();
			Delay.msDelay(100);
		}
		super.getRobot().stopMotion();

		LCD.drawString("stop motion...", 1, 0);
		 
	}
	
	public static void main(String[] args) 
	{
		BridgeStageSolver solver = new BridgeStageSolver();
		
		LCD.drawString("Starte BridgeStageSolver mit UP", 0, 1);
		while (!Button.UP.isDown()) {	    	
			Delay.msDelay(100);
		}
		solver.start();
		while (!Button.ESCAPE.isDown()) {	    	
			Delay.msDelay(100);
		}
		solver.stopSolver();
	}
}
