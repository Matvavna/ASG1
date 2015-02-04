package cs455.overlay.transport;
//Author: Tiger Barras
//RegistryConnectionCache.java
//Holds a HashMap of port:Connections.

import java.util.concurrent.ConcurrentHashMap;
import cs455.overlay.transport.Connection;
import cs455.overlay.exception.ConnectionCacheException;

public interface ConnectionCache{

	//public ConnectionCache();

	public void add(int index, Connection c){
		cache.put(index, c);
	}//End add

	public Connection get(int index)throws ConnectionCacheException{
		if(!cache.containsKey(index)){//If  there is no connection tied to this port in the map
			throw new ConnectionCacheException("Index not found in cache");
		}else{
			Connection toReturn = cache.get(index);
			return toReturn;
		}
	}//End get

	public int size(){
		return cache.size();
	}//End size

}//End class
