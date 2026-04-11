package concurrency;

import operators.CentroControl;
import energy.ZonaEnergetica;
import java.util.Random;

public class OperadorRed implements Runnable {
	private CentroControl control;
	private ZonaEnergetica zona;
	private TodosOp todos;
	private TodosConsum todCon;
	
	public OperadorRed (ZonaEnergetica _zona, CentroControl _control, TodosOp _todos, TodosConsum _todCon) {
		zona = _zona;
		control = _control;
		todos = _todos;
		todCon = _todCon;
	}
	
	@Override
	public void run() {
		while (true) {
			// System.out.println("--- OPERADOR DE ZONA " + zona.getIdZona());
			todos.operadoresLanzados();
			
			// coge trabajo
			TrabajoConsumo tc = control.cogerTrabajo();
			Double consum = tc.getConsumo().getTotalKWh();
			
			todCon.maxTramitado(tc.getConsumo().getIdConsumo()); // ---- VERSION 6 ----
			
			Random r = new Random();
	        // Simulamos un tiempo de tramitación del consumo
	        try {
				Thread.sleep(r.nextInt(200));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	       
	        // double total = consum;
	        // double suministrado = zona.getBateria().suministra(total);
	        
	        // ---- VERSIÓN 5: Identificar el tipo de demanda ----
	        double suministrado = 0;
	        double total = consum;
	        for (int i = 0; i < tc.getConsumo().getDemandas().size(); i++) {
	        	// System.out.println(tc.getConsumo().getDemandas().get(i).toString());
		     	String d = tc.getConsumo().getDemandas().get(i).getIdTipo();
		     	total = tc.getConsumo().getDemandas().get(i).getKWh();
	     		switch (d) {
		     		case "SOLAR":
		     			suministrado += zona.getBateriaSol().retirar(total);
		     			// Traza.traza(ColoresConsola.YELLOW, 0, "** CONSUMO SOLAR **");
		     		break;
		     		case "EOLICA":
		     			suministrado += zona.getBateriaEol().retirar(total);
		     			// Traza.traza(ColoresConsola.WHITE_BOLD, 0, "** CONSUMO EÓLICO **");
		     		break;
		     		case "RAPIDA":
		     			suministrado += zona.getBateria().suministra(total);
		     			// Traza.traza(ColoresConsola.PURPLE, 0, "** CONSUMO RÁPIDO **");
		     		break;
		     	}
	     	}
	     	// ---------------------------------------------------

	        zona.getCuenta().anotaConsumo(suministrado);
	        
	        String resultado = (suministrado >= total)
	                ? "OK: suministrados " + fmt(total) + " kWh"
	                : "PARCIAL: suministrados " + fmt(suministrado) + " kWh (faltan " + fmt(total - suministrado) + " kWh)";	        
	        traza ("Trabajo completado para: " + tc.getConsumo().getIdConsumo());
	        
	        todCon.tramitadoFinalizado(tc.getConsumo().getIdConsumo());
	        
	        tc.setResultado(resultado);
		}
	}
	
	// copiado del CentroControl
	private String fmt(double x) {
        return String.format(java.util.Locale.ROOT, "%.2f", x);
    }
	
	public void traza(String msg) {
        System.out.println("[CentroControl Z" + zona.getIdZona() + "] " + msg);
    }
}
