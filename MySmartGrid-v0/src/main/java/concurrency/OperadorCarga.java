package concurrency;

import energy.ZonaEnergetica;
import main.Config;

public class OperadorCarga implements Runnable{

	private ZonaEnergetica zona;
	
	// Constructor
	public OperadorCarga(ZonaEnergetica _zona) {
		zona = _zona;
	}
	
	@Override
	public void run() {
		// Traza.traza(ColoresConsola.YELLOW, 0, "** OPERADOR CARGA CREADO PARA ZONA " + zona.getIdZona() + " **");
		while(true) {
			// cargamos la batería
			zona.getBateria().carga(Config.NIVEL_INICIAL_BATERIA); // cargamos este valor
		}
	}	
}
