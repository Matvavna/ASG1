package cs455.overlay.transport;
/*
*Author: Tiger Barras
*Connection.java
*Holds pairs of senders and receiver threads. These will be stored in the cache
*/

import cs455.overlay.transport.RecieverThread;
import cs455.overlay.transport.Sender;
import java.net.Socket;

public class Connection{

	private RecieverThread reciever;
	private Sender sender;

	public Connection(RecieverThread r, Sender s){
		this.reciever = r;
		this.sender = s;
	}

	public Connection(Socket s){
		this.reciever = new RecieverThread(s);
		this.sender = new Sender(s);
	}

	public RecieverThread getReciever(){
		return this.reciever;
	}

	public Sender getSender(){
		return this.sender;
	}

}
