package com.panamana.sharetaxi.cars.objects;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;
import com.panamana.sharetaxi.R;
import com.panamana.sharetaxi.cars.CarsWorker;
import com.panamana.sharetaxi.cars.locations.parser.LocationsJSONParser.LocationsJsonTags;
import com.panamana.sharetaxi.directions.DirectionsManager;
import com.panamana.sharetaxi.directions.parser.DirectionsJSONParser;
import com.panamana.sharetaxi.directions.tasks.GetDirectionsTask;
import com.panamana.sharetaxi.lines.LINES;
import com.panamana.sharetaxi.model.maps.MapManager;
import com.panamana.sharetaxi.model.utils.DirectionalVector;
import com.panamana.sharetaxi.model.utils.LocationUtils;
import com.panamana.sharetaxi.model.utils.Position;

/**
 * this is the Car object that gets the JSON data from the locations server
 * response example:
 * "androidID": "1234", "date": 1399279938.0, "lineNum": "5", "longitude": "34.7833047", "latitude": "32.0987302"
 * @author naama
 */
public class Car {

	private static final String TAG = Car.class.getSimpleName();
	private static final boolean DEBUG = false;
	private static final int FIND_CLOSEST_SEGMENT_FILTER = 3;


	// Fields:
	private String mID;
	private String mTime;
	private String mDirection;
	private String mPrevDirection;
	private LatLng mLatLng;
	private LatLng mPrevLatLng;
	private String mLineName;
	private String mFreeSeats;
	private Marker mMarker;
	private int mIRouteLocation;
	private float mDistanceFromI;
	private int mIcon;
	private int mIterator;
	private String mNewDirection;
	private float mLocalDirection;
	private boolean mIsActive;
	private static String mEstimatedTime;
	
	// Constructor:
	public Car (String ID, String time, String line, LatLng latlng, String freeSeats ) {
		this.mID = ID;
		this.mTime = time;
		this.mLineName = line;
		if (CarsWorker.cars.get(ID) != null) {
			this.mPrevLatLng = CarsWorker.cars.get(ID).getLatLng();
		} else {
			mPrevLatLng = latlng;
		}
		this.mLatLng = latlng;
		if (freeSeats.equals("null")) {
			this.mFreeSeats = "";
		} else {
			this.mFreeSeats = freeSeats;
		}
		this.mMarker = null;
		this.mIRouteLocation = 0;
		this.mDistanceFromI = 0;
		this.mDirection = "";
		this.mIcon = R.drawable.l5north;
		this.mIterator = 0;
		this.mNewDirection = "";
		this.mPrevDirection = "";
		this.mLocalDirection = -1;
		this.mIsActive = true;
	}
	public Car (JSONObject jo) throws JSONException {
		this(
			jo.getString(LocationsJsonTags.ANDROIDID),           
			jo.getString(LocationsJsonTags.DATE),               
			jo.getString(LocationsJsonTags.LINENUM),          
			new LatLng( jo.getDouble(LocationsJsonTags.LATITUDE), 
	                  	jo.getDouble(LocationsJsonTags.LONGITUDE)),
			jo.getString(LocationsJsonTags.FREE_SEATS)
			);
	}
	
	public String getDirection() {
		return mDirection;
	}
	
	public void updateCarDirection() {
		// the prev object of the same car in CarsWorker.cars
		Car carByID = CarsWorker.cars.get(mID);
		if (carByID == null) {
			carByID = this;
		}
		// I root location - the I'th segment of the route

		mLocalDirection = LocationUtils.latLng2Location(mPrevLatLng).bearingTo(LocationUtils.latLng2Location(mLatLng));
		
		updateCarDirection2(carByID);

		CarsWorker.cars.put(mID, this);
	}
	
