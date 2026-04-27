package storage;

import concurrency.EsperaOpCDL;
import pcd.util.ColoresConsola;
import pcd.util.Traza;
import pcd.util.Ventana;

public class Bateria {
    private final double capacidadMaxKWh;
    private double nivelActualKWh;
    Ventana v;
    private boolean llenar; // VERSIÓN 3
    private EsperaOpCDL cdl; // VERSIÓN 8

    public Bateria(double capacidadMaxKWh, double nivelInicialKWh) {
        if (capacidadMaxKWh <= 0) throw new IllegalArgumentException("capacidadMaxKWh debe ser > 0");
        if (nivelInicialKWh < 0) throw new IllegalArgumentException("nivelInicialKWh debe ser >= 0");
        this.capacidadMaxKWh = capacidadMaxKWh;
        this.nivelActualKWh = Math.min(nivelInicialKWh, capacidadMaxKWh);
    }

    public double getCapacidadMaxKWh() { return capacidadMaxKWh; }
    public double getNivelActualKWh() { return nivelActualKWh; }

    public boolean puedeSuministrar(double kWh) {
        return kWh <= nivelActualKWh;
    }

    // ---- VERSIÓN 2: Es mejor proteger batería y cuenta energética  ----
    // ---- VERSIÓN 3: Si el nivel es menor, se bloquea y llama al operador red ----
    public synchronized double suministra(double kWh) {
        if (kWh <= 0) return 0.0;
        if(nivelActualKWh < kWh) { // si hay menos de lo que se pide, se bloquea
        	Traza.traza(ColoresConsola.CYAN_BOLD, 0, "** NO HAY SUFICIENTE BATERÍA ** PIDEN " + kWh + " Y HAY " + nivelActualKWh);
        	llenar = true; // flag a true
        	try {
				wait();
			} catch (Exception e) { e.printStackTrace(); }
        }
        double suministrado = Math.min(kWh, nivelActualKWh);
        nivelActualKWh -= suministrado;
        return suministrado;
    }

    public synchronized void carga(double kWh) {
    	// cdl.bloquearBateria(); // VERSIÓN 8
        if (kWh <= 0) return;
        if(llenar) {
	        nivelActualKWh = Math.min(capacidadMaxKWh, nivelActualKWh + kWh);
	        Traza.traza(ColoresConsola.RED_BOLD, 0, "** BATERÍA CARGADA ** AHORA HAY " + nivelActualKWh );
	        llenar = false;
	        notify();
        }
    }

    public boolean hayQueCargar() {
    	return llenar;
    }
    
    public void setVentana (Ventana _v) {
    	v=_v;
    }

    @Override
    public String toString() {
        return "Bateria{capacidadMaxKWh=" + capacidadMaxKWh + ", nivelActualKWh=" + nivelActualKWh + "}";
    }
}
