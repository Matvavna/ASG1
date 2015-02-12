package cs455.overlay.node;
/*
 *Author: Tiger Barras
 *Registry.java
 *Top level class that creates the overlay that all the MessagingNodes reside in
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Collection;
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
import cs455.overlay.wireformats.OverlayNodeSendsDeregistration;
import cs455.overlay.wireformats.RegistryReportsDeregistrationStatus;
import cs455.overlay.wireformats.RegistrySendsNodeManifest;
import cs455.overlay.routing.RoutingTable;
import cs455.overlay.routing.RoutingEntry;
import cs455.overlay.util.InteractiveCommandParser;

public class Registry implements Node{

	int portNum;//Port that the registry listens on
	RegisterConnectionCache cache;//Short term storage to hold connections before they are error checked
	RoutingTable routingTable;//Long term storage for legit connections
	//ArrayList<Integer> registry = new ArrayList<Integer>();//Why did I make this?

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
		//System.out.println(e.getBytes().length);

		int type = e.getType();
		switch(type){
			 case 2: this.onMessageTwo(e);
							 break;
			 case 4: this.onMessageFour(e);
			default: break;
		}
		//System.out.println(e);
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
		System.out.println(response);

		//Send response
		InetAddress address = onsr.getIP();
		int port = onsr.getPort();
		String key = address.getHostAddress().concat(String.valueOf(port));
		RoutingEntry responseEntry = routingTable.getEntry(key);
		Connection responseConnection = responseEntry.getConnection();
		//System.out.println(response.getBytes().length);
		responseConnection.getSender().write(response.getBytes());

	}//End onMessageTwo

	public void onMessageFour(Event e){
		OverlayNodeSendsDeregistration onsd = new OverlayNodeSendsDeregistration(e.getBytes(), e.getSocket());

		//Build the key from the actual address/socket that sent the deregistration
		Socket socket = onsd.getSocket();
		InetAddress socketAddress = socket.getInetAddress();
		int socketPort = socket.getPort();
		String socketKey = socketAddress.getHostAddress();
		socketKey = socketKey.concat(String.valueOf(socketPort));

		//Pull relevant data from the message for error checking
		InetAddress messageAddress = onsd.getIP();
		int messagePort = onsd.getPort();
		//Build key to search cache
		String addressKey = messageAddress.getHostAddress();
		addressKey = addressKey.concat(String.valueOf(messagePort)); //At some point, make this it's own method so that all the keys are generated the exact same way

		//Set up reply information
		int successStatus = -1;
		String information = "Deregistration request successfull";

		//Check to make sure that the information in the cache matches
			//what is in the message
		if(!socketKey.equals(addressKey)){
			System.out.printf("socket:%s%d\nmessage:%s%d\n",socketAddress.getHostAddress(),socketPort,messageAddress.getHostAddress(),messagePort);
			//This means that the address or the port in the message is wrong
			information = "Deregistration failed: Information in message did not match actual";
			successStatus = -1;
		}else{
			//The information was correct!
			successStatus = 1;
		}
		//Check to see if this node is already in the Routing Table
		//At this point, the successStatus is either -1 cause the info was messed up,
			//Or it's !-1 because the information was correct
		if(!routingTable.contains(onsd.getId())){
			//This node has already registered!
			System.out.println(addressKey);
			System.out.println(onsd.getId());
			System.out.println(routingTable.contains(addressKey));
			System.out.println(routingTable.contains(onsd.getId()));
			information = "Registration failed: Node has already deregistered";
			successStatus = -1;
		}

		//If it's all good, add this node to the table
		if(successStatus == 1){
			System.out.println("Removing routing entry from table");
			routingTable.removeEntry(onsd.getPort(), addressKey);
		}

		RegistryReportsDeregistrationStatus statusMessage;
		statusMessage = new RegistryReportsDeregistrationStatus(successStatus, information);

		//Send response
		int idKey = onsd.getId();
		RoutingEntry responseEntry = routingTable.getEntry(idKey);
		Connection responseConnection = responseEntry.getConnection();
		//System.out.println(response.getBytes().length);
		responseConnection.getSender().write(statusMessage.getBytes());

	}//End onMessageFour

	//Generates a new, unique identifier between 0 & 127.
	private synchronized int generateId(){
		//We'll just assign them sequentially
		return routingTable.getSize();
	}//End generateID

	//Does error checking on registration messages
	//Returns message with appropriate info to be sent back to messageNode
	private RegistryReportsRegistrationStatus checkRegistration(OverlayNodeSendsRegistration onsr){
		Socket socket = onsr.getSocket();
		InetAddress socketAddress = socket.getInetAddress();
		int socketPort = socket.getPort();
		String socketKey = socketAddress.getHostAddress();
		socketKey = socketKey.concat(String.valueOf(socketPort));

		//Pull relevant data from the message for error checking
		InetAddress messageAddress = onsr.getIP();
		int messagePort = onsr.getPort();
		//Build key to search cache
		String addressKey = messageAddress.getHostAddress();
		addressKey = addressKey.concat(String.valueOf(messagePort)); //At some point, make this it's own method so that all the keys are generated the exact same way
		int successStatus = -1;
		//Set up info string so it's ready to go if registration is successful
		String information = "Registration request successfull ";
		information = information.concat("The number of messaging nodes currently in the overlay is ");
		information = information.concat(String.valueOf(cache.size()));

		//Check to make sure that the information in the cache matches
			//what is in the message
		if(!socketKey.equals(addressKey)){
			System.out.printf("socket:%s%d\nmessage:%s%d\n",socketAddress.getHostAddress(),socketPort,messageAddress.getHostAddress(),messagePort);
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
			System.out.println("Adding routing entry to table");
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



	//These are methods called by InteractiveCommandParser
	public void listMessagingNodes(){
		Enumeration<RoutingEntry> tableElements = routingTable.getAllEntriesEnumeration();

		while(tableElements.hasMoreElements()){
			System.out.println(tableElements.nextElement());
		}
	}//End listMessagingNodes

	public void setupOverlay(int numberOfTableEntries){
		//Turn routing table into array that's easier to navigate
		RoutingEntry[] routingArray = routingTable.getAllEntriesCollection().toArray(new RoutingEntry[0]);
		//Sort array, so it can be traversed in order of the entry IDs
		Arrays.sort(routingArray);

		//Array of all the node IDs, to be sent to messagingNodes
		int[] allIds = new int[routingArray.length];
		for(int i = 0; i < routingArray.length; i++){
			allIds[i] = routingArray[i].getId();
		}

		//Go through each element in the routing table.
		for(int i = 0; i < routingArray.length; i++){
			// System.out.println(routingArray[i].getId());

			//Build list of nodes that are in this nodes routingTable
			RoutingEntry[] entryManifest;//The list of entries used to build this nodes routingTable
			entryManifest = new RoutingEntry[numberOfTableEntries];

			//Build entryManifest using routingArray's entries
			int routingEntryIndexBase = i;
			for(int n = 0; n < numberOfTableEntries; n++){
				int offset = 2^n;//1,2,4,8,16...
				System.out.println("n: " + n + " Offset: " + offset);
				int routingEntryIndex = (routingEntryIndexBase+offset)%routingArray.length;
				entryManifest[n] = routingArray[routingEntryIndex];
			}

			//Build ID and Address arrays for message
			int id[] = new int[numberOfTableEntries];
			InetAddress address[] = new InetAddress[numberOfTableEntries];
			for(int n = 0; n < entryManifest.length; n++){
				id[n] = entryManifest[n].getId();
				address[n] = entryManifest[n].getAddress();
			}

			//System.out.println(Arrays.toString(id));
			//System.out.println(Arrays.toString(address));

			//Build REGISTRY_NODE_SENDS_MANIFEST message to that node
			RegistrySendsNodeManifest rsnm = new RegistrySendsNodeManifest(numberOfTableEntries
																																			, id
																																			, address
																																			, allIds);

			//Send message
			Connection messagingNodeConnection = routingArray[i].getConnection();
			messagingNodeConnection.getSender().write(rsnm.getBytes());
		}
	}


	public static void main(String args[]){
		//The arguement for this is the portnumber to run on
		Registry registry = new Registry(Integer.parseInt(args[0]));//args[0] being the portnum

		InteractiveCommandParser parser = new InteractiveCommandParser(registry);

		/*InetAddress addr = InetAddress.getLoopbackAddress();
		try{
			addr = InetAddress.getLocalHost();
			//addrByte = addr.getAddress();
		}catch(UnknownHostException e){
			System.out.println("Error finding address");
		}
		*/
	}//End main



}//End class
