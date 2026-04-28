package concurrency;

import storage.BateriaEolica;

public class RobotEolico implements Runnable{
	private BateriaEolica batEol;
	
	public RobotEolico (BateriaEolica _batEol) {
		batEol = _batEol;
	}
	@Override
	public void run() {
		while (true) {
			batEol.depositar();
		}
	}
}
