package concurrency;

import java.util.concurrent.CountDownLatch;

public class EsperaOpCDL {
	private CountDownLatch esperaInicio;
	
	public EsperaOpCDL (int numOperadores) {
		esperaInicio = new CountDownLatch(numOperadores);
	}
	
	public void bloquearBateria() {
		try {
			System.out.println("BLOQUEANDO BATERIA");
			esperaInicio.await();
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public void operadorEjecutando() {
		System.out.println("OPERADOR ESTÁ EJECUTANDO");
		esperaInicio.countDown();
	}
}
