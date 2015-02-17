package cs455.overlay.wireformats;
/*
*Author: Tiger Barras
*RegistryRequestsTaskInitiate.java
*Wireformat the Registry uses to tell the message nodes to marshall and send their summaries
*/

import cs455.overlay.wireformats.Event;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

public class RegistryRequestsTrafficSummary implements Event{

	private byte[] message = {11};
	private int messageType = 11;



	public byte[] getBytes(){
		return message;
	}//End getBytes

	public int getType(){
		return messageType;
	}//End getType

	public String toString(){
		return "REGISTRY_REQUESTS_TRAFFIC_SUMMARY";
	}//End toString

	public Socket getSocket(){
		return null;
	}//End getSocket



}
