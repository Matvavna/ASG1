package cs455.overlay.node;
/*
 *Author: Tiger Barras
 *Registry.java
 *Top level class that creates the overlay that all the MessagingNodes reside in
 */

import java.net.Socket;
import java.io.IOException;
import cs455.overlay.transport.ConnectionCache;
import cs455.overlay.transport.RegisterConnectionCache;
import cs455.overlay.transport.ServerThread;
import cs455.overlay.wireformats.Event;

public class Registry implements Node{

	int portNum;//Port that the registry listens on
	RegisterConnectionCache cache;

	public Registry(int pn){
		portNum = pn;
		cache = new RegisterConnectionCache();

		try{
			this.startServer(portNum);
		}catch(IOException e){
			System.out.println("Registry: Could not start ServerThread");
			System.out.println(e);
		}
	}//End constructor

	public void onEvent(Event e){

	}//End onEvent

	public void startServer(int portNum) throws IOException{
		ServerThread server = new ServerThread(portNum, this);
		server.start();
	}//End startServer

	public void spawnRecieverThread(Socket socket){
		System.out.println("I didn't implement Registry.spawnRecieverThread b/c I don't think it's neaded anymore");
	}//End spawnRecieverThread

	public ConnectionCache getConnectionCache(){
		return new RegisterConnectionCache();
	}//End getConnection



	public static void main(String args[]){
		//The arguement for this is the portnumber to run on
		Registry registry = new Registry(Integer)
	}//End main

}//End class
