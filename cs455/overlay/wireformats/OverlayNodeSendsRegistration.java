package cs455.overlay.wireformats;
/*
*Author: Tiger Barras
*OverlayNodeSendsRegistration.java
*Wireformat for when a node sends it's registration to the register
*/

import cs455.overlay.wireformats.Event;
import java.net.InetAddress;

/*----------FORMAT-----------
 *Byte: Message Type (OVERLAY_NODE_SENDS_REGISTRATION)
 *Byte: Length of following "IP Address" field
 *Byte[^^]: IP Address; from InetAddress.getAddress()
 *int: Port Number
 */
public class OverlayNodeSendsRegistration implements Event{

  int messageType = 2;
	int length;
	InetAddress IP;
	int portNumber;

  //Constructor to make an object out of an incoming byte array
	public OverlayNodeSendsRegistration(byte[] data){
		portNumber = pn;
		length = data[1];

	}

  //Another constructor to make an object out of the data fields


  //Method to return the byte array

}
