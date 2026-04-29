package server;

import java.io.IOException;

import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import pcd.util.Ventana;
import service.ServicioPrecio;

public class ServidorPrecios {
	public static void main(String args[]) throws IOException, InterruptedException {
		Ventana v = new Ventana (100, 30, 360, 360, "Servidor");
		v.traza("Starting Monitorizacion GRPc Server...");
		
		
		// ¡¡¡ Recordar cambiar el servicio al crear un server !!!
		Server server = Grpc
				.newServerBuilderForPort(9004, InsecureServerCredentials.create())
				.addService(new ServicioPrecio())
				.build();
		server.start();
		v.traza("Monitorizacion server started at: " + server.getPort());
		server.awaitTermination();
	}
}
