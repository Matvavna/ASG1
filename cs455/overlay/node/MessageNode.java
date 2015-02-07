package cs455.overlay.node;
/*
 *Author: Tiger Barras
 *MessageNode.java
 *Top level class that generates, routes, and recieves packages from other Nodes
 */

import cs455.overlay.node.Node;
import cs455.overlay.transport.ConnectionCache;
import cs455.overlay.transport.NodeConnectionCache;
import cs455.overlay.transport.Connection;
import cs455.overlay.transport.ServerThread;
import cs455.overlay.transport.RecieverThread;
import cs455.overlay.transport.Sender;
import cs455.overlay.wireformats.Event;
import cs455.overlay.wireformats.OverlayNodeSendsRegistration;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.IOException;
import java.util.Scanner;
import java.lang.Integer;

//Remember that connections between nodes only need to be one way
//Other than the registry
public class MessageNode implements Node{

	//Need a cache for the sockets I'm going to send messages to
	//I'll start receiverThreads to deal with the ones talking to me
	NodeConnectionCache cache;
	//The port that this node's serverThread is listening on
	//Set in the startServer call
	int portNum;

	//Data on how to reach the registry
	InetAddress registryAddress;
	int registryPort;
	int portToRegistry;

	public MessageNode(){
		cache = new NodeConnectionCache();
		//Get the server set up
		try{
			this.startServer(0);//Opening a serverSocket on port 0 automatically finds an open port
		}catch(IOException e){
			System.out.println("MessageNode: Could not start ServerThread");
			System.out.println(e);
		}
	}//End constructor

	//This is what will get called when something happens
	//Such as a message coming in, or a new link being opened
	public void onEvent(Event e){

		System.out.println(e);

	}//End onEvent

	//Listens at a specific port, and then passes out a Socket
	public void startServer(int pn) throws IOException{
		ServerThread server = new ServerThread(pn, this);
		server.getPortNum();
		server.start();
	}//End startServer

	//Spans a Reciever thread that is linked to the specified socket
	public void spawnRecieverThread(Socket socket){
		RecieverThread reciever = new RecieverThread(this, socket);
		reciever.start();
	}//End spawnRecieverThread

	public ConnectionCache getConnectionCache(){
		return cache;
	}//End getConnectionCache

	public void setPortNum(int pn){
		this.portNum = pn;
		//System.out.println("MessageNode pn: " + this.portNum);
	}//End setPortNum

	public Connection connectToRegistry(String addressString, int registryPort){
		//This is just a dummy address so that the variable is initialized
		InetAddress registryHost = InetAddress.getLoopbackAddress();
		try{
			//Generate the actual InetAddress of the Registry
			registryHost = InetAddress.getByName(addressString);
		}catch(UnknownHostException e){
			System.out.println("Unknown Host exception");
			System.out.println(e);
			System.exit(-1);
		}

		//Default socket to initialize
		Socket registrySocket = new Socket();
		try{
			//Create a socket connection with the Registry
			registrySocket = new Socket(registryHost, registryPort);
			portToRegistry = registrySocket.getLocalPort();
			System.out.println("MessageNode: Using port " + portToRegistry + " to connect to registry");
		}catch(IOException e){
			System.out.println("Error opening socket to Registry");
			System.out.println(e);
		}

		//Create a Connection object witht that socket
		Connection registryConnection = new Connection(this, registrySocket);
		return registryConnection;
	}//End connectToRegistry

	public void sendRegistration() throws UnknownHostException{
		InetAddress local = InetAddress.getLocalHost();
		OverlayNodeSendsRegistration registration = new OverlayNodeSendsRegistration(local, portNum);
		String RegistryKey = registryAddress.getHostAddress().concat(String.valueOf(portToRegistry));
		System.out.println("MessageNode: Trying to find connection with key: " + RegistryKey);
		Connection registryConnection = cache.get(RegistryKey);
		System.out.println(registration);
		System.out.println(registration.getBytes().length);
		registryConnection.getSender().write(registration.getBytes());
	}//End sendRegistration

	//MAIN
	//Currently only for testing
	public static void main(String args[]){
		//Make a messaging node. It will start our server for us
		MessageNode node = new MessageNode();

		//@TODO  Add sanity checking on command line input

		//The address and port of the registry are pulled from the command line
		try{
			node.registryAddress = InetAddress.getByName(args[0]);
		}catch(UnknownHostException e){
			System.out.println("MessageNode: Error retrieving registry address");
			System.out.println(e);
		}
		node.registryPort = Integer.parseInt(args[1]);

		//Open connection to registry, and place it in the cache
		Connection registryConnection = node.connectToRegistry(args[0], node.registryPort);
		String key = node.registryAddress.getHostAddress().concat(String.valueOf(node.registryPort));
		System.out.println("MessageNode: Adding connection with key: " + key);
		node.cache.add(key, registryConnection);
		// node.cache.get(key);

		//Register...It's the law
		try{
			node.sendRegistration();
		}catch(UnknownHostException e){
			System.out.println("MessageNode: Error sending registration");
			System.out.println(e);
		}
	}//End main

}
