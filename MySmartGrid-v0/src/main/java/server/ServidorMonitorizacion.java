package server;

import java.io.IOException;
import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import service.ServicioMonitorizacion;

public class ServidorMonitorizacion {
	public static void main(String args[]) throws IOException, InterruptedException {
		System.out.println("Starting Monitorizacion GRPc Server...");
		
		// ¡¡¡ Recordar cambiar el servicio al crear un server !!!
		Server server = Grpc
				.newServerBuilderForPort(9002, InsecureServerCredentials.create())
				.addService(new ServicioMonitorizacion())
				.build();
		server.start();
		System.out.println("Monitorizacion server started at: " + server.getPort());
		server.awaitTermination();
	}
}
