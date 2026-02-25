package concurrency;

import operators.CentroControl;
import energy.Consumo;
import energy.ZonaEnergetica;
import concurrency.TrabajoConsumo;
import java.util.Random;

// TODO ¿Dónde se lanzan?

public class OperadorRed implements Runnable {
	CentroControl control;
	ZonaEnergetica zona;
	Consumo consum;
	TrabajoConsumo tc;
	
	
	public OperadorRed (CentroControl _control, ZonaEnergetica _zona) {
		control = _control;
	}
	
	@Override
	public void run() {
		while (true) {
			tc.awaitResultado();
			
			// coge trabajo
			control.cogerTrabajo(consum);
			
			
			// batería
			// cuenta
			// setResultado
			Random r = new Random();
	        // Simulamos un tiempo de tramitación del consumo
	        try {
				Thread.sleep(r.nextInt(200));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	       
	        // TODO ¿Cómo obtengo la c?
	        double total = consum.getTotalKWh();
	        double suministrado = zona.getBateria().suministra(total);
	        zona.getCuenta().anotaConsumo(suministrado);
	        
	        String resultado = (suministrado >= total)
	                ? "OK: suministrados " + fmt(total) + " kWh"
	                : "PARCIAL: suministrados " + fmt(suministrado) + " kWh (faltan " + fmt(total - suministrado) + " kWh)";
	        System.out.print("Trabajo completado para: "+consum.getIdConsumo());
	        
	        tc.setResultado(resultado);
		}
	}
	
	// copiado del CentroControl
	private String fmt(double x) {
        return String.format(java.util.Locale.ROOT, "%.2f", x);
    }
}
