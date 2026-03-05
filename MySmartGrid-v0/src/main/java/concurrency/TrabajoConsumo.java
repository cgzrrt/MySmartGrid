package concurrency;

import energy.Consumo;

public class TrabajoConsumo {

	// se bloquea hasta que esté disponible el trabajo
	// cuando deja el trabajo, esperas
	// en setResultado desbloquea
	// se bloquea sí o sí
	// espera de forma incondicional mientras no esté el resultado
	
	// ---- VERSIÓN 3: AwaitResultado y SetResultado ----
	Double totalKw;
	String resultado = "";
	boolean resultObtenido = false;
	
	public TrabajoConsumo (Double _totalKw) {
		totalKw = _totalKw;
	}

	public synchronized void awaitResultado() {
		// bloqueo 
		// comprobar con un bucle
		while (!resultObtenido) {
			try {
				wait();
			} catch (Exception e) { e.printStackTrace(); }
		}
	}
	
	// da igual si es notify o notifyAll porque solo hay un hilo
	public synchronized void setResultado(String _resultado) {
		// desbloqueo
		resultObtenido = true;
		resultado = _resultado;
		notify();
	}
	
	// métodos auxiliares
	public Double getConsumo() {
		return totalKw;
	}
	public String getResultado() {
		return resultado;
	}
}
