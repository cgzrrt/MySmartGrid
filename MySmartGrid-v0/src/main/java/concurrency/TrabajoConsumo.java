package concurrency;

import energy.Consumo;

public class TrabajoConsumo {
	// ---- VERSIÓN 3: AwaitResultado y SetResultado ----
	Double totalKw;
	String idConsum;
	String resultado = "";
	boolean resultObtenido = false;
	Consumo consum;
	
	public TrabajoConsumo (Consumo _consum) {
//		totalKw = _totalKw;
		consum = _consum;
	}

	public synchronized void awaitResultado() {
		// mientras no obtenga el resultado, se espera
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
		notifyAll();
	}
	
	// métodos auxiliares
	public Consumo getConsumo() {
		return consum;
	}
	public String getResultado() {
		return resultado;
	}
	public String getIdConsumo() {
		return idConsum;
	}
}
