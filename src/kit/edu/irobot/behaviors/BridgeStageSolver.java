package kit.edu.irobot.behaviors;

import kit.edu.irobot.behaviors.BossStageSolver.DoSomeThing;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

/**
 * Behavior for driving over the bridge 
 * @author Pascal Becker
 *
 */

public class BridgeStageSolver extends StageSolver{
	public class DoSomeThing  implements Behavior{

		@Override
		public boolean takeControl() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void action() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void suppress() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	public BridgeStageSolver() {
		super("BridgeStageSolver");
		
		Behavior b1 = new DoSomeThing();

		Behavior[] bArray = {b1};
		super.arby = new Arbitrator(bArray);
		super.arby.start();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void solve() {
		// TODO Auto-generated method stub
		
	}
}
