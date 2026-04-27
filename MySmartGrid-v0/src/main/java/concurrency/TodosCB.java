package concurrency;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import pcd.util.ColoresConsola;
import pcd.util.Traza;

public class TodosCB {
	private int numOperadores;
	private CyclicBarrier cb;
	
	public TodosCB(int _numOperadores) {
		numOperadores = _numOperadores;
		cb = new CyclicBarrier( numOperadores, 
				new Runnable() {
				public void run() {
					Traza.traza(ColoresConsola.BLUE, 0, "Liberando operadores");
				}
		});
	}
	
	public void operadoresLanzados() {
		try {
            Traza.traza(ColoresConsola.BLUE, 0, "Bloqueando operadores");
            cb.await();
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
	}
}
