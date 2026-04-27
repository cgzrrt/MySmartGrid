package concurrency;

import java.util.List;
import java.util.concurrent.Callable;

import energy.Consumo;

public class CallableConsumo implements Callable<Consumo> {

	private List<Consumo> consumos;
	
	public CallableConsumo(List<Consumo> _consumos) {
		consumos = _consumos;
	}
	
	@Override
	public Consumo call() throws Exception {
		// recorrer lista de consumos
		Consumo cmax = consumos.getFirst();
		for (Consumo c:consumos) {
			if (c.getTotalKWh() > cmax.getTotalKWh()) {
				cmax = c;
			}
		}
		return cmax;
	}

}
