package concurrency;

import energy.ZonaEnergetica;
import main.Config;

public class OperadorCarga implements Runnable{

	private ZonaEnergetica zona;
	private EsperaOpCDL cdl;
	
	// Constructor
	public OperadorCarga(ZonaEnergetica _zona, EsperaOpCDL _cdl) {
		zona = _zona;
		cdl = _cdl;
	}
	
	@Override
	public void run() {
		// Traza.traza(ColoresConsola.YELLOW, 0, "** OPERADOR CARGA CREADO PARA ZONA " + zona.getIdZona() + " **");
		cdl.bloquearBateria(); // VERSIÓN 8
		
		while(true) {
			// cargamos la batería
			zona.getBateria().carga(Config.NIVEL_INICIAL_BATERIA); // cargamos este valor
		}
	}	
}
