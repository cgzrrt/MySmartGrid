package concurrency;

import java.awt.Color;
import java.util.concurrent.Semaphore;

import pcd.util.Ventana;

public class TodosConsum {
	private final Semaphore s2;
	private Ventana v;
	
	public TodosConsum (int _numConsum, Ventana _v) {
		s2 = new Semaphore(_numConsum, true);
		v = _v;
	}
	
	public void maxTramitado(String idConsumo) {	
		v.traza("** CONSUMO: " + idConsumo + " SE VA A BLOQUEAR", Color.RED);
		try {
			s2.acquire(); // bloqueamos
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public void tramitadoFinalizado(String idConsumo) {
		v.traza("** CONSUMO: " + idConsumo + " SE HA TRAMITADO", Color.GREEN);
		try {
			s2.release();
		} catch (Exception e) { e.printStackTrace(); }
	}
}
