package energy;

import operators.CentroControl;
import pcd.util.Ventana;
import storage.*;

import java.awt.Color;
import java.util.Objects;

import concurrency.TrabajoConsumo;
import main.Config;
import concurrency.EsperaOpCDL;
import concurrency.TodosConsum;

public class ZonaEnergetica {
    private final int idZona;
    private final CuentaEnergetica cuenta;
    private final Bateria bateria;
    private final BateriaSolar bateriaSol;
    private final BateriaEolica bateriaEol;
    private final CentroControl centroControl;
    private Ventana v;
    
    private TodosConsum todCon;

    // modificado para la VERSIÓN 5, ahora incluye bateria solar y eolica
    public ZonaEnergetica(int idZona, CuentaEnergetica cuenta, Bateria bateria,
    		CentroControl centroControl, Ventana _v, BateriaSolar bateriaSol, BateriaEolica bateriaEol) {
        this.idZona = idZona;
        this.cuenta = Objects.requireNonNull(cuenta);
        this.bateria = Objects.requireNonNull(bateria);
        this.bateriaSol = bateriaSol; // VERSIÓN 5
        this.bateriaEol = bateriaEol; // VERSIÓN 5
        this.centroControl = Objects.requireNonNull(centroControl);
        this.v = _v;
        
        todCon = new TodosConsum(Config.NUM_MAX_CONSUMOS,  _v) ;
    }

    public int getIdZona() { return idZona; }
    public CuentaEnergetica getCuenta() { return cuenta; }
    public Bateria getBateria() { return bateria; }
    public BateriaSolar getBateriaSol() { return bateriaSol; } // VERSIÓN 5
    public BateriaEolica getBateriaEol() { return bateriaEol; } // VERSIÓN 5
    public CentroControl getCentroControl() { return centroControl; }

    // ---- VERSIÓN 2: Aquí también se podría poner synchronized ----
    public String tramitarConsumo(Consumo c) {
        if (c.getZona() != idZona) {
            return "ERROR: consumo dirigido a zona " + c.getZona() + " pero tramitado en zona " + idZona;
        }

        v.traza (c.getIdConsumo()+" solicita: "+c.getTotalKWh(), Color.BLUE);
        
        // ---- VERSIÓN 3: Llamar enviarTrabajo ----
        // todCon.maxTramitado(c.getIdConsumo()); // ---- VERSION 6 ----
        
    	TrabajoConsumo tc = centroControl.enviarTrabajo(c);
    	tc.awaitResultado();
    	String resultado = tc.getResultado();
        
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
