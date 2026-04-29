package service;

import grpc.PreciosGrpc.PreciosImplBase;
import io.grpc.stub.StreamObserver;
import grpc.PreciosProto.PreciosReply;
import grpc.PreciosProto.PreciosRequest;

import grpc.PreciosProto.DemandaRequest;

public class ServicioPrecio extends PreciosImplBase {
	// Implementación de métodos
	// bidirectional
		public StreamObserver<PreciosRequest> calcularPrecios(StreamObserver<PreciosReply> responseObserver) {
			return new StreamObserver<PreciosRequest>() {
				@Override
				public void onNext(PreciosRequest value) {
					double precio = 0.0;
					for(DemandaRequest d:value.getDemandasList()) {
						if (d.getIdTipo().equals("SOLAR")) {
							precio += d.getKWh() * 0.08;
						}
						else if (d.getIdTipo().equals("EOLICA")) {
							precio += d.getKWh() * 0.10;
						}
						else {
							precio += d.getKWh() * 0.15;
						}
					}
					// montar respuesta
					PreciosReply response = 
							PreciosReply.newBuilder().setPrecio(precio).setIdConsumo(value.getIdConsumo()).build();
					// enviar respuesta
					responseObserver.onNext(response);
				}
				@Override
				public void onError(Throwable t) {
					System.out.print("error:{}"+ t.getMessage());
				}
				@Override
				public void onCompleted() {
					responseObserver.onCompleted();
					
				}
				
			};

		}
}
