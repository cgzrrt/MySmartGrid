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
    
    public void setZona(ZonaEnergetica zona) {
    	this.zona=zona;
    }

    public int getIdZona() { return zona.getIdZona(); }

    // ---- VERSIÓN 3: Se lleva una parte de este método al hilo ----
    public String enviarTrabajo(Consumo c) {
    	String resultado = "";
    	
    	// creo trabajo consumo y lo encolo
    	TrabajoConsumo tc = new TrabajoConsumo(c);
    	colaTrabajos.add(tc);
    	
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
        return resultado;
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
//    	while (colaTrabajos.isEmpty()) {
//    		    		try {
//				wait();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//    	}
    	
    	return colaTrabajos.poll();
    }
}
