package concurrency;

import java.util.concurrent.Semaphore;

public class TodosOp {
	private int numOperadores;
	private final Semaphore s1;
	private final Semaphore s2;
	private int lanzados;
	
	public TodosOp(int _numOperadores) {
		numOperadores = _numOperadores;
		s1 = new Semaphore(1);
		s2 = new Semaphore(0);
		lanzados = 0;
	}
	
	public void operadoresLanzados() {
		try {
			s1.acquire(); // bloqueamos al operador
		} catch (InterruptedException e) { e.printStackTrace(); }
		
		lanzados++; // aumentamos el número de operadores lanzados
		
		if (lanzados == numOperadores) {
			s2.release();
			// System.out.println("---- LIBERANDO OPERADORES");
		}
		s1.release();
		
		if ( lanzados < numOperadores ) {
			// System.out.println("--- BLOQUEANDO OPERADOR ----");
			try {
				s2.acquire();
			} catch (InterruptedException e) { e.printStackTrace();	}
		}
	}
	
	
	
}
