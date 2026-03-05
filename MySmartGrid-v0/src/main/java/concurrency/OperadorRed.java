package concurrency;

import operators.CentroControl;
import energy.Consumo;
import energy.ZonaEnergetica;
import concurrency.TrabajoConsumo;
import java.util.Random;

public class OperadorRed implements Runnable {
	CentroControl control;
	ZonaEnergetica zona;

	
	public OperadorRed (ZonaEnergetica _zona, CentroControl _control) {
		zona = _zona;
		control = _control;
	}
	
	@Override
	public void run() {
		while (true) {
			// coge trabajo
			TrabajoConsumo tc = control.cogerTrabajo();
			Double consum = tc.getConsumo();
			
			Random r = new Random();
	        // Simulamos un tiempo de tramitación del consumo
	        try {
				Thread.sleep(r.nextInt(200));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	       
	        double total = consum;
	        double suministrado = zona.getBateria().suministra(total);
	        zona.getCuenta().anotaConsumo(suministrado);
	        
	        String resultado = (suministrado >= total)
	                ? "OK: suministrados " + fmt(total) + " kWh"
	                : "PARCIAL: suministrados " + fmt(suministrado) + " kWh (faltan " + fmt(total - suministrado) + " kWh)";
	        System.out.println("Trabajo completado para: "+consum);
	        
	        tc.setResultado(resultado);
		}
	}
	
	// copiado del CentroControl
	private String fmt(double x) {
        return String.format(java.util.Locale.ROOT, "%.2f", x);
    }
}
