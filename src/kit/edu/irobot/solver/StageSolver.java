package kit.edu.irobot.solver;

import kit.edu.irobot.robot.Robot;
import lejos.robotics.subsumption.Arbitrator;

public abstract class StageSolver extends Thread{
	private String name;
	public BetterArbitrator arby;
	private Robot robot;
	
	private boolean abort;
	
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

	abstract public void stopSolver();
	abstract public void run();
}
