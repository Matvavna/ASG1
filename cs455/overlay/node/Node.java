package cs455.overlay.node;

//Author: Tiger Barras
//Node class. Will be inherited by Registry and MessagingNode classes

import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.ArrayList;

public class Node{

  /*
   *This class should use objects from cs455.overlay.transport
   *  Those objects will let you send and recieve shit.
   */

  //Listens at a specific port, and then passes out a Socket
  public Socket listen(int portNum) throws IOException{
    ServerSocket serverSocket = new ServerSocket(portNum);
    System.out.println("Waiting for connection on port " + portNum);
    Socket socket = serverSocket.accept();
    return socket;
  }//end listen

  public static void main(String args[]){

  }
}
