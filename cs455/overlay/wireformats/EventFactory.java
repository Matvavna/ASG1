package cs455.overlay.wireformats;
/*
*Author: Tiger Barras
*EventFactory.java
*Creates events out of byte arrays
*/

import cs455.overlay.wireformats.*;
import java.net.UnknownHostException;

public class EventFactory{

	private static EventFactory instance = null;

	protected EventFactory(){
		//Only exists to defeat instantiation
	}//End constructor

	//This is synchronized because that if statement is not
	  //Thread safe
	public synchronized static EventFactory getInstance(){
		if(instance == null){
			instance = new EventFactory();
		}

		return instance;
	}//End getInstance

	public static Event manufactureEvent(byte[] data)throws UnknownHostException{
		Event event = new OverlayNodeSendsRegistration(data);

		int type = data[0];

		//Add logic here to turn the byte stream into an event...homie
		switch(type){
			case 2:	 event = new OverlayNodeSendsRegistration(data);
							 break;
			default: System.out.println("That message is not even a real message!");
							 System.exit(-1);
		}

		return event;
	}//End manufactureEvent
}//End class