	public void updateCarDirection2(Car carByID) {

		// I root location - the I'th segment of the route
		int prevIRouteLocation = carByID.getIRouteLocation();
		mPrevDirection = carByID.getDirection();
		mIterator = carByID.getIterator();
		mNewDirection = carByID.getNewDirection();
		this.calcIRouteLocationAndDistance();
		if (prevIRouteLocation != Float.MAX_VALUE) {
			// if car was not just initialized 
			if(DEBUG) Log.i(TAG,"save prev location");

			float prevDistanceFromI = carByID.getDistanceFromI();
			if (prevIRouteLocation == this.mIRouteLocation) {
				// if car is still on the same I-th segment of the root
				if (prevDistanceFromI < this.mDistanceFromI) {
					mDirection = LINES.getLine("line"+mLineName).getEndStations().getEndStation();
				} else {
					mDirection = LINES.getLine("line"+mLineName).getEndStations().getStartStation();
				}
			} else {
				if (prevIRouteLocation < this.mIRouteLocation) {
					mDirection = LINES.getLine("line"+mLineName).getEndStations().getEndStation();
				} else {
					mDirection = LINES.getLine("line"+mLineName).getEndStations().getStartStation();
				}
			}
//			if (direction changed and prevDirection is not "") {
//				check 3 times if mDirection = newDirection
//			if it is, change to newDirection 
//			}
			if(DEBUG) Log.i("1"+TAG,"car "+mID+" i="+mIterator);
			if(mIterator==0) {
				if (!"".equals(mPrevDirection) ) {
					if(DEBUG) Log.i("1.1"+TAG,"car prevDirection is blank");
					if (!mDirection.equals(mPrevDirection)) {
						mNewDirection = mDirection;
						mDirection =mPrevDirection;
						mIterator++;
						if(DEBUG) Log.i("2"+TAG,"car "+mID+" i="+mIterator);
					}
				}
			}
			else {
				if (mIterator>0 && mIterator<3) {
					if(DEBUG) Log.i("3"+TAG,"car "+mID+" i="+mIterator);
					if (mDirection.equals(mNewDirection)) {
						mDirection = mPrevDirection;
						mIterator++;
					}
					else {
						mIterator=0;
					}
				}
				else {
					if (mIterator==3) {
						if(DEBUG) Log.i("4"+TAG,"car "+mID+" i="+mIterator);
						mDirection =mNewDirection;
						mPrevDirection =mNewDirection;
						mIterator=0;
					}
				}
			}
		}
	}		

	public void setDirection(String mDirection) {
		this.mDirection = mDirection;
	}
	public int getIRouteLocation() {
		return mIRouteLocation;
	}
	public void setIRootLocation(int mIRootLocation) {
		this.mIRouteLocation = mIRootLocation;
	}
	public float getDistanceFromI() {
		return mDistanceFromI;
	}
	public void setDistanceFromI(float mDistanceFromI) {
		this.mDistanceFromI = mDistanceFromI;
	}

	// Methods:
	
	public Marker getMarker() {
		return mMarker;
	}
	
	public void setMarker(Marker marker) {
		this.mMarker = marker;
	}
	/**
	 * prefix + "5" OR "4a" OR "4"
	 * @return
	 */
	public String getLineName() {
		return mLineName;
	}

	public void setLineName(String mLineName) {
		this.mLineName = mLineName;
	}

	/**
	 * i.e. 1234
	 * @return
	 */
	public String getID() {
		return mID;
	}

	/**
	 * i.e 1399279938.0
	 * @return
	 */
	public String getTime() {
		return mTime;
	}


	public LatLng getLatLng() {
		return mLatLng;
	}

