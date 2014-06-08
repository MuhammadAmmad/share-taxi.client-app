package com.panamana.sharetaxi.directions;

import android.text.format.Time;

import com.google.android.gms.maps.model.LatLng;
import com.panamana.sharetaxi.directions.parser.DirectionJSONParserTask;
import com.panamana.sharetaxi.directions.tasks.GetDirectionsTask;
import com.panamana.sharetaxi.lines.objects.Line;
import com.panamana.sharetaxi.model.utils.LocationUtils;

/**
 * Google Directions API manager class.
 * @author 
 */
public class DirectionsManager {

	/* 
	 * Example Requests:
	 * https://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&sensor=false
	 * http://maps.googleapis.com/maps/api/directions/json?origin=Boston,MA&destination=Concord,MA&waypoints=Charlestown,MA|via:Lexington,MA&sensor=false
	 * reference:
	 * https://developers.google.com/maps/documentation/directions/#Waypoints
	 */
	
	/**
	 * 
	 * @param src
	 * @param dst
	 */
	static public void getDirection(String src, String dst, String... waypoints) {
		new GetDirectionsTask().execute(buildDirectionRequest(src,dst,waypoints));
	}
	
	/**
	 * 
	 * @param src
	 * @param dst
	 */
	public static void drawRoute(Line line) {
		new GetDirectionsTask().execute(buildDirectionRequest(
				LocationUtils.latlng2String(line.getStart()),
				LocationUtils.latlng2String(line.getEnd()),
				LocationUtils.latlng2String(line.getWaypoints())));
	}

	static public void parseJson(String json){
		DirectionJSONParserTask parserTask = new DirectionJSONParserTask();
        // Invokes the thread for parsing the JSON data
        parserTask.execute(json);
	}
	
	public static String buildDirectionRequest(String origin, String destination, String[]waypoints){

		String result = "https://maps.googleapis.com/maps/api/directions/json?" +
		"origin=" + origin + 
		"&" +
		"destination=" + destination +
		"&" +
		"sensor=" + "true" +
		"&" + 
		"mode=" + "driving" +
		"departure_time=" + System.currentTimeMillis();
		if(waypoints!= null && waypoints.length>0) {
			result += "&"+"waypoints=" + "via:" + waypoints[0]; 
			for (int i=1; i<waypoints.length; i++) {
				result += "%7C" + "via:" + waypoints[i];
			}
		}
		return result;
	}

	/**
	 * 
	 * @param json
	 */
	public static void handleDirectionReponse(String json) {
		parseJson(json);
	}

}
