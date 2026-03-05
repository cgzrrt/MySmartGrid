package operators;

import java.util.Random;
import java.util.Queue;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import concurrency.OperadorRed;
import concurrency.TrabajoConsumo;
import energy.Consumo;
import energy.ZonaEnergetica;
import main.Config;

// ---- VERSION 3: hacer cola de trabajo consumo 
// y añadir esperarResultado y cogerTrabajo
// setResultado en trabajoConsumo
// enviar trabajo es encolar el trabajoConsumo

// segunda parte, llamo la bateria y si no hay suficiente, bloqueo

public class CentroControl {
    private ZonaEnergetica zona;
    
    // TODO ¿qué hago con los TCs? ¿Dónde pongo sus métodos?
    Queue<TrabajoConsumo> colaTrabajos = new LinkedList<>(); // cola de trabajos
    boolean colaVacia = true;
    
    public void setZona(ZonaEnergetica zona) {
    	this.zona=zona;
    }

    public int getIdZona() { return zona.getIdZona(); }

    // ---- VERSIÓN 3: Se lleva una parte de este método al hilo ----
    public synchronized TrabajoConsumo enviarTrabajo(Consumo c) {    	
    	// creo trabajo consumo y lo encolo
    	
    	// System.out.println("añadiendo trabajo en zona: " + zona.getIdZona() + " con consumo: " + c.getTotalKWh());
    	
	    TrabajoConsumo tc = new TrabajoConsumo(c.getTotalKWh());
	    colaTrabajos.add(tc);
	    try {
			notifyAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	
    	// System.out.println("----- Trabajo añadido en zona: " + zona.getIdZona() + ", total: " + colaTrabajos.size());
    	
    	// ESTA PARTE SE LLEVA AL RUN DE LOS HILOS
//    	Random r = new Random();
//        // Simulamos un tiempo de tramitación del consumo
//        try {
//			Thread.sleep(r.nextInt(200));
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//       
//        double total = c.getTotalKWh();
//        double suministrado = zona.getBateria().suministra(total);
//        zona.getCuenta().anotaConsumo(suministrado);
//        
//        String resultado = (suministrado >= total)
//                ? "OK: suministrados " + fmt(total) + " kWh"
//                : "PARCIAL: suministrados " + fmt(suministrado) + " kWh (faltan " + fmt(total - suministrado) + " kWh)";
//    	traza ("Trabajo completado para: "+c.getIdConsumo());
        return tc;
    }
    
    private String fmt(double x) {
        return String.format(java.util.Locale.ROOT, "%.2f", x);
    }

    public void traza(String msg) {
        System.out.println("[CentroControl Z" + zona.getIdZona() + "] " + msg);
    }
    
    public TrabajoConsumo cogerTrabajo() {
    	// borrar de la cola el trabajo consumo
    	// y devolverlo
    	// procesar el hilo
    	while (colaTrabajos.isEmpty()) {
    		synchronized (this) {	
	    		try {
					wait();
				} catch (Exception e) {
					e.printStackTrace();
				}
    		}
    	}
    	return colaTrabajos.poll();
    }
 
}
