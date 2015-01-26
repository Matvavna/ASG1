package cs455.overlay.transport;
//Author: Tiger Barras
//ServerThread.java
//Waits for incoming connections, then opens a socket with them

import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;

public class ServerThread extends Thread{

	int portNum; //Port that the ServerThread object will listen on
	ConnectionCache cache;
	long id;
	String name;

	public ServerThread(int pn, ConnectionCache c){ //Port number to listen to, cache to add sockets to
		portNum = pn;
		cache = c;
		id = this.getId();
		name = this.getName();
	}//End constructor

	//This is where execution begins when this thread is created
	public void run(){
		Socket socket;
		try{ //Listen at port portNum, and open socket to an incoming connection
			ServerSocket serverSocket = new ServerSocket(portNum);
			messageWithId("Ready to connect. . .");
			socket = serverSocket.accept();
			messageWithId("Socket Generated");
			cache.add(1,socket);
			messageWithId("Socket added to cache");	
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
