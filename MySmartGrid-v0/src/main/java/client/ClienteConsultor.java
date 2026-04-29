package client;

import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import grpc.MonitorizacionProto.DemandaReply;
import grpc.MonitorizacionProto.DemandaRequest;
import grpc.MonitorizacionProto.DireccionReply;
import grpc.MonitorizacionProto.DireccionRequest;

import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import grpc.MonitorizacionGrpc;
import grpc.MonitorizacionGrpc.MonitorizacionBlockingStub;
import grpc.MonitorizacionGrpc.MonitorizacionStub;

import pcd.util.Ventana;

public class ClienteConsultor  {
	static ManagedChannel channel;
	static MonitorizacionBlockingStub monStubBlocking;
	static MonitorizacionStub monStubNonBlocking;
	static Ventana v = new Ventana(20, 20, 300, 300, "CLIENTE");
	
	public static void main(String args[]) throws InterruptedException {
		String target="localhost:9002";
		channel = Grpc
			.newChannelBuilder(target, InsecureChannelCredentials.create())
			.build();
		monStubBlocking = MonitorizacionGrpc.newBlockingStub(channel);
		monStubNonBlocking = MonitorizacionGrpc.newStub(channel);
		
		executeDemandaSolar();
		executeConsumosDireccion();
	}
	
	public static void executeDemandaSolar() throws InterruptedException {
		v.traza("#######START DEMANDA SOLAR#######\n");
		
		DemandaRequest demandaRequest =
				DemandaRequest.newBuilder().setIdZona(2).build();
		
		// v.traza ("REQUEST;"+ demandaRequest.getIdZona() +"\n");
		
		try {
			Iterator<DemandaReply>
			demandaReply=monStubBlocking.demandaSolar(demandaRequest);
			//Leer las respuestas recibidas en demandaReply por el servidor
			while (demandaReply.hasNext())
					v.traza("RESPONSE:"+ demandaReply.next().getIdConsumo()+"\n");
		} catch (StatusRuntimeException e) {e.printStackTrace();}
	}
	
	public static void executeConsumosDireccion() throws InterruptedException {
		v.traza("#######START CONSUMO DIRECCION#######: ClientStreaming \n");
		
		//Declarar tiempo de espera
		final CountDownLatch finishLatch = new CountDownLatch(1);
		
		//1. Declaramos mi comportamiento como observador.
		StreamObserver<DireccionReply> streamResponse = new StreamObserver<DireccionReply>() {
			public void onNext(DireccionReply value) {
					// Leer y mostrar la respuesta del servidor
					int respuesta= value.getTotal();
					v.traza("Respuesta consumos Direccion: " + respuesta);
			}
			public void onError(Throwable t) {
				System.out.print("Finished error \n");
				finishLatch.countDown();
			}
			@Override
			public void onCompleted() {
				System.out.println("finish call");
				finishLatch.countDown();
			}
		};
		
		//Definimos mi comportamiento como cliente
		StreamObserver<DireccionRequest> requestObserver =
				monStubNonBlocking.consumosDireccion(streamResponse);
		try {
			for (int i=0;i<5;i++) {
				DireccionRequest dirRequest = DireccionRequest.newBuilder()
						.setDireccion("Berna, 11").build();
				requestObserver.onNext(dirRequest);
			// v.traza("DIRECCION:" + i + " desde Cliente\n");
			}
			requestObserver.onCompleted();
		} catch (RuntimeException e) {
			requestObserver.onError(e); throw e;
		}
		
		//Esperar un minuto a la respuesta
		if (!finishLatch.await(1, TimeUnit.MINUTES)) {
			System.out.print("Operation didn’t finish within 1 minute \n");
		}
	}
	
	
}
