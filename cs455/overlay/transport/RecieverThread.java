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


public class RecieverThread extends Thread{

	Socket socket; //Socket that Reciever will listen to
	BufferedReader br;

	public RecieverThread(Socket s){
		socket = s;
		try{
			InputStreamReader isr = new InputStreamReader(socket.getInputStream());
			br = new BufferedReader(isr);
		}catch(IOException e){
			System.out.println("RecieverThread: Error opening BufferedReader");
			System.out.println(e);
		}
	}//End constructor

	public void run(){
		while(true){
			try{
				if(br.ready()){
					System.out.println(br.readLine());
				}
			}catch(IOException e){
				System.out.println("Error reading from BufferedReader");
				System.out.println(e);
			}
		}
	}

	//Need functionality for listening to a socket

}//End class
