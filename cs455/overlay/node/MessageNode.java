package cs455.overlay.node;
/*
*Author: Tiger Barras
*MessageNode.java
*Top level class that generates, routes, and recieves packages from other Nodes
*/

import cs455.overlay.node.Node;
import cs455.overlay.transport.NodeConnectionCache;
import cs455.overlay.transport.ServerThread;
import cs455.overlay.transport.RecieverThread;
import cs455.overlay.wireformats.Event;
import java.net.Socket;
import java.io.IOException;
import java.util.Scanner;

//Remember that connections between nodes only need to be one way
//Other than the registry
public class MessageNode implements Node{

	//Need a cache for the sockets I'm going to send messages to
	//I'll start receiverThreads to deal with the ones talking to me
	NodeConnectionCache cache = new NodeConnectionCache();

	public MessageNode(int portNum){
		//Get the server set up
		try{
			this.startServer(portNum);
		}catch(IOException e){
			System.out.println("MessageNode: Could not start ServerThread");
			System.out.println(e);
		}
	}

	//This is what will get called when something happens
	//Such as a message coming in, or a new link being opened
	public void onEvent(Event e){

	}//End onEvent

	//Listens at a specific port, and then passes out a Socket
	public void startServer(int portNum) throws IOException{
		ServerThread server = new ServerThread(portNum);
		server.start();
	}//End startServer

	//Spans a Reciever thread that is linked to the specified socket
	public void spawnRecieverThread(Socket socket){
		RecieverThread reciever = new RecieverThread(socket);
		reciever.start();
	}//End spawnRecieverThread

	public static void main(String args[]){
		MessageNode node = new MessageNode(5000);
		Scanner sc = new Scanner(System.in);
		sc.next();

	}//End main

}
