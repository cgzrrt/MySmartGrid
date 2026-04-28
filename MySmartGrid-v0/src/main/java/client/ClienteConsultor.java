package client;

import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;

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
	}
	
	
}
