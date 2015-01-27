package cs455.overlay.transport;
//Author: Tiger Barras
//ConnectionCache.java
//Holds an array of Sockets.
//This will be inherited by two classes:
//  RegisterConnectionCache
//  NodeConnectionCache
//These will have error/sanity checking
//  e.g. NodeConnectionCache will no let you have more that four connections

import java.net.Socket;

public interface ConnectionCache{

	//public ConnectionCache();

	public void add(int index, Socket s);

	public Socket get(int index);

	public int size();

}//End class
