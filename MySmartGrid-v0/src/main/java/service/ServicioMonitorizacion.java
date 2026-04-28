package service;

import grpc.MonitorizacionGrpc.MonitorizacionImplBase;
import grpc.MonitorizacionProto.ConsumoReply;
import grpc.MonitorizacionProto.ConsumoRequest;
import io.grpc.stub.StreamObserver;;

public class ServicioMonitorizacion extends MonitorizacionImplBase {

	// Implementación de métodos
	private double totalRed = 0;
	
	// anotar consumo UNARY
	public void anotarConsumo(ConsumoRequest request, StreamObserver<ConsumoReply> responseObserver) {
		System.out.println("Request received from client: " + request);
		
		boolean solar = request.getSolar();
		String idConsum = request.getIdConsumo();
		String dir = request.getDireccion();
		
		int idZona = request.getIdZona();
		double kWh = request.getKWh();
		totalRed += kWh;
		
		ConsumoReply response = ConsumoReply.newBuilder()
				.setIdZona(idZona)
				.setTotalRed(totalRed)
				.setTotalZona(kWh)
				.build();
		
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

	
}
