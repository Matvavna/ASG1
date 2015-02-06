package cs455.overlay.routing;

/*
 *Author: Tiger Barras
 *RoutingEntry.java
 *
 */

import java.io.InetAddress;

public class RoutingEntry{

	//Contains information about the node this entry points to
	//Id, address, and port

	InetAddress address;
	int id;
	int port;

	public RoutingEntry(InetAddress a, int i, int p){
		address = a;
		id = i;
		port = p;
	}

}//End class
