package cs455.overlay.transport;
//Author: Tiger Barras
//ServerThread.java
//Waits for incoming connections, then opens a socket with them

import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;

public class ServerThread extends Thread{

	int portNum; //Port that the ServerThread object will listen on
	long id;
	String name;

	public ServerThread(int pn){
		portNum = pn;
		id = this.getId();
		name = this.getName();
	}//End constructor

	//This is where execution begins when this thread is created
	public void run(){
		try{
			ServerSocket serverSocket = new ServerSocket(portNum);
			messageWithId("Ready to connect. . .");
			Socket socket = serverSocket.accept();
			messageWithId("Socket Generated");
			//Open up new Connection
		}catch(IOException e){
			messageWithId("Error opening Server Socket");
			messageWithId("Error: " + e);
			System.exit(-1);
		}//End try/catch
	}//End run

	private void messageWithId(String message){
		System.out.printf("Thread (%s:%d): %s\n", name, id, message);
	}//End messageWithId

}//End class
