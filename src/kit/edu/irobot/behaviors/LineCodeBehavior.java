package kit.edu.irobot.behaviors;

import kit.edu.irobot.robot.Robot;
import kit.edu.irobot.utils.Constants;
import lejos.hardware.sensor.EV3ColorSensor;

public class LineCodeBehavior extends Thread {
	private EV3ColorSensor light = null;
	private Robot robot = null;
	private float[] val = null;
	private int count = 0;

	public LineCodeBehavior(Robot robot) {
		this.robot = robot;
		light = robot.getSensorLight();
		val = new float[light.sampleSize()];
	}
	
	public int getCurrentStage(){
		
		return count;
	}

	public void run() {

		System.out.println("reading barcodes");
		light.fetchSample(val, 0);
		boolean onBar = false;
		long tLastSensorRead = System.currentTimeMillis();
		long tLastScanSecs = tLastSensorRead;
		int delay = 1000 / Constants.SCAN_FREQUENCY_HZ;
		while (true) {

			light.fetchSample(val, 0);
			if (!onBar) {
				if (val[0] > Constants.BAR_MIN) {
					onBar = true;
					count++;
					// listener.onBarRead(count);
					try {
						Thread.sleep(200);
					} catch (Exception e) {
					}
				}
			}
			if (onBar) {
				if (val[0] < (Constants.BAR_MIN - Constants.MIN_DIFF)) {
					onBar = false;
				}
			}

			// Scan seconds count reset
			if (System.currentTimeMillis() - tLastScanSecs > 1000 * Constants.SCAN_SECS) {
				count = 0;
			}
			try {
				Thread.sleep(1000 / Constants.SCAN_FREQUENCY_HZ);
			} catch (Exception e) {
			}
		}
	}
}