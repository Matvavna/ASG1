package cs455.overlay.transport;
//Author: Tiger Barras
//ServerThread.java
//Waits for incoming connections, then opens a socket with them

public class ServerThread extends Thread{

	int portNum; //Port that the ServerThread object will listen on

	public ServerThread(int pn){
		portNum = pn;
	}//End constructor

	//This is where execution begins when this thread is created
	public void run(){
		
	}

}//End class
