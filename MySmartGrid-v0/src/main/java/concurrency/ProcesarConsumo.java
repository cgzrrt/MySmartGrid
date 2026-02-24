package concurrency;

import energy.RedEnergetica;
import energy.Consumo;

// Clase creada para la sesión 1
public class ProcesarConsumo implements Runnable {
	
	private Consumo consumo;
	private RedEnergetica red;
	
	  /*
     * Se crea una clase Thread o se imlpementa Runnable. Esta vez, será con Runnable. Clase ProcesarConsumo
     * Implementa Runnable donde pasamos por parámetro lo que necestie y un método run()
     * En run llama a tramitar consumo, le tenemos que pasar el consumo y la zona energética
     * 
     * Hay que sincronizar para asegurar que se terminen los hilos para imprimir la auditoría. Esto se hace con join()
     * Se meten los hilos en una lista de hilos y se hace join().
     */
	
	public ProcesarConsumo(RedEnergetica _red, Consumo _consumo) {
		consumo = _consumo;
		red = _red;
	}
	
	@Override
	public void run() {
		String resultado;
		resultado = red.getZona(consumo.getZona()).tramitarConsumo(consumo); // tramitamos el consumo con la zona del consumo indicado
		red.getZona(consumo.getZona()).getVentana().traza(consumo.getIdConsumo() + " - Tramitado: " + resultado); // se imprime la tramitación
	}

}
