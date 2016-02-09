package kit.edu.irobot.solver;

import kit.edu.irobot.robot.Robot;
import lejos.robotics.subsumption.Arbitrator;

public abstract class StageSolver extends Thread{
	private String name;
	public BetterArbitrator arby;
	protected Robot robot;
	
	protected boolean abort;
	
	public StageSolver(String name){
		this.name = name;
		this.robot = Robot.getInstance();
		this.abort = false;
	}
	
	public BetterArbitrator getArbitrator(){
		return this.arby;
	}
	
	public Robot getRobot(){
		return robot;
	}
	
	public void setRobot(Robot robot){
		this.robot = robot;
	}

	public void stopSolver() {
		abort = true;
	}
	
	abstract public void run();
	
	
	public interface ExitCallback {
		void exitArby();
	}

	protected ExitCallback exitCallback = new ExitCallback() {
		
		@Override
		public void exitArby() {
			StageSolver.this.arby.stop();
		}
	};
}
