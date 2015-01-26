package cs455.overlay.transport;
/*
 *Author: Tiger Barras
 *RegisterConnectionCache.java
 *Wrapper for a socket array that holds all the nodes that talk to the register
 */

import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class RegisterConnectionCache implements ConnectionCache{

	ConcurrentHashMap<Integer,Socket> cache = new ConcurrentHashMap<Integer, Socket>();

	public void add(int index, Socket s){
		cache.put(index,s);
	}//End add

	//Make this throw a ConnectionCacheException
	public Socket get(int index){

		return new Socket();
	}//End get


}//End class
