package storage;

import java.util.concurrent.locks.*;

//----- VERSIÓN 5: Monitores -----

public class BateriaEolica {
	private final double capacidadMaxKWh;
    private double nivelActualKWh;
    
    Lock monitor = new ReentrantLock(); // monitor
    // si no hay energia para retirar, se bloquea
    Condition vacio = monitor.newCondition();
    // si no hay espacio para depositar energía, se bloquea
    Condition lleno = monitor.newCondition();
    
    public BateriaEolica(double _capacidadMaxKWh, double _nivelActualKWh) {
		capacidadMaxKWh = _capacidadMaxKWh;
    	nivelActualKWh = _nivelActualKWh;
    }
    
 // retira una cantidad pedida
    public double retirar(double kWh) {
    	double suministrado = 0;
    	monitor.lock(); // bloqueamos el monitor
    	try {
    		// esperamos si no hay energía para suministrar
    		while (nivelActualKWh == 0) {
    			try {
					vacio.await();
				} catch (InterruptedException e) { e.printStackTrace();	}
    		}
    		suministrado = Math.min(kWh, nivelActualKWh);
            nivelActualKWh -= suministrado;
    		
    		lleno.signal();
    	} finally {
			monitor.unlock(); // desbloqueo
		}
    	return suministrado;
    }
    
    // carga 1kWh cada vez
    public void depositar() {
    	monitor.lock(); // bloqueamos el monitor
    	try {
			// esperamos si está lleno
    		while ( nivelActualKWh == capacidadMaxKWh ) {
    			try {
					lleno.await();
				} catch (Exception e) { e.printStackTrace(); }
    		}
    		nivelActualKWh = Math.min(capacidadMaxKWh, nivelActualKWh + 1); // solo 1kWh
    		vacio.signal();
		} finally {
			monitor.unlock(); // desbloqueo
		}
    }

}
