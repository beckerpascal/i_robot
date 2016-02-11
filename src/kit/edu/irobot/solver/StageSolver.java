package kit.edu.irobot.solver;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;

import kit.edu.irobot.robot.Robot;
import kit.edu.irobot.utils.Constants;
import kit.edu.irobot.utils.UnregulatedPilot;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;

public abstract class StageSolver extends Thread {
	
	protected static final boolean SYNCHRONIZE = false;
	
	/** Regulated Motors. Used with requestRessources() */
	protected static final int MOTORS = 1 << 0;
	
	/** DifferentialPilot. Used with requestRessources() */
	protected static final int D_PILOT = 1 << 1;
	
	/** UnregulatedPilot. Used with requestRessources() */
	protected static final int U_PILOT = 1 << 2;
	
	/** Light Sensor. Used with requestRessources() */
	protected static final int TOUCH = 1 << 3;
	
	/** Light Sensor. Used with requestRessources() */
	protected static final int COLOR = 1 << 4;
	
	/** Ultra Sonic Sensor. Used with requestRessources()*/
	protected static final int ULTRA = 1 << 5;
	
	/** Head Motor. Used with requestRessources()*/
	protected static final int HEAD = 1 << 5;
	
	protected String name;
	protected BetterArbitrator arby;
	protected Robot robot;
	
	protected boolean abort;
	
	/* Ressources
	 * These are null unless requested with requestRessources() */
	protected EV3LargeRegulatedMotor motorLeft = null;
	protected EV3LargeRegulatedMotor motorRight = null;
	protected EV3MediumRegulatedMotor headMotor = null;
	protected DifferentialPilot diffPilot = null;
	protected UnregulatedPilot unregPilot = null;
	protected EV3TouchSensor touchSensor = null;
	protected EV3ColorSensor colorSensor = null;
	protected EV3UltrasonicSensor distanceSensor = null;
	
	private ArrayList<Closeable> ressources = new ArrayList<>();
	
	private int requestedResourced;
	
	public StageSolver(String name){
		super(name);
		this.name = name;
		this.robot = Robot.getInstance();
		this.abort = false;
		this.requestedResourced = 0;
	}
	
	/**
	 * Sets abort and stops arbitrator
	 */
	public void stopSolver(boolean join) {
		abort = true;
		stopArbitrator();
		try {
			this.join(5000);
		} catch (InterruptedException e) {
			//TODO: can this happen?
		}
	}
	
	public void stopSolver() {
		stopSolver(false);
	}
	
	public void start() {
		createResources();
		super.start();
	}
	
	/**
	 * Call this to request resources
	 * @param flags i.e: MOTORS | COLOR | TOUCH
	 */
	protected void requestResources(int flags) {
		if (this.isAlive()) throw new IllegalStateException("Ressources have to be reuqested befor start()");
		
		//if ((flags & MOTORS) != 0 && (flags & D_PILOT) != 0) throw new IllegalArgumentException("Cant use motors and pilot");
		if ((flags & MOTORS) != 0 && (flags & U_PILOT) != 0) throw new IllegalArgumentException("Cant use motors and pilot");
		if ((flags & D_PILOT) != 0 && (flags & U_PILOT) != 0) throw new IllegalArgumentException("Cant use unregulated and differential pilot");
		
		requestedResourced = flags;
		
		//createResources();
	}
	
	private final void createResources() {
		if ((requestedResourced & MOTORS) != 0) {
			motorLeft = new EV3LargeRegulatedMotor(Constants.LEFT_MOTOR);
			motorRight = new EV3LargeRegulatedMotor(Constants.RIGHT_MOTOR);
			if (SYNCHRONIZE) motorLeft.synchronizeWith(new RegulatedMotor[]{motorRight});
			ressources.add(motorLeft);
			ressources.add(motorRight);
		}
		
		if ((requestedResourced & D_PILOT) != 0) {
			/*EV3LargeRegulatedMotor left = new EV3LargeRegulatedMotor(Constants.LEFT_MOTOR);
			EV3LargeRegulatedMotor right = new EV3LargeRegulatedMotor(Constants.RIGHT_MOTOR);*/
			if (motorLeft == null) {
				motorLeft = new EV3LargeRegulatedMotor(Constants.LEFT_MOTOR);
				motorRight = new EV3LargeRegulatedMotor(Constants.RIGHT_MOTOR);
				if (SYNCHRONIZE) motorLeft.synchronizeWith(new RegulatedMotor[]{motorRight});
				ressources.add(motorLeft);
				ressources.add(motorRight);
			}
			diffPilot = new DifferentialPilot(4.275, 14.315, motorLeft, motorRight);
		}
		
		if ((requestedResourced & U_PILOT) != 0) {
			unregPilot = new UnregulatedPilot(143, 43);
			ressources.add(unregPilot);
		}
		
		if ((requestedResourced & COLOR) != 0) {
			colorSensor = new EV3ColorSensor(Constants.LIGHT_SENSOR);
			ressources.add(colorSensor);
		}
		
		if ((requestedResourced & ULTRA) != 0) {
			distanceSensor = new EV3UltrasonicSensor(Constants.DISTANCE_SENSOR);
			ressources.add(distanceSensor);
		}
		
		if ((requestedResourced & TOUCH) != 0) {
			touchSensor = new EV3TouchSensor(Constants.TOUCH_FRONT_SENSOR);
			ressources.add(touchSensor);
		}
		
		if ((requestedResourced & HEAD) != 0) {
			headMotor = new EV3MediumRegulatedMotor(Constants.SPECIAL_MOTOR);
			ressources.add(headMotor);
		}
	}
	
	@Override
	public final void run() {
		printName();
		//createResources();
		//try {
			solve();
		/*} catch (Exception e) {
			LCD.clearDisplay();
			printName();
			LCD.drawString("ERROR: " + e.getMessage(), 0, 1);
		}*/
		closeRessources();
	}
	
	private void printName() {
		LCD.clear(0);
		LCD.drawString(name, 0, 0);
	}
	
	/**
	 * Override this method to implement the stage behavior
	 */
	public abstract void solve();
	
	private void closeRessources() {
		for (Closeable res : ressources){
			try {
                res.close();
            } catch(IOException e)
            {
                // this really should not happen
            }
		}
	}
	
	
	public interface ExitCallback {
		void exitArby();
	}

	protected ExitCallback exitCallback = new ExitCallback() {
		
		@Override
		public void exitArby() {
			StageSolver.this.stopArbitrator();
		}
	};
	
	protected void waitForBounce() {
		SampleProvider provider = touchSensor.getTouchMode();
		float[] values = new float[provider.sampleSize()];
		
		while (active()) {
			provider.fetchSample(values, 0);
			float touched = values[0];
			
			if (touched >= 1.0f) {
				return;
			} else {
				Thread.yield();
			}
		}
	}
	
	/**
	 * Checks whether the solver is active
	 * @return
	 */
	protected boolean active() {
		return !abort && !isInterrupted();
	}
	
	protected void headUp() {
		headMotor.rotate(-90);
	}

	protected void headDown() {
		headMotor.rotate(90);
	}
	
	protected void stopArbitrator() {
		if (arby != null && arby.isRunning()) {
			// TODO: is this enough?
			arby.stop();
		}
	}
	
}
