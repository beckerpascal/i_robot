package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import kit.edu.irobot.utils.Buffer;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.robotics.SampleProvider;

public class LineCodeBehavior {

	private Robot robot = null;
	private SampleProvider prov = null;
	private float[] curVal;
	private Buffer buffer = null;
	private float avg = 0;
	private int amountMeans = 5;
	private int sampleSize = -1;
	private double delta = 0.3;
	private int stage = 0;
	private boolean wasBlack = true;
	private boolean wasWhite = false;
	private long lastTime = System.currentTimeMillis();
	private long maxTime = 2000; // in ms //TODO good time?
	private boolean foundCode = false;

	public LineCodeBehavior(Robot robot) {
		this.robot = robot;
		prov = this.robot.getSensorLight().getRedMode();
		sampleSize = prov.sampleSize();
		curVal = new float[sampleSize];
		buffer = new Buffer(amountMeans);
		lastTime = System.currentTimeMillis();

		robot.beep();
		robot.setLEDPattern(3);
	}

	public int search(int button) {
		while (!foundCode && button != Button.ID_ESCAPE) {
			button = Button.getButtons();
			robot.moveRobotForward();
			fetchSamples();
			// LCD.drawString("CurVal" + curVal[0], 3,0);
			long curTime = System.currentTimeMillis();
			if (wasBlack && curVal[0] > avg + delta) {
				// rising edge
				LCD.drawString("WasWhite, now IsBlack", 0, 1);
				robot.setLEDPattern(3);
				wasBlack = false;
				wasWhite = true;
				lastTime = System.currentTimeMillis();
				buffer.reset();
			} else if (wasWhite && curVal[0] < avg && curTime < lastTime + maxTime) {
				// falling edge
				LCD.drawString("WasBlack, now IsWhite", 0, 1);
				robot.setLEDPattern(4);
				wasBlack = true;
				wasWhite = false;

				stage++;
				robot.beep();
				Sound.beepSequenceUp();
				buffer.reset();
				LCD.drawString("Stage: " + stage, 0, 3);
				// foundCode = true;
			} else if (lastTime + maxTime < curTime) {
				LCD.drawString("Resetting...", 0, 1);
				wasBlack = true;
				wasWhite = false;
				lastTime = System.currentTimeMillis();
				buffer.reset();
			}
			// } else if (lastTime < System.currentTimeMillis() - maxTime) {
			// //robot.writeErrorToDisplay("No Line found...", "");
			// foundCode = true;
			// }
		}
		if (foundCode) {
			return stage;
		} else {
			return -1;
		}
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
