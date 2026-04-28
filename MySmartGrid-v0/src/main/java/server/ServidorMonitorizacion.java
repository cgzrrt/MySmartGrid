package server;

import java.io.IOException;
import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import pcd.util.Ventana;
import service.ServicioMonitorizacion;

public class ServidorMonitorizacion {
	
	public static void main(String args[]) throws IOException, InterruptedException {
		Ventana v = new Ventana (30, 30, 360, 360, "Servidor");
		v.traza("Starting Monitorizacion GRPc Server...");
		
		
		// ¡¡¡ Recordar cambiar el servicio al crear un server !!!
		Server server = Grpc
				.newServerBuilderForPort(9002, InsecureServerCredentials.create())
				.addService(new ServicioMonitorizacion())
				.build();
		server.start();
		v.traza("Monitorizacion server started at: " + server.getPort());
		server.awaitTermination();
	}
}
