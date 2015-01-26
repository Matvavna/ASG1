//Author: Tiger Barras
//TestMain.java
//Just a place for me to mess around with my objects as they progress

import cs455.overlay.transport.ServerThread;

public class TestMain{

	public static void main(String args[]){
		ServerThread st = new ServerThread(5000);
		st.start();
	}//End main

}
