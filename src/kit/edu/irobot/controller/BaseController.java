package kit.edu.irobot.controller;

import kit.edu.irobot.solver.BaseStageSolver;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import lejos.utility.TextMenu;

/**
 * This class is used to setup and start StageSolvers.
 * Subclasses need to implement createStages() to supply the 
 * stage solvers in order of execution.
 * 
 * StageSolvers can be started solo if stopAfter is set to true. 
 * If not set, all stages are executed in order (from the specified stage).
 * 
 * NOTE: In the current version there is a ~5sec delay when starting stages.
 * This is caused by the fact that each StageSolver creates the Sensors at startup
 * and closed the Resources on end.
 * 
 * The menu can be exited with ESC. 
 * The safest way to stop a running StageSolver is to press ENTER+DOWN.
 * 
 * @author Fabian
 *
 */
public abstract class BaseController {
	
	protected int active = -1;
	protected BaseStageSolver[] stages;
	protected final TextMenu menu;
	protected boolean exit = false;
	protected boolean showMenu = false;
	
	public BaseController() {
		stages = createStages();
		
		String[] titles = new String[stages.length];
		for (int i = 0; i < stages.length; ++i)
			titles[i] = stages[i].getName();
		
		menu = new TextMenu(titles, 1, "Select a Stage");
	}
	
	protected abstract  BaseStageSolver[] createStages();
	
	public void menu() {
		menu(0, false);
	}
	
	/**
	 * Shows a menu on LCD to select a stage to run.
	 * After the stage (stopAfter==True) or all stages (stopAfter=False)
	 * are completed, the menu is shown again.
	 * 
	 * The menu can be exited with ESC Button.
	 * 
	 * @param stopAfter if set, only the selected stage is executed.
	 */
	public void menu(boolean stopAfter) {
		menu(0, stopAfter);
	}
	
	public void menu(int stage, boolean stopAfter) {
		stages = createStages();
		showMenu = true;
		int selected = menu.select(stage);
		
		if (selected >= 0 && stage < stages.length)
			start(selected, stopAfter);
	}
	
	public void start(int stage, boolean stopAfter) {
		LCD.clear();
		if (active != -1) {
			LCD.drawString("Stopping: " + stages[active].getName(), 0, 7, true);
			stages[active].stopSolver(true);
		}
		
		active = stage;
		stages[active].start();
		waitForState();
		
		int next = stage+1;
		if (!stopAfter && !exit && next < stages.length)
			start(next, stopAfter);
		
		if (showMenu) menu(stage, stopAfter);
	}
	
	public void start() {
		start(0, false);
	}
	
	public void stop() {
		exit = true;
	}
	
	protected void waitForState() {
		if (active == -1) return;
		
		LCD.drawString("Waiting for: " + stages[active].getName(), 0, 7, true);
		while (true) {
			if (exit) {
				LCD.drawString("Called Stop: " + stages[active].getName(), 0, 7, true);
				stages[active].stopSolver(true);
				Delay.msDelay(500);
				break;
			} 
			
			if (!stages[active].isAlive()) {
				LCD.drawString("Finished: " + stages[active].getName(), 0, 7, true);
				Delay.msDelay(500);
				break;
			}
			
			if (Button.DOWN.isDown()) {
				LCD.drawString("Canceled: " + stages[active].getName(), 0, 7, true);
				stages[active].stopSolver(true);
				Delay.msDelay(500);
				break;
			}
		}
		LCD.clear(6);
		active = -1;
	}
}
