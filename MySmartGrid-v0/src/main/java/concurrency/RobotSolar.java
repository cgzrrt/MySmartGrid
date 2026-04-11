package concurrency;

import storage.BateriaSolar;

public class RobotSolar implements Runnable{
	private BateriaSolar batSol;
	
	public RobotSolar (BateriaSolar _batSol) {
		batSol = _batSol;
	}
	@Override
	public void run() {
		while (true) {
			batSol.depositar();
		}
	}
}
