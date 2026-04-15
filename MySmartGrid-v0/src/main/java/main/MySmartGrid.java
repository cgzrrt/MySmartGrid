package main;

import concurrency.ProcesarConsumo; // Sesión 1
import concurrency.OperadorRed; // Versión 3
import concurrency.OperadorCarga;
import concurrency.RobotEolico;
import concurrency.RobotSolar;
import concurrency.TodosConsum;
import concurrency.TodosOp;

import energy.Consumo;
import energy.RedEnergetica;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.ArrayList;
import java.util.Comparator;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observables.ConnectableObservable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MySmartGrid {
	
    public static void main(String[] args) {
        RedEnergetica red = new RedEnergetica(
                Config.NUM_ZONAS,
                Config.CAPACIDAD_BATERIA,
                Config.NIVEL_INICIAL_BATERIA
        );
        
        List<Consumo> consumos = Consumo.consumosDesdeFichero(Config.FICHERO_CONSUMOS);
        System.out.println("Leidos " + consumos.size() + " consumos desde " + Config.FICHERO_CONSUMOS);
      
        // ---- VERSIÓN 7: Observables ----
        // Suma de todos los consumos
        Observable<Consumo> obs1 = Observable.fromIterable(consumos);
        obs1
        	.subscribeOn(Schedulers.computation())
        	.map( c -> c.getTotalKWh() )
        	.reduce(0.0, Double::sum) // realiza la suma
        	.subscribe( c -> System.out.println("Hilo actual: " + Thread.currentThread() + ". Sumado: " + c) );
        // Imprimir consumos mayores a 20kw
        Observable<Consumo> obs2 = Observable.fromIterable(consumos);
        obs2
        	.subscribeOn(Schedulers.computation())
        	.filter( c -> c.getTotalKWh() > 20 )
        	.subscribe( c -> System.out.println( "Consumo: "+ c.getIdConsumo() + " con KWh: " + c.getTotalKWh() + "\n Hilo actual: " + Thread.currentThread() ) );
        
        
        
        // Tramitamos los consumos de manera secuencial
//        String resultado;
//        for (Consumo c:consumos) {
//        	resultado = red.getZona(c.getZona()).tramitarConsumo(c);
//            red.getZona(c.getZona()).getVentana().traza (c.getIdConsumo()+ " - Tramitado: "+resultado);
//        }
//        red.imprimeAuditoria();

        // ---- VERSIÓN 3: lanzar los operadores -----
        // Con estos hilos no hace falta hacer join porque se queda bloqueado el programa
        for (int j = 0; j < Config.NUM_ZONAS; j++) {
        	// ---- VERSION 5: Semaforos ----
            TodosOp t = new TodosOp(Config.NUM_OPERADORES_POR_ZONA);
            TodosConsum tcon = new TodosConsum(Config.NUM_MAX_CONSUMOS, red.getZona(j).getVentana());
	        for (int i = 0; i < Config.NUM_OPERADORES_POR_ZONA; i++) {
	        	// operadores de red, traminan los consumos
	        	OperadorRed or = new OperadorRed(red.getZona(j), red.getZona(j).getCentroControl(), t, tcon);
	        	Thread o = new Thread(or);
	        	o.start();
	        }
	        // operadores de carga (1 por zona), recargan la batería
	        OperadorCarga oc = new OperadorCarga(red.getZona(j));
	        Thread toc = new Thread(oc);
	        toc.start();
	        
	        // ---- VERSIÓN 5: Lanzar robots (1 por zona), recargan baterías solar y eólica ----
	        RobotSolar rs = new RobotSolar(red.getZona(j).getBateriaSol());
	        Thread rsol = new Thread(rs);
	        rsol.start();
	        RobotEolico re = new RobotEolico(red.getZona(j).getBateriaEol());
	        Thread reol = new Thread(re);
	        reol.start();
        }

        // DISTINTAS FORMAS DE LANZAR PEDIDOS
        switch(Config.TIPO_LANZADO) {
        case 0: // Forma concurrente
        	
        	// ---- VERSIÓN 1: Implementar concurrencia ----
            // para cada elemento de la lista, se lanza un hilo por consumo que llama a tramitar consumo de su zona
            List<Thread> processList = new ArrayList<Thread>();
            for (Consumo c:consumos) { // para cada consumo, se lanza un hilo
            	ProcesarConsumo pc = new ProcesarConsumo(red, c);
            	Thread t = new Thread(pc);
            	t.start();
            	processList.add(t); // se añade a la lista de hilos
            }
            try {
            	for (int i=0; i<processList.size(); i++) {
            		processList.get(i).join(); // con join se espera para que se impriman las auditorías al final
            	}
            } catch (Exception e) {e.printStackTrace();}  
            
        	break;
        case 1: // Observables
        	
        	// ---- VERSIÓN 7: Lanzar desde observables ----
        	Observable<Consumo> oConsum;
        	oConsum = Consumo.consumosDesdeFicheroObservable(Config.FICHERO_CONSUMOS);
        	
        	oConsum.subscribeOn(Schedulers.computation())
        			.flatMap( i -> Observable.just(i)
        					.map(null)
        					.doOnNext(null));
        	
        	break;
        case 2:
        	ThreadPoolExecutor executor;
        	executor = (ThreadPoolExecutor)Executors.newCachedThreadPool();
        	
        	for (Consumo c:consumos) {
        		ProcesarConsumo pc = new ProcesarConsumo(red, c);
        		executor.execute(pc);
        	}
        	
//        	for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
//        		
//        	}
        	
        	
        	executor.shutdown(); // si no lo invocamos, no acaba
        	break;
        }

        red.imprimeAuditoria();
        
        // ---- VERSIÓN 4: Streams ----
        // Imprimir consumos de menos de 5kwh
        System.out.println("--- CONSUMOS DE MENOS DE 5KW ---");
        consumos.stream()
        	.parallel()
        	.filter(c->c.getTotalKWh() < 5)
        	.forEach(c->System.out.println(c.getIdConsumo()));
        // Imprimir máximo de kwh
        System.out.println("\n--- MÁXIMO DE KWH ---");
        consumos.stream()
        	.parallel()
        	.max(Comparator.comparingDouble(c->c.getTotalKWh()))
        	.ifPresent(c->System.out.println(c.getTotalKWh()));;
        // encontrar direcciones
        System.out.println("\n--- ENCONTRAR SAGITARIO O BERNA ---");
        consumos.stream()
        	.parallel()
        	.filter(c->c.getDireccion().equals("Sagitario, 24"))
        	.findAny()
        	.ifPresent(c->System.out.println("Encontrado"));
        consumos.stream()
        	.parallel()
        	.filter(c->c.getDireccion().equals("Berna, 11"))
        	.findAny()
        	.ifPresent(c->System.out.println("Encontrado: " + c.getDireccion()));
    }
    
}
