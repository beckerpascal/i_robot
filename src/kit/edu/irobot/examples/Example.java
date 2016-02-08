package kit.edu.irobot.examples;

import java.io.File;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.utility.Delay;

public class Example {

	public static void main(String[] args) {
		File file = new File("./home/lejos/programs/batman_theme_x.wav");
		Sound.playSample(file);

	}

}
