package cs455.overlay.util;

/*
 *Author: Tiger Barras
 *SummaryAggregator.java
 *Collects on to all of those delicious numbers that the overlay nodes are sending in
 *The summaries are held in a NodeSummary object
 */

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import cs455.overlay.wireformats.OverlayNodeReportsTrafficSummary;


public class SummaryAggregator{

	private ArrayList<OverlayNodeReportsTrafficSummary> summaries = new ArrayList<OverlayNodeReportsTrafficSummary>();

	public synchronized void addSummary(OverlayNodeReportsTrafficSummary summary){
		summaries.add(summary);
	}//End addSummary

	public String toString(){
		//Totals
		AtomicInteger totalSent = new AtomicInt(0);
		AtomicInteger totalRecieved = new AtomicInt(0);
		AtomicInteger totalRelayed = new AtomicInt(0);
		AtomicLong totalValuesSent = new AtomicLong(0);
		AtomicLong totalValuesRecieved = new AtomicLong(0);

		String toReturn = "     Sent     Recieved     Relayed     Sum Values Sent    Sum Values Recieved";

		for(OverlayNodeReportsTrafficSummary summary : summaries){
			toReturn = toReturn.concat(summary.toString() + "\n");

			totalSent.getAndAdd(summary.getSent());
			totalRecieved.getAndAdd(summary.getRecieved());
			totalRelayed.getAndAdd(summary.getRelayed());
			totalValuesSent.getAndAdd(summary.getSentDataSum());
			totalValuesRecieved.getAndAdd(summary.getRecievedDataSum());
		}

		toReturn = toReturn.concat(String.format("Sum  %d  %d  %d  %d  %d\n",totalSent, totalRecieved, totalRelayed, totalValuesSent, totalValuesRecieved));

		return toReturn;
	}


}//End class
