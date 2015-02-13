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
import cs455.overlay.routing.RoutingTable;
import cs455.overlay.routing.RoutingEntry;
import cs455.overlay.wireformats.Event;
import cs455.overlay.wireformats.OverlayNodeSendsRegistration;
import cs455.overlay.wireformats.RegistryReportsRegistrationStatus;
import cs455.overlay.wireformats.OverlayNodeSendsDeregistration;
import cs455.overlay.wireformats.RegistrySendsNodeManifest;
import cs455.overlay.util.InteractiveCommandParser;

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
	InetAddress address;

	//Data on how to reach the registry
	InetAddress registryAddress;
	int registryPort;
	int portToRegistry;

	//The id assigned to this node by the registry in RegistryReportsRegistrationStatus
	int id;

	//The routing table for this node
	RoutingTable routingTable = new RoutingTable();

	public MessageNode(){
		cache = new NodeConnectionCache();

		try{
			address = InetAddress.getLocalHost();
		}catch(UnknownHostException e){
			System.out.println("MessageNode: Error finding address");
			System.out.println(e);
		}

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

		int messageType = e.getType();

		switch(messageType){
			case 3:
					this.onMessageThree(e);
					break;
			case 5:
					this.onMessageFive();
			case 6:
					this.onMessageSix(e);
			default: break;
		}

	}//End onEvent

	public void onMessageThree(Event e){
		RegistryReportsRegistrationStatus message3 = null;
		try{
			message3 = new RegistryReportsRegistrationStatus(e.getBytes());
		}catch(UnknownHostException exception){
			System.out.println("MessageNode: Error reading RegistryReportsRegistrationStatus");
			System.out.println(exception);
		}


		int status = message3.getStatus();
		String information = message3.getInformation();

		if(status == -1){
			System.out.println("MessageNode: Error in registration!\n" + information);
		}else{
			this.id = status;
			System.out.println("Registration successful. ID: " + id);
		}
	}//End onMessageThree

	public void onMessageFive(){
		System.out.println("Need to implement functionality to handle REGISTRY_REPORTS_DEREGISTRATION_STATUS messages");
	}//end onMessageFive

	public void onMessageSix(Event event){
		RegistrySendsNodeManifest rsnm = new RegistrySendsNodeManifest(event.getBytes());

		int[] id = rsnm.getIds();
		InetAddress[] address = rsnm.getAddress();
		int[] port = rsnm.getPorts();

		//Build routing Entries
		for(int i = 0; i < id.length; i++){
			Connection overlayConnection = null;
			//Open socket to node in overlay in order to create a Connection
			Socket overlaySocket = new Socket();
			try{
				//Create a socket connection with the messageNode
				overlaySocket = new Socket(address[i], port[i]);
				overlayConnection = new Connection(this, overlaySocket);
			}catch(IOException e){
				System.out.println("Error opening socket to node in overlay");
				System.out.println("  Node ID: " + id[i]);
				System.out.println(e);
			}

			RoutingEntry entry = new RoutingEntry(address[i], id[i], port[i], overlayConnection);
			routingTable.addEntry(entry);
		}

		System.out.println("Routing Table: ");
		System.out.println(routingTable);
	}//End onMessageSix

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
		OverlayNodeSendsRegistration registration = new OverlayNodeSendsRegistration(local, portToRegistry);
		String RegistryKey = registryAddress.getHostAddress().concat(String.valueOf(registryPort));
		System.out.println("MessageNode: Trying to find connection with key: " + RegistryKey);
		Connection registryConnection = cache.get(RegistryKey);
		System.out.println(registration);
		System.out.println(registration.getBytes().length);
		registryConnection.getSender().write(registration.getBytes());
	}//End sendRegistration

	//Sets up instance variables, and registers with the registry
	public void init(String hostName, String port){
		//The address and port of the registry are pulled from the command line
		try{
			this.registryAddress = InetAddress.getByName(hostName);
		}catch(UnknownHostException e){
			System.out.println("MessageNode: Error retrieving registry address");
			System.out.println(e);
		}
		this.registryPort = Integer.parseInt(port);

		//Open connection to registry, and place it in the cache
		Connection registryConnection = this.connectToRegistry(hostName, this.registryPort);
		String key = this.registryAddress.getHostAddress().concat(String.valueOf(this.registryPort));
		System.out.println("MessageNode: Adding connection with key: " + key);
		this.cache.add(key, registryConnection);
		// node.cache.get(key);

		//Register...It's the law
		try{
			this.sendRegistration();
		}catch(UnknownHostException e){
			System.out.println("MessageNode: Error sending registration");
			System.out.println(e);
		}
	}

	//These methods are called from InteractiveCommandParser
	public void exitOverlay(){
		//Create message
		OverlayNodeSendsDeregistration deregistration;
		deregistration = new OverlayNodeSendsDeregistration(this.address, this.portToRegistry, this.id);

		//Send message
		String RegistryKey = registryAddress.getHostAddress().concat(String.valueOf(registryPort));
		Connection registryConnection = cache.get(RegistryKey);
		registryConnection.getSender().write(deregistration.getBytes());
	}

	//MAIN
	//Currently only for testing
	public static void main(String args[]){
		//Make a messaging node. It will start our server for us
		MessageNode node = new MessageNode();

		node.init(args[0], args[1]);

		InteractiveCommandParser parser = new InteractiveCommandParser(node);
	}//End main

}
