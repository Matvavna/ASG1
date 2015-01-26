//Author: Tiger Barras
//TestServer.java
//Just a place for me to mess around with my objects as they progress

import cs455.overlay.transport.ServerThread;
import cs455.overlay.transport.NodeConnectionCache;

public class TestServer{

	public static void main(String args[]){
		NodeConnectionCache cache = new NodeConnectionCache();
		ServerThread st = new ServerThread(5000, cache);
		st.start();
		while(cache.size() == 0){
			//chill
		}
	}//End main

}
