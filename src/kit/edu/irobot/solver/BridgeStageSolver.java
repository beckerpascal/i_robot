package kit.edu.irobot.solver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.kit.mindstorms.communication.ComModule;
import edu.kit.mindstorms.communication.Communication;
import kit.edu.irobot.behaviors.ElevatorBehavior;
import kit.edu.irobot.behaviors.GrindtheCrack;
import kit.edu.irobot.behaviors.RobotBehavior;
import kit.edu.irobot.utils.UnregulatedPilot;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
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
		
		//RobotBehavior b1 = new DriveForward(super.getRobot());
		RobotBehavior b2 = new GrindtheCrack(super.getRobot());
		RobotBehavior b3 = new ElevatorBehavior(super.getRobot(), super.exitCallback);

		behaviors = new ArrayList<RobotBehavior>();
		behaviors.add(b2);
		behaviors.add(b3);
		//behaviors.add(b3);
		
		RobotBehavior[] temp = new RobotBehavior[behaviors.size()];
		behaviors.toArray(temp);
		super.arby = new BetterArbitrator(temp);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		UnregulatedPilot pilot = super.robot.getUnregulatedPilot();
		
		pilot.stop();
		pilot.travel(200, 70);
		
		super.robot.HeadDown();
		
		Delay.msDelay(1000);
		
		super.arby.start();
		
		LCD.clear();
		LCD.drawString("JUHU, arby ended", 0, 0);
		Delay.msDelay(1000);
		
		
		/* calling elevator */
		final ComModule module = Communication.getModule();
		LCD.clear();
		LCD.drawString("Talking to elevator...", 0, 0);
		boolean reservated = false;
		try {
			while (!reservated && !abort) {
				String tries = "";
				LCD.clear(1);
				while (!module.requestStatus() && !abort) {
					LCD.drawString("Waiting" + tries, 0, 1);
					Delay.msDelay(5000);
					tries += ".";
				}
			
				if (module.requestElevator()) {
					LCD.drawString("Requested Elevator", 0, 1);
					reservated = true;
				} else {
					LCD.drawString("Requested Failed", 0, 1);
				}
			}
			
			
			while (!module.requestStatus() && !abort) {
				LCD.drawString("Waiting...", 0, 2);
				Delay.msDelay(500);
			}
			
			LCD.drawString("Got State RDY", 0, 2);
			Delay.msDelay(5000);
			
		} catch (IOException e) {
			LCD.drawString("Exception in Elevator: " + e.getMessage(), 0, 4);
			Delay.msDelay(5000);
		}

		Button.ENTER.waitForPressAndRelease();
		
		/* navigate into elevator*/
		
		try {
			if (module.moveElevatorDown()) {
				LCD.drawString("Requested Down", 0, 3);
			} else {
				LCD.drawString("Down Failed", 0, 3);	
			}
			
			
		} catch (IOException e) {
			LCD.drawString("Exception in Elevator: " + e.getMessage(), 0, 4);
			Delay.msDelay(5000);
		}

		
		// waiting for elevator to travel down
		for (int secs = 5; secs >= 0; secs--) {
			LCD.drawString("Waiting " + secs, 0, 4);
			Delay.msDelay(1000);
		}
		
		LCD.drawString("Complete!", 0, 5);
		Delay.msDelay(2000);
		
	}
	
	@Override
	public void stopSolver() {
		abort = true;
		for( int i = 0; i < behaviors.size(); i++)		
			behaviors.get(i).terminate();
		
		for( int i = 0; i < 10; i++){
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
