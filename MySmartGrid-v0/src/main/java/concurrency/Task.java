package concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RecursiveTask;

import energy.Consumo;

public class Task extends RecursiveTask <List<Consumo>> {
	private static final long serialVersionUID = 1L;
	List<Consumo> listaConsumos;
	List<Consumo> listaRetorno;
	
	public Task (List<Consumo> _listaConsumos) {
		listaConsumos = _listaConsumos;
		listaRetorno = new ArrayList<>();
	}

	@Override
	public List<Consumo> compute() {
		// caso trivial
        if (listaConsumos.size() < 10) {
        	for (Consumo c:listaConsumos) {
        		if (c.getTotalKWh() > 20) {
        			listaRetorno.add(c);
        		}
        	}
        }
        else {
        	int medio = listaConsumos.size()/2;
        	Task g1 = new Task(listaConsumos.subList(0, medio));
        	Task g2 = new Task(listaConsumos.subList(medio, listaConsumos.size()));
        	
        	invokeAll(g1,g2);
        	
        	try {
				listaRetorno.addAll(g1.get());
				listaRetorno.addAll(g2.get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
        }
		return listaRetorno;
	}
	
}
