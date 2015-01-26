package cs455.overlay.node;

//Author: Tiger Barras
//Node class. Will be inherited by Registry and MessagingNode classes

import java.net.ServerSocket;
import java.io.*;
import java.util.arrayList;

public class Node{

  //Listens at a specific port, and then passes out a Socket
  public Socket listen(int portNum) throws IOException{
    serverSocket = new ServerSocket(portNum);
    System.out.println("Waiting for connection on port " + portnum);
    Socket socket = serverSocket.accept();
    return socket;
  }//end listen

  public static void main(String args[]){

  }
}
