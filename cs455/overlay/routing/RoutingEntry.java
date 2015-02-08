package cs455.overlay.routing;

/*
 *Author: Tiger Barras
 *RoutingEntry.java
 *Aggregates address, port, id, and connection for a node
 *This is how connections are stored long term
 */

import java.net.InetAddress;
import cs455.overlay.transport.Connection;

public class RoutingEntry{

	//Contains information about the node this entry points to
	//Id, address, and port

	private InetAddress address;
	private int id;
	private int port;
	private Connection connection;

	public RoutingEntry(InetAddress a, int i, int p, Connection c){
		address = a;
		id = i;
		port = p;
		connection = c;
	}//End constructor

	public InetAddress getAddress(){
		return address;
	}//End getAddress

	public int getId(){
		return id;
	}//End getId

	public int getPort(){
		return port;
	}//End getPort

	public Connection getConnection(){
		return connection;
	}//End getConnection

	public String toString(){
		String toReturn = "";
		toReturn = toReturn.concat("Routing Entry");
		toReturn = toReturn.concat("  HostName: " + address.getHostName());
		toReturn = toReturn.concat("  ID:" + String.valueOf(id));
		toReturn = toReturn.concat("  port: " + String.valueOf(port));
		return toReturn;
	}

}//End class
