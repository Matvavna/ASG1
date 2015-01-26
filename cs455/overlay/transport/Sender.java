package cs455.overlay.transport;
/*
 *Author: Tiger Barras
 *Sender.java
 *Object that will send messages over a socket connection
 */

import java.net.Socket;
import java.io.*;

public class Sender{

	Socket socket; //Socket Sender will talk in to
	PrintWriter pw;

	public Sender(Socket s){
		socket = s;
		try{//Open printwriter on the output stream of the socket
			pw = new PrintWriter(socket.getOutputStream());
		}catch(IOException e){
			System.out.println("Sender: Error opening Printwriter");
			System.out.println(e);
		}
	}//End constructor

	public void write(String message){
		pw.println(message);
		pw.flush();
	}//End write

	//Need functionality for sending data over a socket

}//End class
