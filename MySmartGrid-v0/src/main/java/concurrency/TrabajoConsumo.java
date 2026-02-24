package concurrency;


public class TrabajoConsumo {

	// se bloquea hasta que esté disponible el trabajo
	// cuando deja el trabajo, esperas
	// en setResultado desbloquea
	// se bloquea sí o sí
	// espera de forma incondicional mientras no esté el resultado
	
	// ---- VERSIÓN 3: AwaitResultado y SetResultado ----
	
	public void awaitResultado() {
		// bloqueo
		try {
			wait();
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	// da igual si es notify o notifyAll porque solo hay un hilo
	public void setResultado(String resultado) {
		// desbloqueo
		System.out.println(resultado);
		notify();
	}
}
