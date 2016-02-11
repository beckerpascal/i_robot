package kit.edu.irobot.solver;

import java.io.IOException;

import edu.kit.mindstorms.communication.ComModule;
import edu.kit.mindstorms.communication.Communication;
import kit.edu.irobot.behaviors.ExitOnLight;
import kit.edu.irobot.behaviors.GrindtheCrack;
import kit.edu.irobot.behaviors.RobotBehavior;
import lejos.hardware.lcd.LCD;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;

/**
 * Behavior for driving over the bridge 
 * @author Pascal Becker
 *
 */

public class BridgeStageSolver extends StageSolver{	
	private RobotBehavior[] behaviors;
	
	public BridgeStageSolver() {
		super("Bridge");
		requestResources(D_PILOT | ULTRA | COLOR | TOUCH | HEAD);
		
		//RobotBehavior b1 = new DriveForward(super.getRobot());
		RobotBehavior b2 = new GrindtheCrack(super.robot);
		RobotBehavior b3 = new ExitOnLight(super.robot, super.exitCallback);

		behaviors = new RobotBehavior[]{b2, b3};
		super.arby = new BetterArbitrator(behaviors);
	}
	
	private void findEntry(DifferentialPilot pilot) {
		boolean found = false;
		while (!found && active()) {
			pilot.reset();
			pilot.forward();
			waitForBounce();
			float distance = pilot.getMovementIncrement(); //in cm
			if (distance > 25) {
				LCD.drawString("Should be in Elevator " + distance, 0, 2);
				found = true;
				pilot.travel(-2.f);
				pilot.stop();
				
			} else {
				LCD.drawString("Bounced after " + distance, 0, 2);
				pilot.travel(-4.f);
				pilot.rotate(17.f);
			}
			
		}
	}
	
	private void waitForGreen() {
		LCD.clear();
		LCD.drawString("Wait for Green", 0, 0);
		
		SampleProvider provider = colorSensor.getAmbientMode();
		float[] values = new float[provider.sampleSize()];
		
		while (active()) {
			provider.fetchSample(values, 0);
			float ambient = values[0];
			
			if (ambient > 0.18f && ambient < 0.5f) {
				LCD.clear(1);
				LCD.drawString("Green! " + ambient, 0, 1);
				break;
			} else {
				LCD.clear(1);
				LCD.drawString("Ambient:" + ambient, 0, 1);
				Delay.msDelay(50);
			}
		}
	}

	@Override
	public void solve() {
		diffPilot.setTravelSpeed(0.8*diffPilot.getMaxTravelSpeed());
		if (active()) diffPilot.travel(45);
		
		if (active()) headDown();
		if (active()) arby.start();
		if (active()) diffPilot.stop();
		
		LCD.clear();
		
		/* calling elevator */
		final ComModule module = Communication.getModule();
		LCD.clear();
		LCD.drawString("Talking to elevator...", 0, 1);
		boolean reservated = false;
		try {
			while (!reservated && active()) {
				String tries = "";
				LCD.clear(2);
				while (!module.requestStatus() && active()) {
					LCD.drawString("Waiting" + tries, 0, 2);
					Delay.msDelay(500);
					tries += ".";
				}
				
				if (active() && module.requestElevator()) {
					LCD.drawString("Requested Elevator", 0, 2);
					reservated = true;
				} else {
					LCD.drawString("Requested Failed", 0, 2);
				}
			}
			
		} catch (IOException e) {
			LCD.drawString("Exception in Elevator: " + e.getMessage(), 0, 5);
			Delay.msDelay(1000);
		}

		
		/* navigate into elevator */
		if (active()) diffPilot.rotate(-15);
		if (active()) diffPilot.travel(35);
			
		if (active()) waitForGreen();
		if (active()) findEntry(diffPilot);
		
		
		/* move elevator down */
		try {
			if (active() && module.moveElevatorDown()) {
				LCD.drawString("Requested Down", 0, 4);
			} else {
				LCD.drawString("Down Failed", 0, 4);	
			}
			
			
		} catch (IOException e) {
			LCD.drawString("Exception in Elevator: " + e.getMessage(), 0, 5);
			Delay.msDelay(1000);
		}

		
		// waiting for elevator to travel down
		for (int secs = 5; active() && secs >= 0; secs--) {
			LCD.drawString("Waiting " + secs, 0, 5);
			Delay.msDelay(1000);
		}
			
			// drive out of elevator
		if (active()) diffPilot.travel(33);
		if (active()) LCD.drawString("Complete!", 0, 5);
		
		diffPilot.stop();
	}
	
	@Override
	public void stopSolver() {
		super.stopSolver();
		
		for( int i = 0; i < behaviors.length; i++)		
			behaviors[i].terminate();
	}
	
	
	/*public static void main(String[] args) 
	{
		BridgeStageSolver solver = new BridgeStageSolver();
		
		LCD.drawString("Starte BridgeStageSolver mit UP", 0, 1);
		if (Button.waitForAnyPress() == Button.ID_ESCAPE) return;
		
		//solver.setDaemon(true);
		solver.start();
		Button.ESCAPE.waitForPressAndRelease();
		solver.stopSolver();
		LCD.drawString("solver finished :)", 0, 1);
		Delay.msDelay(2000);
	}*/
}
