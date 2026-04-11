package operators;

import java.util.Queue;
import java.util.LinkedList;

import concurrency.TrabajoConsumo;
import energy.Consumo;
import energy.ZonaEnergetica;

// ---- VERSION 3: Modificar enviarTrabajo y crear cogerTrabajo ----

// segunda parte, llamo la bateria y si no hay suficiente, bloqueo

public class CentroControl {
    private ZonaEnergetica zona;
    
    Queue<TrabajoConsumo> colaTrabajos = new LinkedList<>(); // cola de trabajos
    boolean colaVacia = true;
    
    public void setZona(ZonaEnergetica zona) {
    	this.zona=zona;
    }

    public int getIdZona() { return zona.getIdZona(); }

    // ---- VERSIÓN 3: Se lleva una parte de este método al hilo ----
    public synchronized TrabajoConsumo enviarTrabajo(Consumo c) {    	
    	// creo trabajo consumo y lo encolo
	    TrabajoConsumo tc = new TrabajoConsumo(c);
	    colaTrabajos.add(tc);
	    notifyAll(); // despertar a los hilos que puedan estar esperando
    	
        return tc;
    }
    
//    private String fmt(double x) {
//        return String.format(java.util.Locale.ROOT, "%.2f", x);
//    }

    public void traza(String msg) {
        System.out.println("[CentroControl Z" + zona.getIdZona() + "] " + msg);
    }
    
    public synchronized TrabajoConsumo cogerTrabajo() {
    	while (colaTrabajos.isEmpty()) { // si no hay trabajos, el hilo espera
    		try {
				wait();
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	return colaTrabajos.poll(); // devuelve el primero de la cola
    }
 
}
