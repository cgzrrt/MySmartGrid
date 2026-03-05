package main;

import energy.Consumo;
import energy.RedEnergetica;
import java.util.List;
import concurrency.ProcesarConsumo; // Sesión 1
import concurrency.OperadorRed; // Versión 3
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.*; // versión 4

public class MySmartGrid {
	
    public static void main(String[] args) {
        RedEnergetica red = new RedEnergetica(
                Config.NUM_ZONAS,
                Config.CAPACIDAD_BATERIA,
                Config.NIVEL_INICIAL_BATERIA
        );
        
        List<Consumo> consumos = Consumo.consumosDesdeFichero(Config.FICHERO_CONSUMOS);
        System.out.println("Leidos " + consumos.size() + " consumos desde " + Config.FICHERO_CONSUMOS);
      
        // Tramitamos los consumos de manera secuencial
//        String resultado;
//        for (Consumo c:consumos) {
//        	resultado = red.getZona(c.getZona()).tramitarConsumo(c);
//            red.getZona(c.getZona()).getVentana().traza (c.getIdConsumo()+ " - Tramitado: "+resultado);
//        }
//        red.imprimeAuditoria();
        

        // ---- VERSIÓN 3 -----
        List<Thread> operatorList = new ArrayList<Thread>();
        for (int j = 0; j < Config.NUM_ZONAS; j++) {
	        for (int i = 0; i < Config.NUM_OPERADORES_POR_ZONA; i++) {
	        	OperadorRed or = new OperadorRed(red.getZona(j), red.getZona(j).getCentroControl());
	        	Thread o = new Thread(or);
	        	o.start();
	        	operatorList.add(o);
	        	
	        	// 	System.out.println("Operario lanzado en " + red.getZona(j).getIdZona());
	        }
        }
        // System.out.println("acabao");

                
       // System.out.println("Operarios lanzados, vamos a lanzar los procesos");
        
        // ---- VERSIÓN 1: Implementar concurrencia ----
        // para cada elemento de la lista, se lanza un hilo por consumo que llama a tramitar consumo de su zona
        List<Thread> processList = new ArrayList<Thread>();
        for (Consumo c:consumos) { // para cada consumo, se lanza un hilo
        	ProcesarConsumo pc = new ProcesarConsumo(red, c);
        	Thread t = new Thread(pc);
        	t.start();
        	processList.add(t); // se añade a la lista de hilos
        	
        	System.out.println("Consumo " + c.getIdConsumo());
        }
        try {
        	for (int i=0; i<processList.size(); i++) {
        		processList.get(i).join(); // con join se espera para que se impriman las auditorías al final
        	}
        } catch (Exception e) {e.printStackTrace();}     
        
        try {
			for (int i = 0; i < operatorList.size(); i++) {
				operatorList.get(i).join();
				System.out.println("----- OH WOW ------");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        System.out.println("AUDITORIAS");
        
        red.imprimeAuditoria();
        
        // ---- VERSIÓN 4: Streams ----
        // Imprimir consumos de menos de 5kwh
        consumos.stream()
        	.parallel()
        	.filter(c->c.getTotalKWh() < 5)
        	.forEach(c->System.out.println(c.getIdConsumo()));
        // Imprimir máximo de kwh
        consumos.stream()
        	.parallel()
        	.max(Comparator.comparingDouble(c->c.getTotalKWh()));
        // encontrar direccion
        consumos.stream()
        .parallel()
        .filter(c->c.getDireccion() == "Sagitario,24")
        .findAny()
        .ifPresent(c->System.out.println("Encontrado"));
        consumos.stream()
        .parallel()
        .filter(c->c.getDireccion() == "Berna,11")
        .findAny()
        .ifPresent(c->System.out.println("Encontrado"));
        
      
        
    }
}
