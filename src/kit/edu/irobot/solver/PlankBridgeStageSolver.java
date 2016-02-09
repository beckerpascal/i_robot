package kit.edu.irobot.solver;

import java.util.ArrayList;
import java.util.List;

import kit.edu.irobot.behaviors.AvoidObstacle;
import kit.edu.irobot.behaviors.DriveForward;
import kit.edu.irobot.behaviors.DrivePlankBridge;
import kit.edu.irobot.behaviors.ExitOnLight;
import kit.edu.irobot.behaviors.GrindtheCrack;
import kit.edu.irobot.behaviors.PlankBridgeGetInPosition;
import kit.edu.irobot.behaviors.RobotBehavior;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

/**
 * Behavior for driving over the bridge 
 * @author Pascal Becker
 *
 */

public class PlankBridgeStageSolver extends StageSolver{	
	private PlankBridgeGetInPosition getInPosition;

	private EV3ColorSensor sensor = null;
	private SampleProvider lightProv = null;
	private float[] lightValue;
	private boolean exit = false;
	
	private List<RobotBehavior> behaviors;
	public PlankBridgeStageSolver() {
		super("BridgeStageSolver");
		this.sensor = super.getRobot().getSensorLight();
		this.lightProv = sensor.getRedMode();
		this.lightValue = new float[this.lightProv.sampleSize()];
		
		
		RobotBehavior b1 = new DrivePlankBridge(super.getRobot());
		RobotBehavior b2 = new PlankBridgeGetInPosition(super.getRobot());

		behaviors = new ArrayList<RobotBehavior>();
		behaviors.add(b2);
		behaviors.add(b1);
		RobotBehavior[] temp = new RobotBehavior[behaviors.size()];
		behaviors.toArray(temp);
		super.arby = new BetterArbitrator(temp);
	}

	@Override
	public void run() {
		
		
		//Tries to get robot in position
		super.getRobot().setRobotSpeed(0.2f);
		super.getRobot().moveRobotForward();
		Delay.msDelay(1500);
		super.getRobot().stopMotion();
		/*getInPosition.action();
		super.getRobot().setRobotSpeed(0.5f);
		super.getRobot().moveRobotForward();
		
		this.lightProv.fetchSample(lightValue, 0);
		while( lightValue[0] < 0.8f && !exit){
			Delay.msDelay(10);
			this.lightProv.fetchSample(lightValue, 0);
		}
		super.getRobot().stopMotion();
		LCD.drawString("Finished Bridge", 0,4);
		*/
		super.arby.start();
	}
	
	@Override
	public void stopSolver() {
		for( int i = 0; i < behaviors.size(); i++)
			behaviors.get(i).terminate();
		
		for( int i = 0; i< 10; i++){
			super.arby.stop();
		}
		super.getRobot().stopMotion();
		
		LCD.drawString("stop motion...", 1, 0); 
	}
	
	public static void main(String[] args) 
	{
		PlankBridgeStageSolver solver = new PlankBridgeStageSolver();
		
		LCD.drawString("Starte BridgeStage", 0, 1);
		LCD.drawString("-start with button", 0, 2);
		LCD.drawString("-kill with button" , 0, 3);
		

		Button.waitForAnyPress();
		solver.start();
		Button.waitForAnyPress();
		solver.stopSolver();
	}
}
