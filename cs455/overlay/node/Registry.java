package cs455.overlay.node;
/*
 *Author: Tiger Barras
 *Registry.java
 *Top level class that creates the overlay that all the MessagingNodes reside in
 */

import java.io.IOException;
import java.util.ArrayList;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import cs455.overlay.transport.ConnectionCache;
import cs455.overlay.transport.RegisterConnectionCache;
import cs455.overlay.transport.ServerThread;
import cs455.overlay.wireformats.Event;
import cs455.overlay.wireformats.OverlayNodeSendsRegistration;

public class Registry implements Node{

	int portNum;//Port that the registry listens on
	RegisterConnectionCache cache;
	ArrayList<Integer> registry = new ArrayList<Integer>();

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

	//Called by the reciever thread whenever it recieves a message
	//This calls the appropriate method for the message type
	public void onEvent(Event e){
		int type = e.getType();
		switch(type){
			 case 2: this.onMessageTwo(e);
							 break;
			default: break;
		}
		System.out.println(e);
	}//End onEvent

	//----Methods called from onEvent----
	public void onMessageTwo(Event sendsReg){
		//Turn the Event into the right message type
		OverlayNodeSendsRegistration onsr = null;
		try{
			onsr = new OverlayNodeSendsRegistration(sendsReg.getBytes());
		}catch(UnknownHostException e){
			System.out.println("Registry: Error converting event into OverlayNodeSendsRegistration");
			System.out.println(e);
		}

		InetAddress IP = onsr.getIP();
		int port = onsr.getPort();
		boolean successFlag = false;
		int successStatus = -1;
		String information = "Registration request successfull";
		information = information.concat("The number of messaging nodes currently in the overlay is ");
		information = information.concat(String.valueOf(cache.size()));

		//Check to make sure that the information in the cache matches
		  //what is in the message
		//Check to see if this node is already in the Routing Table


	}//End onMessageTwo

	//Generates a new, unique identifier between 0 & 127.
	private int generateID(){
		if(registry.isEmpty()) return 0;//This means that this is the first node in the registry

		//Otherwise, just return one more than the last one in the registry
		return registry.get(registry.size()-1);
	}//End generateID

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
