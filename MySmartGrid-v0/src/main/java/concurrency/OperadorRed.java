package concurrency;

import operators.CentroControl;
import energy.ZonaEnergetica;
import main.Config;
import grpc.MonitorizacionGrpc;
import grpc.MonitorizacionGrpc.MonitorizacionBlockingStub;
import grpc.MonitorizacionProto.ConsumoReply;
import grpc.MonitorizacionProto.ConsumoRequest;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;

import java.util.Random;

public class OperadorRed implements Runnable {
	private CentroControl control;
	private ZonaEnergetica zona;
	private TodosOp todos;
	private TodosConsum todCon;
	private TodosCB todosCB;
	
	private EsperaOpCDL cdl; // VERSIÓN 8
	
	private ManagedChannel channel;
	private MonitorizacionBlockingStub blockingStub;
	
	public OperadorRed (ZonaEnergetica _zona, CentroControl _control, TodosOp _todos, TodosConsum _todCon, TodosCB _todosCB) {
		zona = _zona;
		control = _control;
		todos = _todos;
		todCon = _todCon;
		todosCB = _todosCB;
		
		
		// ---- VERSIÓN 9: Unary ----
		String target="localhost:9002";
		channel = Grpc
			.newChannelBuilder(target, InsecureChannelCredentials.create())
			.build();
		
		blockingStub = MonitorizacionGrpc.newBlockingStub(channel);	
	}
	
	@Override
	public void run() {
		
		// cdl.operadorEjecutando(); // VERSIÓN 8
		
		switch (Config.TIPO_TODOS) {
		case 0:
			todos.operadoresLanzados();
			break;
		case 1:
			todosCB.operadoresLanzados();
			break;
		}
		
		while (true) {
			// System.out.println("--- OPERADOR DE ZONA " + zona.getIdZona());
			
			
			
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
	     		
	     		// ---- VERSIÓN 9: Unary ----
	     		System.out.println ("####### EJECUTAR ANOTAR CONSUMO #######\n");
				ConsumoRequest consumoRequest =
					ConsumoRequest.newBuilder().setDireccion(tc.getConsumo().getDireccion())
						.setIdConsumo(tc.getConsumo().getIdConsumo())
						.setIdZona(tc.getConsumo().getZona())
						.setKWh(tc.getConsumo().getTotalKWh())
						.setSolar(d.equals("SOLAR"))
						.build();
				ConsumoReply consumoReply = blockingStub.anotarConsumo(consumoRequest);
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
	
	
	public void anotarConsumoCliente() {
		
//		HelloRequest helloRequest =
//				HelloRequest.newBuilder().setName("Celia").build();
//		System.out.println ("Sending to server: "+helloRequest.getName()+"\n");
//		//Send request to Server
//		HelloReply helloReply = greeterStubBlocking.sayHello(helloRequest);
//		System.out.println(helloReply.getMessage());
	}
	
	
	
}