	@Override
	public String toString() {
		return "Car [mID=" + mID + ", mTime=" + mTime + ", mDirection="
				+ mDirection + ", mLatLng=" + mLatLng + ", mLine=" + mLineName + ", mMarker=" + mMarker 
				+ "]";
	}
	
	
	/**
	 * updates the location of the car on the lines' route and the distance from the last polyline vertex
	 */
	public void calcIRouteLocationAndDistance() {
		PolylineOptions linePolylineOptions = MapManager.polylineOptionsMap.get("line"+mLineName);
		
		if (linePolylineOptions != null) {
			// valid poly line
			int iTHLocation = 0;
			Position carXYZPoint = LatLng2XYZ(mLatLng);
			List<LatLng> linePoints = linePolylineOptions.getPoints(); 
			float distanceFromLine = 10000;
			for (int i = 0; i<linePoints.size()-1; i++) {
				float distanceFromThisLine = 
						Position.distancePfromVectorAB(
								carXYZPoint,
								LatLng2XYZ(linePoints.get(i)),
								LatLng2XYZ(linePoints.get(i+1)));
				if (distanceFromLine < distanceFromLine) {
					// car is closer to this line than to the last one
					distanceFromLine = distanceFromThisLine;
					iTHLocation =i;
				}
				// if the car was closer to the last line than to this one, we can stop iterate over the lines points
				if (distanceFromThisLine > distanceFromLine) {
					break;
				}
			}
			if(DEBUG) {
				Log.i(TAG,"routeLocation"+Integer.toString(this.getIRouteLocation()));
			}
			mIRouteLocation=iTHLocation;
			if (linePoints.size()>0) {
				mDistanceFromI=DirectionalVector.calcDirection(
						LatLng2XYZ(linePoints.get(iTHLocation)),
						carXYZPoint).getVectorSize();
			}
			if(DEBUG) {
				Log.i(TAG,"routeLocation"+Integer.toString(this.getIRouteLocation()));
			}
		}
	}
	

	
	
	
	
	
	/**
	 * updates the location of the car on the lines' route and the distance from the last polyline vertex
	 */
	public void calcIRouteLocationAndDistance2() {
		
		PolylineOptions linePolylineOptions = MapManager.polylineOptionsMap.get("line"+mLineName);
		
		if (linePolylineOptions != null) {
			// valid poly line
			Position carXYZPoint = LatLng2XYZ(mLatLng);
			List<LatLng> linePoints = linePolylineOptions.getPoints(); 

			mIRouteLocation = getClosestSegmentIndex(carXYZPoint, linePoints);
//
//			if(DEBUG) Log.i(TAG,"mIRouteLocation= "+ mIRouteLocation);
//			
//			if (linePoints.get(mIRouteLocation) != null && linePoints.get(mIRouteLocation+5) != null){
//				// got previous and current lines
//				Position prevPos = LatLng2XYZ(linePoints.get(mIRouteLocation)) ;
//				Position curPos = LatLng2XYZ(linePoints.get(mIRouteLocation+1));
//				
//				if(DEBUG) Log.i(TAG,"prevPos="+prevPos+ " curPos= "+curPos);
//				
//				if (DirectionalVector.calcDirection(prevPos,curPos) != null) {
//					// got a valid vector between prev and cur positions
//					mLocalDirection = 
//							(float) Math.atan( (curPos.getY()-prevPos.getY())/(curPos.getX()-prevPos.getX()) );
//							
//							//DirectionalVector.calcDirection(prevPos,curPos).getAngleFromNorth();
//					if(DEBUG) Log.i(TAG,"mLocalDirection= "+mLocalDirection);
//				}
				mDistanceFromI=DirectionalVector.calcDirection(
						LatLng2XYZ(linePoints.get(mIRouteLocation)),
						carXYZPoint).getVectorSize();
//			}
		}
	}
	
	
	private int getClosestSegmentIndex(Position carXYZPoint,
			List<LatLng> linePoints) {
		int minIthSegment = 0;
		int growingDistanceFilter = 0;
		float minDistanceFromLine = Float.MAX_VALUE;
		
		for (int i=0; i<linePoints.size()-1; i++) {
			// iterates over all points in taxi line  
			
			Position prevPos = LatLng2XYZ(linePoints.get(i)) ;
			Position curPos = LatLng2XYZ(linePoints.get(i+1));
			
			float distanceFromThisLine = 
					Position.distancePfromVectorAB(
							carXYZPoint,
							prevPos,
							curPos);
			
			if (distanceFromThisLine < minDistanceFromLine) {
				// segment got closer to the car
				minDistanceFromLine = distanceFromThisLine;
				minIthSegment =i;
//			} else {
//				// segment got further away from the car
//				growingDistanceFilter ++;
//				if (growingDistanceFilter == FIND_CLOSEST_SEGMENT_FILTER) break;
			}
		}
		
		return minIthSegment;
	}
	
	

	
	private String getNewDirection() {
		return mNewDirection;
	}
	private int getIterator() {
		return mIterator;
	}
	private Position LatLng2XYZ(LatLng latlng) {
		float xPos = (float) 6371000 * (float)Math.cos(latlng.latitude) * (float)Math.cos(latlng.longitude);
		float yPos = (float) 6371000 * (float)Math.cos(latlng.latitude) * (float)Math.sin(latlng.longitude);
		float zPos = (float) 6371000 * (float)Math.sin(latlng.latitude);
		Position position = new Position(xPos, yPos, zPos);
		return position;
	}
	
