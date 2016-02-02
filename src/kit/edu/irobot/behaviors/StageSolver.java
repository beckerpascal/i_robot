package kit.edu.irobot.behaviors;

import lejos.robotics.subsumption.Arbitrator;

public abstract class StageSolver {
	private String name;
	public Arbitrator arby;
	
	
	public StageSolver(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public Arbitrator getArbitrator(){
		return this.arby;
	}

	abstract public void solve();
}
