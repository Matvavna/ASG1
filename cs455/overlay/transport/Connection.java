package cs455.overlay.transport;
/*
*Author: Tiger Barras
*Connection.java
*Holds pairs of senders and receiver threads. These will be stored in the cache
*/

import cs455.overlay.node.Node;
import cs455.overlay.node.MessageNode;
import cs455.overlay.node.Registry;
import cs455.overlay.transport.RecieverThread;
import cs455.overlay.transport.Sender;
import cs455.overlay.wireformats.*;
import java.net.Socket;

public class Connection{

	private Node node;
	private RecieverThread reciever;
	private Sender sender;

	public Connection(Node n, RecieverThread r, Sender s){
		this.node = n;
		this.reciever = r;
		this.sender = s;
	}

	public Connection(Node n, Socket s){
		node = n;
		this.reciever = new RecieverThread(node, s);
		reciever.start();
		this.sender = new Sender(s);
	}

	public RecieverThread getReciever(){
		return this.reciever;
	}

	public Sender getSender(){
		return this.sender;
	}

}
