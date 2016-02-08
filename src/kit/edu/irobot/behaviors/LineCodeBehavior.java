package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.filter.MeanFilter;

public class LineCodeBehavior extends RobotBehavior {

	private EV3ColorSensor sensor = null;
	private float[] curVal;
	private MeanFilter meanF = null;
	private int amountMeans = 10;
	private int sampleSize = -1;
	private double delta = 0.5;
	private int lineCounter = 0;
	private boolean wasBlack = true;
	private boolean wasWhite = false;
	private long lastTime = System.currentTimeMillis();
	private long maxTime = 3000; // in ms
	private boolean foundCode = false;

	private final int MAZE_LINES = 1;
	private final int LINE_LINES = 2;
	private final int BRIDGE_LINES = 3;
	private final int ELEVATOR_LINES = 4;
	private final int HANGING_BRIDGE_LINES = 5;
	private final int SWAMP_LINES = 6;
	private final int ENDBOSS_LINES = 7;

	public LineCodeBehavior(Robot robot) {
		this.robot = robot;
		sensor = robot.getSensorLight();
		sampleSize = sensor.sampleSize();
		curVal = new float[2 * sampleSize];
		meanF = new MeanFilter(sensor, amountMeans);
		lastTime = System.currentTimeMillis();
	}

	public boolean takeControl() {
		// TODO: Improve?!
		return true;
	}

	public void suppress() {
		suppressed = true;
	}

	public void action() {

		lastTime = System.currentTimeMillis();
		while (!suppressed && !foundCode) {
			robot.moveRobotForward();
			fetchSamples();
			if (wasWhite && curVal[0] < curVal[sampleSize] - delta) {
				// falling edge
				wasBlack = true;
				wasWhite = false;
			} else if (wasBlack && curVal[sampleSize] + delta < curVal[0]) {
				// rising edge
				wasBlack = false;
				wasWhite = true;
				lineCounter++;
				robot.writeErrorToDisplay("Increased LineCounter", "now: " + lineCounter);
				robot.beep();
			} else if (lastTime < System.currentTimeMillis() - maxTime) {
				switch (lineCounter) {
				case MAZE_LINES:
					// maze arb
					break;
				case LINE_LINES:
					// line arb
					break;
				case BRIDGE_LINES:
					// bridge arb
					break;
				case ELEVATOR_LINES:
					// elevator arb
					break;
				case HANGING_BRIDGE_LINES:
					// haning bridge arb
					break;
				case SWAMP_LINES:
					// swamp arb
					break;
				case ENDBOSS_LINES:
					// endboss arb
					break;
				}
				foundCode = true;
			}
		}
	}

	private void fetchSamples() {
		// curVal[0] - current value
		// curVal[sampleSize] - filtered value
		sensor.fetchSample(curVal, 0);
		meanF.fetchSample(curVal, sampleSize);

	}

}
