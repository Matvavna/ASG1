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
import cs455.overlay.transport.Connection;
import cs455.overlay.transport.ConnectionCache;
import cs455.overlay.transport.RegisterConnectionCache;
import cs455.overlay.transport.ServerThread;
import cs455.overlay.wireformats.Event;
import cs455.overlay.wireformats.OverlayNodeSendsRegistration;
import cs455.overlay.wireformats.RegistryReportsRegistrationStatus;
import cs455.overlay.routing.RoutingTable;
import cs455.overlay.routing.RoutingEntry;

public class Registry implements Node{

	int portNum;//Port that the registry listens on
	RegisterConnectionCache cache;//Short term storage to hold connections before they are error checked
	RoutingTable routingTable;//Long term storage for legit connections
	ArrayList<Integer> registry = new ArrayList<Integer>();//Why did I make this?

	public Registry(int pn){
		portNum = pn;
		cache = new RegisterConnectionCache();
		routingTable = new RoutingTable();

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
		System.out.println("Registry.onEvent()");
		System.out.println(e);
		System.out.println(e.getBytes().length);

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
			onsr = new OverlayNodeSendsRegistration(sendsReg.getBytes(), sendsReg.getSocket());
		}catch(UnknownHostException e){
			System.out.println("Registry: Error converting event into OverlayNodeSendsRegistration");
			System.out.println(e);
		}

		//Generate response message
		RegistryReportsRegistrationStatus response;
		response = checkRegistration(onsr);

		//Send response
		InetAddress address = onsr.getIP();
		int port = onsr.getPort();
		String key = address.getHostAddress().concat(String.valueOf(port));
		RoutingEntry responseEntry = routingTable.getEntry(key);
		Connection responseConnection = responseEntry.getConnection();
		responseConnection.getSender().write(response.getBytes());

	}//End onMessageTwo

	//Generates a new, unique identifier between 0 & 127.
	private int generateId(){
		if(registry.isEmpty()) return 0;//This means that this is the first node in the registry

		//Otherwise, just return one more than the last one in the registry
		return registry.get(registry.size()-1);
	}//End generateID

	//Does error checking on registration messages
	//Returns message with appropriate info to be sent back to messageNode
	private RegistryReportsRegistrationStatus checkRegistration(OverlayNodeSendsRegistration onsr){
		Socket socket = onsr.getSocket();
		InetAddress socketAddress = socket.getInetAddress();
		int socketPort = socket.getPort();

		//Pull relevant data from the message for error checking
		InetAddress messageAddress = onsr.getIP();
		int messagePort = onsr.getPort();
		//Build key to search cache
		String addressKey = messageAddress.getHostAddress();
		addressKey = addressKey.concat(String.valueOf(messagePort)); //At some point, make this it's own method so that all the keys are generated the exact same way
		int successStatus = -1;
		//Set up info string so it's ready to go if registration is successful
		String information = "Registration request successfull";
		information = information.concat("The number of messaging nodes currently in the overlay is ");
		information = information.concat(String.valueOf(cache.size()));

		//Check to make sure that the information in the cache matches
			//what is in the message
		if((socketAddress != messageAddress)  ||  (socketPort!=messagePort)){
				//This means that the address or the port in the message is wrong
				information = "Registration failed: Information in message did not match actual";
				successStatus = -1;
		}else{
			//The information was correct!
			successStatus = generateId();
		}
		//Check to see if this node is already in the Routing Table
		//At this point, the successStatus is either -1 cause the info was messed up,
			//Or it's !-1 because the information was correct
		if(routingTable.contains(addressKey)){
			//This node has already registered!
			information = "Registration failed: Node has already registered";
			successStatus = -1;
		}

		//If it's all good, add this node to the table
		if(successStatus != -1){
			Connection connection = cache.get(addressKey);
			RoutingEntry entry = new RoutingEntry(messageAddress, successStatus, messagePort, connection);
			routingTable.addEntry(entry);
		}

		RegistryReportsRegistrationStatus statusMessage;
		statusMessage = new RegistryReportsRegistrationStatus(successStatus, information);
		return statusMessage;

	}//End checkRegistration

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
