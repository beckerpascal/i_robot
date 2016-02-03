package kit.edu.irobot.solver;

import kit.edu.irobot.robot.Robot;
import lejos.robotics.subsumption.Arbitrator;

public abstract class StageSolver {
	private String name;
	public Arbitrator arby;
	private Robot robot;
	
	public StageSolver(String name){
		this.name = name;
		this.robot = robot.getInstance();
	}
	
	public String getName(){
		return this.name;
	}
	
	public Arbitrator getArbitrator(){
		return this.arby;
	}

	public Robot getRobot(){
		return robot;
	}
	
	abstract public void solve();
}
