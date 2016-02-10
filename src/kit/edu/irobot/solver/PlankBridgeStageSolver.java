package kit.edu.irobot.solver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.kit.mindstorms.communication.ComModule;
import edu.kit.mindstorms.communication.Communication;
import kit.edu.irobot.behaviors.ExitOnLight;
import kit.edu.irobot.behaviors.GrindtheCrack;
import kit.edu.irobot.behaviors.PlankBridgeGetInPosition;
import kit.edu.irobot.behaviors.RobotBehavior;
import kit.edu.irobot.utils.UnregulatedPilot;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;

/**
 * Behavior for driving over the bridge 
 * @author Pascal Becker
 *
 */

public class PlankBridgeStageSolver extends StageSolver{	
	private RobotBehavior[] behaviors;
	
	private RobotBehavior getInPosition;
	
	public PlankBridgeStageSolver() {
		super("BridgeStageSolver");

		getInPosition = new PlankBridgeGetInPosition(robot);
		
		//RobotBehavior b2 = new GrindtheCrack(super.getRobot());
		RobotBehavior b3 = new ExitOnLight(robot, exitCallback);

		behaviors = new RobotBehavior[] {b3};
		super.arby = new BetterArbitrator(behaviors);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public void run() {
		DifferentialPilot pilot = robot.getDifferentialPilot();
		//getInPosition.action();
		
		pilot.setTravelSpeed(pilot.getMaxTravelSpeed());
		/* in position and still on ramp */
		

		pilot.travel(-20);
		pilot.rotate(-90);
		pilot.forward();
		
		
		//arby.start();
		//pilot.quickStop();
		waitForBounce();
		pilot.travel(-20);
		pilot.rotate(-90);
		pilot.quickStop();
	}
	
	@Override
	public void stopSolver() {
		super.stopSolver();
		
		if (getInPosition != null) getInPosition.terminate();
		
		for( int i = 0; i < behaviors.length; i++)		
			behaviors[i].terminate();
		
		for( int i = 0; i < 5; i++){
			super.arby.stop();
			Delay.msDelay(10);
		}
		
		super.getRobot().stopMotion();
	}
	
	public static void main(String[] args) 
	{
		BridgeStageSolver solver = new BridgeStageSolver();
		
		LCD.drawString("Starte BridgeStageSolver mit UP", 0, 1);
		if (Button.waitForAnyPress() == Button.ID_ESCAPE) return;
		
		//solver.setDaemon(true);
		solver.start();
		Button.ESCAPE.waitForPressAndRelease();
		solver.stopSolver();
		/*try {
			solver.join();
		} catch (InterruptedException e) {
		}*/
		LCD.drawString("solver finished :)", 0, 1);
		Delay.msDelay(2000);
	}
}
