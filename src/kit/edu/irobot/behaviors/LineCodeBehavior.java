package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import kit.edu.irobot.utils.Buffer;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;

public class LineCodeBehavior {

	private Robot robot = null;
	private SampleProvider prov = null;
	private float[] curVal;
	private Buffer buffer = null;
	private int amountMeans = 10;
	private int sampleSize = -1;
	private double delta = 0.3;
	private int stage = 0;
	private boolean wasBlack = true;
	private boolean wasWhite = false;
	private long lastTime = System.currentTimeMillis();
	private long maxTime = 1000; // in ms //TODO good time?
	private boolean foundCode = false;

	public LineCodeBehavior(Robot robot) {
		this.robot = robot;
		prov = this.robot.getSensorLight().getRedMode();
		sampleSize = prov.sampleSize();
		curVal = new float[2 * sampleSize];
		buffer = new Buffer(amountMeans);
		lastTime = System.currentTimeMillis();
		
		robot.beep();
		robot.setLEDPattern(3);
	}

	public int search(int button) {
		lastTime = System.currentTimeMillis();
		while (!foundCode && button != Button.ID_ESCAPE) {
			button = Button.getButtons();
			robot.moveRobotForward();
			fetchSamples();
			//LCD.drawString("CurVal" + curVal[0], 3,0);
			if (wasWhite && curVal[0] < curVal[sampleSize] - delta) {
				// falling edge
				wasBlack = true;
				wasWhite = false;
				robot.setLEDPattern(3);
				LCD.drawString("WasWhite, now IsBlack", 0,1);
			} else if (wasBlack && curVal[sampleSize] + delta < curVal[0]) {
				LCD.drawString("WasBlack, now IsWhite", 0,2);

				// rising edge
				wasBlack = false;
				wasWhite = true;
				stage++;
				robot.beep();
				Sound.beepSequenceUp();
				robot.setLEDPattern(4);
				//foundCode = true;
			}
//			} else if (lastTime < System.currentTimeMillis() - maxTime) {
//				//robot.writeErrorToDisplay("No Line found...", "");
//				foundCode = true;
//			}
		}
		if(foundCode){
			return stage;
		}else{
			return -1;
		}
	}

	private void fetchSamples() {
		// curVal[0] 		  - current value
		// curVal[sampleSize] - filtered value
		prov.fetchSample(curVal, 0);
		buffer.add(curVal[0]);
		robot.writeErrorToDisplay("Cur mean: " + curVal[sampleSize], "");
	}

}
