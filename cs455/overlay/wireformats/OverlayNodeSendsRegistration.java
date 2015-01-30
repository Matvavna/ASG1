package cs455.overlay.wireformats;
/*
*Author: Tiger Barras
*OverlayNodeSendsRegistration.java
*Wireformat for when a node sends it's registration to the register
*/

import cs455.overlay.wireformats.Event;
import java.net.InetAddress;
import java.util.Arrays;

/*----------FORMAT-----------
 *Byte: Message Type (OVERLAY_NODE_SENDS_REGISTRATION)
 *Byte: Length of following "IP Address" field
 *Byte[^^]: IP Address; from InetAddress.getAddress()
 *int: Port Number
 */
public class OverlayNodeSendsRegistration implements Event{

  int messageType = 2;
	int length;
  byte[] IPByte;
	InetAddress IP;
	int portNumber;

  //Constructor to make an object out of an incoming byte array
	public OverlayNodeSendsRegistration(byte[] data){
		length = data[1];
    IPByte = Arrays.copyOfRange(data,2,2+length);
    IP = InetAddress.getByAddress(IPByte);//Turns the byte array into an actual InetAddress
    portNumber = data[2+length];
	}

  //Another constructor to make an object out of the data fields


  //Method to return the byte array

}
