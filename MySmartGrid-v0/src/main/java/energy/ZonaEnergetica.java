package energy;

import operators.CentroControl;
import pcd.util.Ventana;
import storage.Bateria;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import concurrency.OperadorRed;
import main.Config;

public class ZonaEnergetica {
    private final int idZona;
    private final CuentaEnergetica cuenta;
    private final Bateria bateria;
    private final CentroControl centroControl;
    private Ventana v;

    public ZonaEnergetica(int idZona, CuentaEnergetica cuenta, Bateria bateria, CentroControl centroControl, Ventana _v) {
        this.idZona = idZona;
        this.cuenta = Objects.requireNonNull(cuenta);
        this.bateria = Objects.requireNonNull(bateria);
        this.centroControl = Objects.requireNonNull(centroControl);
        this.v = _v;
        
        
        // TODO ¿se lanzan aquí?
    	// ---- VERSIÓN 3: Lanzar hilos de OperadorRed ----
        // Para cada zona, varios operadores
//        List<Thread> operatorList = new ArrayList<Thread>();
//        for (int i = 0; i < Config.NUM_OPERADORES_POR_ZONA; i++) {
//        	OperadorRed or = new OperadorRed(this, centroControl);
//        	Thread t = new Thread(or);
//        	t.start();
//        	operatorList.add(t);
//        }
//        try {
//			for (int i = 0; i < operatorList.size(); i++) {
//				operatorList.get(i).join();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}	
        
    }

    public int getIdZona() { return idZona; }
    public CuentaEnergetica getCuenta() { return cuenta; }
    public Bateria getBateria() { return bateria; }
    public CentroControl getCentroControl() { return centroControl; }

    // ---- VERSIÓN 2: Aquí también se podría poner synchronized ----
    public String tramitarConsumo(Consumo c) {
    	// ---- VERSIÓN 3: Llamar enviarTrabajo ----
    	
        if (c.getZona() != idZona) {
            return "ERROR: consumo dirigido a zona " + c.getZona() + " pero tramitado en zona " + idZona;
        }

        v.traza (c.getIdConsumo()+" solicita: "+c.getTotalKWh(), Color.BLUE);
        String resultado = centroControl.enviarTrabajo(c);

        return resultado;
    }

   
    @Override
    public String toString() {
        return "ZonaEnergetica{idZona=" + idZona + ", cuenta=" + cuenta + ", bateria=" + bateria + "}";
    }
    
    public Ventana getVentana () {
    	return v;
    }
    
    public synchronized void traza (String s) {
    	v.traza(s);
    }
    
    public synchronized void traza (String s, Color color) {
    	v.traza(s,color);
    }


}