	private Position LatLng2XY (LatLng latlng) {
		float xPos = (float) 6371000 * (float)Math.cos(latlng.latitude) * (float)Math.cos(latlng.longitude);
		float yPos = (float) 6371000 * (float)Math.cos(latlng.latitude) * (float)Math.sin(latlng.longitude);
		Position position = new Position(xPos, yPos, 0);
		return position;
	}

	public int getIcon() {
		if (mLineName.equals("4") && mDirection.equals("North")) {
			mIcon = R.drawable.l4north;
		}
		if (mLineName.equals("4") && mDirection.equals("South")) {
			mIcon = R.drawable.l4north;
		}

		if (mLineName.equals("4a") && mDirection.equals("South")) {
			mIcon = R.drawable.l4anorth;
		}
		if (mLineName.equals("4a") && mDirection.equals("North")) {
			mIcon = R.drawable.l4anorth;
		}
		if (mLineName.equals("5") && mDirection.equals("North")) {
			mIcon = R.drawable.l5north;
		}
		if (mLineName.equals("5") && mDirection.equals("South")) {
			mIcon = R.drawable.l5north;
		}

		
		return mIcon;
	}
	public float getLocalDirection() {
		return mLocalDirection;
	}
	public LatLng getPrevLatLng() {
		return mPrevLatLng;
	}
	public boolean isActive() {
		return mIsActive;
	}
	public void setIsActiveFalse() {
		mIsActive = false;
	}
	public String getFreeSeats() {
		return mFreeSeats;
	}
	
	/**
	 * updates the mEstimatedTime field. 
	 * gets the time estimation from the google directions api between the car LatLng and the closest to user- line point LatLng   
	 * @author naama
	 */
	public void updateEstimatedTime() {
		LatLng myLocation = LocationUtils.location2LatLng(MapManager.getMyLocation());
		LatLng endPoint = getClosestRouteLocation(myLocation);
		Log.i(TAG,"startPoint= " + mLatLng.toString() + "endPoint= " + endPoint.toString());
		GetDirectionsTask gdt = new GetDirectionsTask();
		String response = "";
		try {
			// wait get result from task
//			Log.i(TAG,"buildedReques: ");
			response = gdt.execute(DirectionsManager.buildDirectionRequest(
					LocationUtils.latlng2String(mLatLng),
					LocationUtils.latlng2String(endPoint),
					null)).get(10,TimeUnit.SECONDS);
			if(DEBUG) Log.i(TAG,"response: "+response.toString());
		} catch (Exception e) {
			Log.e(TAG,e.toString());
		}
//		Log.i(TAG,"response= "+ response);
		try {
			JSONObject jo = new JSONObject(response);
			// Parse
			new DirectionsJSONParser().parse(jo);
			mEstimatedTime = DirectionsJSONParser.time; 
			Log.i(TAG,"estimatedTime="+ mEstimatedTime);
		} catch (JSONException joe){
			joe.printStackTrace();
		} catch (NullPointerException npe) {
			npe.printStackTrace();
		} catch (IllegalStateException ise) {
			ise.printStackTrace();
		}
	}
	
	/**
	 * returns the point on the car's line that is closest to the given LatLng
	 * @author naama
	 * @param latLng
	 * @return
	 */
	private LatLng getClosestRouteLocation(LatLng latLng) {
		Location latLngLocation = LocationUtils.latLng2Location(latLng);
		PolylineOptions linePolylineOptions = MapManager.polylineOptionsMap.get("line"+mLineName);
		Location closestILocation = null;
		if (linePolylineOptions != null) {
			// valid poly line
			List<LatLng> linePoints = linePolylineOptions.getPoints(); 
			int closestI = 0;
			closestILocation = LocationUtils.latLng2Location(linePoints.get(closestI)); 
			for (int i = 0; i<linePoints.size()-1; i++) {
				Location iLocation = LocationUtils.latLng2Location(linePoints.get(i));
				if(latLngLocation.distanceTo(iLocation) < 
						latLngLocation.distanceTo(closestILocation)) {
					closestI = i;
					closestILocation = LocationUtils.latLng2Location(linePoints.get(closestI));
				}
			}
		}
		return LocationUtils.location2LatLng(closestILocation);
	}
	
	/**
	 * @author naama
	 * @return
	 */
	public String getEstimatedTime() {
		return mEstimatedTime;
	}
	
	
}
