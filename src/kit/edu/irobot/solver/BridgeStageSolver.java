package kit.edu.irobot.solver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.kit.mindstorms.communication.ComModule;
import edu.kit.mindstorms.communication.Communication;
import kit.edu.irobot.behaviors.ExitOnLight;
import kit.edu.irobot.behaviors.GrindtheCrack;
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

public class BridgeStageSolver extends StageSolver{	
	private List<RobotBehavior> behaviors;
	
	public BridgeStageSolver() {
		super("BridgeStageSolver");
		
		//RobotBehavior b1 = new DriveForward(super.getRobot());
		RobotBehavior b2 = new GrindtheCrack(super.getRobot());
		RobotBehavior b3 = new ExitOnLight(super.getRobot(), super.exitCallback);

		behaviors = new ArrayList<RobotBehavior>();
		behaviors.add(b2);
		behaviors.add(b3);
		//behaviors.add(b3);
		
		RobotBehavior[] temp = new RobotBehavior[behaviors.size()];
		behaviors.toArray(temp);
		super.arby = new BetterArbitrator(temp);
		// TODO Auto-generated constructor stub
	}
	
	private void findEntry(DifferentialPilot pilot) {
		boolean found = false;
		while (!found && !abort) {
			pilot.reset();
			pilot.forward();
			waitBounced();
			float distance = pilot.getMovementIncrement(); //in cm
			if (distance > 25) {
				LCD.drawString("Should be in Elevator " + distance, 0, 2);
				found = true;
				pilot.travel(-2.f);
				pilot.stop();
				
			} else {
				LCD.drawString("Bounced after " + distance, 0, 2);
				pilot.travel(-4.f);
				pilot.rotate(15.f);
			}
			
		}
	}
	
	private void waitBounced() {
		SampleProvider provider = robot.getSensorTouchFront().getTouchMode();
		float[] values = new float[provider.sampleSize()];
		
		while (!abort) {
			provider.fetchSample(values, 0);
			float touched = values[0];
			
			if (touched >= 1.0f) {
				return;
			} else {
				Delay.msDelay(10);
			}
		}
	}
	
	private void waitForGreen() {
		LCD.clear();
		LCD.drawString("Wait for Green", 0, 0);
		
		SampleProvider provider = robot.getSensorLight().getAmbientMode();
		float[] values = new float[provider.sampleSize()];
		
		while (!abort) {
			provider.fetchSample(values, 0);
			float ambient = values[0];
			
			if (ambient > 0.2f && ambient < 0.5f) {
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
	public void run() {
		super.robot.getUnregulatedPilot().close();
		DifferentialPilot pilot = super.robot.getDifferentialPilot();
		
		pilot.stop();
		pilot.setTravelSpeed(0.8*pilot.getMaxTravelSpeed());
		pilot.travel(45);
		
		super.robot.HeadDown();
		
		Delay.msDelay(100);
		
		super.arby.start();
		pilot.stop();
		
		LCD.clear();
		LCD.drawString("JUHU, arby ended", 0, 0);
		Delay.msDelay(100);
		
		
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
					Delay.msDelay(1000);
					tries += ".";
				}
				
				if (!abort && module.requestElevator()) {
					LCD.drawString("Requested Elevator", 0, 1);
					reservated = true;
				} else {
					LCD.drawString("Requested Failed", 0, 1);
				}
			}
			
		} catch (IOException e) {
			LCD.drawString("Exception in Elevator: " + e.getMessage(), 0, 4);
			Delay.msDelay(1000);
		}

		
		pilot.rotate(-19);
		pilot.travel(35);
		
		waitForGreen();
		findEntry(pilot);
		
		//Button.ENTER.waitForPressAndRelease();
		
		/* navigate into elevator*/
		
		try {
			if (!abort && module.moveElevatorDown()) {
				LCD.drawString("Requested Down", 0, 3);
			} else {
				LCD.drawString("Down Failed", 0, 3);	
			}
			
			
		} catch (IOException e) {
			LCD.drawString("Exception in Elevator: " + e.getMessage(), 0, 4);
			Delay.msDelay(1000);
		}

		
		// waiting for elevator to travel down
		for (int secs = 5; !abort && secs >= 0; secs--) {
			LCD.drawString("Waiting " + secs, 0, 4);
			Delay.msDelay(1000);
		}
		
		pilot.travel(33);
		
		LCD.drawString("Complete!", 0, 5);
		if (!abort) Delay.msDelay(1000);
		
		pilot.stop();
	}
	
	@Override
	public void stopSolver() {
		super.stopSolver();
		
		for( int i = 0; i < behaviors.size(); i++)		
			behaviors.get(i).terminate();
		
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
