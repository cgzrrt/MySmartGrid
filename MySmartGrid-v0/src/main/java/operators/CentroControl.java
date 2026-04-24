package operators;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.LinkedList;

import concurrency.TrabajoConsumo;
import energy.Consumo;
import energy.ZonaEnergetica;

// ---- VERSION 3: Modificar enviarTrabajo y crear cogerTrabajo ----

// segunda parte, llamo la bateria y si no hay suficiente, bloqueo

public class CentroControl {
    private ZonaEnergetica zona;
    private LinkedBlockingQueue<TrabajoConsumo> queue = new LinkedBlockingQueue<TrabajoConsumo>(); // VERSIÓN 8
    
    Queue<TrabajoConsumo> colaTrabajos = new LinkedList<>(); // cola de trabajos
    boolean colaVacia = true;
    
    public void setZona(ZonaEnergetica zona) {
    	this.zona=zona;
    }

    public int getIdZona() { return zona.getIdZona(); }

    // ---- VERSIÓN 3: Se lleva una parte de este método al hilo ----
    public TrabajoConsumo enviarTrabajo(Consumo c) {    	
    	// creo trabajo consumo y lo encolo
	    TrabajoConsumo tc = new TrabajoConsumo(c);
	    
	    // ---- VERSIÓN 8: LinkedBlockingQueue ----
	    try {
			queue.put(tc);
		} catch (Exception e) { e.printStackTrace(); }
	    
	    // Comentado para la VERSIÓN 8
	    // se ha añadido aqui el synchronized
//	    synchronized(this) {
//		    colaTrabajos.add(tc);
//		    notifyAll(); // despertar a los hilos que puedan estar esperando
//	    }
	    
        return tc;
    }
    
    public void traza(String msg) {
        System.out.println("[CentroControl Z" + zona.getIdZona() + "] " + msg);
    }
    
    public TrabajoConsumo cogerTrabajo() {
    	while (colaTrabajos.isEmpty()) { // si no hay trabajos, el hilo espera
    		// Comentado para la VERSIÓN 8
//    		synchronized (this) {
//    		try {
//				wait();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//    		}
    		
    		// ---- VERSIÓN 8: LinkedBlockingQueue ----
    		try {
				return queue.take();
			} catch (Exception e) { e.printStackTrace(); }   		
    	}
    	return null;
    	// return colaTrabajos.poll(); // devuelve el primero de la cola
    }
 
}
