package kit.edu.irobot.utils;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class LineCodeScanner extends Thread {
	private final SampleProvider light;
	private final float[] val;
	
	private int count = 0;

	public LineCodeScanner(EV3ColorSensor color) {
		light = color.getRedMode();
		val = new float[1]; //safer to set size
	}
	
	public int getCurrentStage(){		
		return count;
	}

	public void run() {

		light.fetchSample(val, 0);
		boolean onBar = false;
		long tLastSensorRead = System.currentTimeMillis();
		long tLastScanSecs = tLastSensorRead;
		while (!Button.ESCAPE.isDown()) {

			light.fetchSample(val, 0);
			if (!onBar) {
				if (val[0] > Constants.BAR_MIN) {
					onBar = true;
					Sound.beepSequenceUp();
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