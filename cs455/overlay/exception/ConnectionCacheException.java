package cs455.overlay.exception
/*
*Author: Tiger Barras
*ConnectionCacheException
*This will be thrown when the cache does not function properly
*/

public class ConnectionCacheException extends RunTimeException{

	public String toString(){
		return "Error in ConnectionCache.\n
			Is the connection you're looking for in this cache?";
	}

}//End class
