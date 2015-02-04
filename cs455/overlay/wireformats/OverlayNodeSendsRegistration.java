package cs455.overlay.wireformats;
/*
*Author: Tiger Barras
*OverlayNodeSendsRegistration.java
*Wireformat for when a node sends it's registration to the register
*/

import cs455.overlay.wireformats.Event;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/*----------FORMAT-----------
 *Byte: Message Type (OVERLAY_NODE_SENDS_REGISTRATION)
 *Byte: Length of following "IP Address" field
 *Byte[^^]: IP Address; from InetAddress.getAddress()
 *int: Port Number
 */
public class OverlayNodeSendsRegistration implements Event{

  byte[] message;
  int messageType = 2;
	int length;
  byte[] IPByte;
	InetAddress IP;
	int portNumber;

  //Constructor to make an object out of an incoming byte array
	public OverlayNodeSendsRegistration(byte[] data)throws UnknownHostException{
    message = data;
		length = data[1];
    IPByte = Arrays.copyOfRange(data,2,2+length);
    IP = InetAddress.getByAddress(IPByte);//Turns the byte array into an actual InetAddress
    portNumber = data[2+length];
	}//End incoming constructor

  //Another constructor to make an object out of the data fields
  public OverlayNodeSendsRegistration(InetAddress addr, int port){
    //Populate fields
    IP = addr;
    IPByte = addr.getAddress();
    length = IPByte.length;

    //Build message
    message = new byte[1+1+length+1];//One each for type, length and port, then length bytes for the address
    message[0] = (byte)messageType;
    message[1] = (byte)length;
    for(int i = 0; i < IPByte.length; i++){
      message[i+2] = IPByte[i];
    }
    message[1+1+length] = (byte)portNumber;
  }//End outgoing constructor

  public int getType(){
    return messageType;
  }//End getType

  //Method to return the byte array
  public byte[] getBytes(){
    return message;
  }//End getBytes

  public String toString(){
    System.out.println("Generating printout for Event subclass");
    String toReturn = "";
    toReturn.concat("OverlayNodeSendsRegistration: \n");
    toReturn.concat("  Length -> " + length + "\n");
    toReturn.concat("  Sender's Address -> " + IP + "\n");
    toReturn.concat("  Sender's Port Number -> " + portNumber + "\n");
    return toReturn;
  }//End toString

}
