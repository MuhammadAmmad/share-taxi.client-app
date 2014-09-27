package com.panamana.sharetaxi.lines.objects;

/**
 * this is the end stations object of the line
 * @author naama
 *
 */
public class EndStations {
	
	String startStation;
	String endStation;
	
	public EndStations(String startStation, String endStation) {
		super();
		this.startStation = startStation;
		this.endStation = endStation;
	}

	public String getStartStation() {
		return startStation;
	}

	public String getEndStation() {
		return endStation;
	}
	
	

}
