package cs455.overlay.util;

/*
 *Author: Tiger Barras
 *InteractiveCommandParser.java
 *Parses the commands for nodes
 */

import cs455.overlay.node.Node;
import cs455.overlay.node.Registry;
import cs455.overlay.node.MessageNode;

import java.util.Scanner;

public class InteractiveCommandParser{

	Registry registry;
	MessageNode messageNode;
	int flag;//Lets the parser know whether it's parsing for a MessageNode or a Registry
				//Zero for Register, one for MessageNode

	public InteractiveCommandParser(Registry r){
		registry = r;

		this.registryListen();
	}//End constructor

	public InteractiveCommandParser(MessageNode mn){
		messageNode = mn;

		this.messageNodeListen();
	}//End constructor

	public void registryListen(){
		System.out.println("Starting command parser");
		Scanner sc = new Scanner(System.in);

		String input;

		while(true){//Just sit in this loop and listen for input
			System.out.print(">> ");
			input = sc.next();
			parseRegistry(input, sc);
		}
	}//End listen

	//The scanner gets passed in here so command arguements can be read
	public void parseRegistry(String input, Scanner sc){
		switch(input){
			//Registry commands
			case "list-messaging-nodes":
					this.registry.listMessagingNodes();
					break;
			case "setup-overlay":
					int numberOfRoutingTableEntries = Integer.parseInt(sc.next());
					//this.registry.setupOverlay(numberOfRoutingTableEntries);
					break;
			default:
					System.out.println("Not a valid command");
		}
	}//End parseRegistry

	public void messageNodeListen(){
		System.out.println("Starting command parser");
		Scanner sc = new Scanner(System.in);

		String input;

		while(true){//Just sit here and listen for input
			System.out.print(">> ");
			input = sc.next();
			parseMessageNode(input);
		}
	}//End MessageNodeListen

	public void parseMessageNode(String input){
		switch(input){
			//Registry commands
			case "exit-overlay":
					this.messageNode.exitOverlay();
					break;
			default:
					System.out.println("Not a valid command");
		}
	}//End parseMessageNode



}
