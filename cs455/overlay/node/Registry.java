package cs455.overlay.node;
/*
 *Author: Tiger Barras
 *Registry.java
 *Top level class that creates the overlay that all the MessagingNodes reside in
 */

import java.net.Socket;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
		System.out.println(e);
	}//End onEvent

	public void startServer(int portNum) throws IOException{
		ServerThread server = new ServerThread(portNum, this);
		server.start();
	}//End startServer

	public void spawnRecieverThread(Socket socket){
		System.out.println("I didn't implement Registry.spawnRecieverThread b/c I don't think it's neaded anymore");
	}//End spawnRecieverThread

	public ConnectionCache getConnectionCache(){
		return this.cache;
	}//End getConnection

	public void setPortNum(int pn){
		System.out.println("Registry pn: " + pn);
		portNum = pn;
	}//End setPortNum



	public static void main(String args[]){
		//The arguement for this is the portnumber to run on
		Registry registry = new Registry(Integer.parseInt(args[0]));//args[0] being the portnum
		/*try{
			registry.startServer(Integer.parseInt(args[0]));
		}catch(IOException e){
			System.out.println("Registry could not start server");
			System.out.println(e);
		}*/
		InetAddress addr = InetAddress.getLoopbackAddress();
		try{
			addr = InetAddress.getLocalHost();
			//addrByte = addr.getAddress();
		}catch(UnknownHostException e){
			System.out.println("Error finding address");
		}
	}//End main



}//End class
