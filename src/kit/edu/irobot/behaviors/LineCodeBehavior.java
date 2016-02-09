package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import kit.edu.irobot.utils.Buffer;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.robotics.SampleProvider;

public class LineCodeBehavior {

	private Robot robot = null;
	private SampleProvider prov = null;
	private float[] curVal;
	private Buffer buffer = null;
	private float avg = 0;
	private int amountMeans = 10;
	private int sampleSize = -1;
	private double delta = 0.4;
	private boolean wasBlack = true;
	private long lastTime = System.currentTimeMillis();
	private long maxTime = 1000; // in ms //TODO good time?
	private boolean foundCode = false;

	public LineCodeBehavior(Robot robot) {
		this.robot = robot;
		prov = this.robot.getSensorLight().getRedMode();
		sampleSize = prov.sampleSize();
		curVal = new float[sampleSize];
		buffer = new Buffer(amountMeans);

		robot.beep();
		robot.setLEDPattern(3);
	}

	public boolean search() {
		robot.moveRobotForward();
		fetchSamples();
		// LCD.drawString("CurVal" + curVal[0], 3,0);
		long curTime = System.currentTimeMillis();
		if (wasBlack && avg + delta < curVal[0]) {
			// rising edge
			LCD.drawString("WasWhite, now IsBlack", 0, 1);
			robot.setLEDPattern(3);
			wasBlack = false;
			lastTime = System.currentTimeMillis();
			buffer.reset();
		} else if (!wasBlack && curVal[0] < avg - delta && curTime < lastTime + maxTime) {
			// falling edge
			LCD.drawString("WasBlack, now IsWhite", 0, 1);
			robot.setLEDPattern(4);
			wasBlack = true;

			Sound.beepSequenceUp();
			buffer.reset();
			foundCode = true;
		} else if (lastTime + maxTime < curTime) {
			LCD.drawString("Resetting...", 0, 1);
			wasBlack = true;
			lastTime = System.currentTimeMillis();
			buffer.reset();
		}

		return foundCode;
	}

	private void fetchSamples() {
		// curVal[0] - current value
		// curVal[sampleSize] - filtered value
		prov.fetchSample(curVal, 0);
		buffer.add(curVal[0]);
		avg = buffer.average();
		robot.writeErrorToDisplay("Cur avg: " + avg, "");
	}

}
