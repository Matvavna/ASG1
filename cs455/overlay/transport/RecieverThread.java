package cs455.overlay.transport;
/*
*Author: Tiger Barras
*RecieverThread.java
*Object that will send messages over a socket connection
*/

import java.net.Socket;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.DataInputStream;


public class RecieverThread extends Thread{

	Socket socket; //Socket that Reciever will listen to
	DataInputStream din;

	public RecieverThread(Socket s){
		socket = s;
		try{
			din = new DataInputStream(socket.getInputStream());
		}catch(IOException e){
			System.out.println("RecieverThread: Error opening DataInputStream");
			System.out.println(e);
		}
	}//End constructor

	public void run(){
		while(socket != null){
			try{
				int dataLength = din.readInt(); //Number of bytes to read
				byte[] data = new byte[dataLength];
				din.readFully(data, 0, dataLength);
				System.out.println(data);
			}catch(IOException e){
				System.out.println("Error reading from BufferedReader");
				System.out.println(e);
			}
		}
	}

	//Need functionality for listening to a socket

}//End class
