package cs455.overlay.util;

/*
 *Author: Tiger Barras
 *SummaryAggregator.java
 *Collects on to all of those delicious numbers that the overlay nodes are sending in
 *The summaries are held in a NodeSummary object
 */

import java.util.ArrayList;

import cs455.overlay.wireformats.OverlayNodeReportsTrafficSummary;


public class SummaryAggregator{

	private ArrayList<OverlayNodeReportsTrafficSummary> summaries = new ArrayList<OverlayNodeReportsTrafficSummary>();

	public synchronized void addSummary(OverlayNodeReportsTrafficSummary summary){
		summaries.add(summary);
	}//End addSummary

	public String toString(){
		//Totals
		int totalSent = 0;
		int totalRecieved = 0;
		int totalRelayed = 0;
		long totalValuesSent = 0;
		long totalValuesRecieved = 0;

		String toReturn = "     Sent     Recieved     Relayed     Sum Values Sent    Sum Values Recieved";

		for(OverlayNodeReportsTrafficSummary summary : summaries){
			toReturn = toReturn.concat(summary.toString());

			totalSent += summary.getSent();
			totalRecieved += summary.getRecieved();
			totalRelayed += summary.getRelayed();
			totalValuesSent += summary.getSentDataSum();
			totalValuesRecieved += summary.getRecievedDataSum();
		}

		toReturn = toReturn.concat(String.format("Sum  %d  %d  %d  %l  %l\n",totalSent, totalRecieved, totalRelayed, totalValuesSent, totalValuesRecieved));

		return toReturn;
	}


}//End class
