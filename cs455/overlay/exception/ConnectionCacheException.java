package cs455.overlay.exception;
/*
*Author: Tiger Barras
*ConnectionCacheException
*This will be thrown when the cache does not function properly
*/

public class ConnectionCacheException extends RuntimeException{

	public String toString(){
		return "Error in ConnectionCache.\nIs the connection you're looking for in this cache?";
	}

}//End class
