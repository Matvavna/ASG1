package cs455.overlay.node;
/*
 *Author: Tiger Barras
 *MessageNode.java
 *Top level class that generates, routes, and recieves packages from other Nodes
 */

import cs455.overlay.node.Node;
import cs455.overlay.transport.ConnectionCache;
import cs455.overlay.transport.NodeConnectionCache;
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

	}//End onEvent

	//Listens at a specific port, and then passes out a Socket
	public void startServer(int portNum) throws IOException{
		ServerThread server = new ServerThread(portNum, this);
		server.getPortNum();
		server.start();
	}//End startServer

	//Spans a Reciever thread that is linked to the specified socket
	public void spawnRecieverThread(Socket socket){
		RecieverThread reciever = new RecieverThread(socket);
		reciever.start();
	}//End spawnRecieverThread

	public ConnectionCache getConnectionCache(){
		return cache;
	}//End getConnectionCache

	//MAIN
	//Currently only for testing
	public static void main(String args[]){
		//Right now we're specifying the port at the command line
		//Eventually, we need to set it to zero
		MessageNode node = new MessageNode();
		//System.out.println("Still in main!");
		Scanner sc = new Scanner(System.in);
		try{
			System.out.println(InetAddress.getLocalHost());
		}catch(UnknownHostException e){
			System.out.println("Oopsy");
		}
		String address = sc.next();
		//System.out.println("I hope that was a g");
		//System.out.println("I only fuck with g's");
		//Ok so at this point, the server is set up, now we need to connect to it
		InetAddress addr = InetAddress.getLoopbackAddress();
		try{
			addr = InetAddress.getLocalHost();
			//addrByte = addr.getAddress();
		}catch(UnknownHostException e){
			System.out.println("Error finding address");
		}
		OverlayNodeSendsRegistration form = new OverlayNodeSendsRegistration(addr, Integer.parseInt(args[1]));
		try{
			Socket socket = new Socket(address, Integer.parseInt(args[1]));
			Sender sender = new Sender(socket);
			sender.write(form.getBytes());
		}catch(UnknownHostException e){
			System.out.println("Temp: Shit");
		}catch(IOException e){
			System.out.println("Temp: Shitty");
			System.out.println(e);
		}
	}//End main

}
