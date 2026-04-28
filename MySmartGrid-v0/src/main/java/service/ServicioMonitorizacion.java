package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import energy.ConsumoAnotado;

import grpc.MonitorizacionGrpc.MonitorizacionImplBase;
import grpc.MonitorizacionProto.ConsumoReply;
import grpc.MonitorizacionProto.ConsumoRequest;
import grpc.MonitorizacionProto.DemandaReply;
import grpc.MonitorizacionProto.DemandaRequest;
import io.grpc.stub.StreamObserver;;

public class ServicioMonitorizacion extends MonitorizacionImplBase {

	// Implementación de métodos
	private double totalRed = 0;
	private HashMap<Integer, Double> totalZona = new HashMap<Integer, Double>();
	private List<ConsumoAnotado> consumosAnotados = new ArrayList<>();
	
	// anotar consumo UNARY
	public void anotarConsumo(ConsumoRequest request, StreamObserver<ConsumoReply> responseObserver) {
		System.out.println("Request received from client: " + request);

		// sumamos al total de la red
		totalRed += request.getKWh();
		
		if (totalZona.containsKey(request.getIdZona())) {
			// insertamos en el hash map
			totalZona.put(request.getIdZona(), totalZona.get(request.getIdZona())+request.getKWh());
		}
		else {
			totalZona.put(request.getIdZona(), request.getKWh());
		}
		
		consumosAnotados.add(new ConsumoAnotado(request.getIdConsumo(), request.getIdZona(), request.getKWh(), request.getDireccion(), request.getSolar()));
		
		ConsumoReply response = ConsumoReply.newBuilder()
				.setIdZona(request.getIdZona())
				.setTotalRed(totalRed)
				.setTotalZona(totalZona.get(request.getIdZona()))
				.build();
		
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

	public void demandaSolar(DemandaRequest request, StreamObserver<DemandaReply> responseObserver) {
		// recorremos la lista
		for (ConsumoAnotado c:consumosAnotados) {
			if (request.getIdZona() == c.getIdZona() && c.getSolar()) {
				// construimos las respuestas
				DemandaReply response = DemandaReply.newBuilder().setIdConsumo(c.getIdConsumo()).build();
				responseObserver.onNext(response);
			}
		}
		responseObserver.onCompleted();
	}
}
