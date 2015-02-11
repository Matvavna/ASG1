package cs455.overlay.wireformats;
/*
 *Author: Tiger Barras
 *RegistrySendsNodeManifest.java
 *Wireformat the registry uses to send a MessageNode it's routing table entries
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
import java.io.UnsupportedEncodingException;



public class RegistrySendsNodeManifest implements Event{

	private byte[] message;
	private int messageType = 6;
	private int routingTableSize;

	//Arrays to hold data about nodes in routing table
	//They correspond, so id[0] refers to the same node as address[0]
	private int[] id;
	private byte[][] addressBytes;
	private InetAddress[] address;

	//The list of add the ID's in the overlay
	int[] allIds;

	public RegistrySendsNodeManifest(byte[] data){

	}//End constructor

	public RegistrySendsNodeManifest(int size, int[] _id, InetAddress[] _address, int[] _allIds){
		routingTableSize = size;
		id = _id;
		address = _address;
		allIds = _allIds;


		try{
			message = null;
			//Initialize streams
			ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
			DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

			//Build up byte stream
			dout.writeInt(messageType);
			dout.writeInt(routingTableSize);
			//loop through arrays to build entry for each node in routing table
			for(int i = 0; i < id.length; i++){
				//Write info of node 2^i hops away
				dout.writeInt(id[i]);
				//Turn address for this node into byte[]
				addressBytes[i] = address[i].getAddress();
				int addressLength = addressBytes[i].length;
				dout.writeInt(addressLength);//Length of following address
				dout.write(addressBytes[i]);//Actual address in byte[] form
			}//Done writing routing table entries
			//write the list of all the nodes in the overlay
			dout.writeInt(allIds.length);//Number of node IDs
			for(int entry : allIds){
				dout.writeInt(entry);
			}//Done writing list of entries

			//Pull byte array from stream
			dout.flush();
			message = baOutputStream.toByteArray();

			//Close streams
			baOutputStream.close();
			dout.close();
		}catch(IOException e){
			System.out.println("RSNM: Error marshalling message");
			System.out.println(e);
		}

	}//End constructor


	public int getType(){
		return messageType;
	}//End getType

	//Method to return the byte array
	public byte[] getBytes(){
		return message;
	}//End getBytes

	public String toString(){
		System.out.println("Generating printout for Event subclass");
		String toReturn = "RegistrySendsNodeManifest: \n";
		toReturn = toReturn.concat("  Nodes in manifest: " + Arrays.toString(id) + "\n");
		toReturn = toReturn.concat("  All node IDs in overlay: " + Arrays.toString(allIds) + "\n");
		return toReturn;
	}//End toString

	public Socket getSocket(){
		return null;
	}//End getSocket

	public int[] getIds(){
		return id;
	}//End getIds

	public InetAddress[] getAddress(){
		return address;
	}//End getAddress

	public int[] getAllIds(){
		return allIds;
	}//End getAllIds
}
