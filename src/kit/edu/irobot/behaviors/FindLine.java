package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Behavior;

public class FindLine  implements Behavior {
	private boolean exit = false; 

	private int arraySize = 50;
	private float[] history = new float[arraySize];
	private int calls = 0;

	private boolean suppressed = false;
	private Robot robot;

	public FindLine(Robot robot) {
		this.robot = robot;
	}

	public boolean takeControl() {
		if (this.exit == true) {
			return false;
		} 

		SampleProvider sample = robot.getSensorLight().getRedMode();

		float[] values = new float[sample.sampleSize()];
		sample.fetchSample(values, 0);

		history[calls % arraySize] = values[0];

		float errorSum = 0;
		for (int index = 0; index < arraySize; index++) {
			errorSum = errorSum + history[index];
		}

		if ((errorSum / arraySize) < 0.12) {
			return true;
		}

		return false;
	}

	public void suppress() {
		suppressed = true;
	}

	public void action() {
		// TODO
	}

	public void terminate() {
		this.exit = true;
	}
}