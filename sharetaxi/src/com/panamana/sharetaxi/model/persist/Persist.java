package com.panamana.sharetaxi.model.persist;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

public class Persist {
	
	private static final String TAG = Persist.class.getSimpleName();
	private static final boolean DEBUG = false;
	
		// save to disk
		public void save(Context context, String filename, List<PolylineOptions> polylineOptionsList) {
			File file;
			FileOutputStream fos = null;
			ObjectOutputStream out = null;
			boolean append = false;
			try {
				file = new File(context.getFilesDir(), filename);
				Log.i(TAG,"file: "+file.toString());
				if(!file.exists()) {
					Log.i(TAG,"file doesn't exist - create file");
					file.createNewFile();
				} else {
					Log.i(TAG,"file exist");
				}
				fos = new FileOutputStream(file,append);
				out = new ObjectOutputStream(fos);
				Log.i(TAG,"open write stream to "+filename);
				// write:
				// polylines size
				out.writeInt(polylineOptionsList.size());
				if(DEBUG) Log.i(TAG,"write polylines length "+polylineOptionsList.size()+" to "+filename);
				// polylines
				for(PolylineOptions polyline : polylineOptionsList) {
					
					Log.i(TAG,"write polyline "+polyline.toString()+" to "+filename);
					// color
					out.writeInt(polyline.getColor());
					if(DEBUG) Log.i(TAG,"write color "+polyline.getColor()+" to "+filename);
					// width
					out.writeFloat(polyline.getWidth());
					if(DEBUG) Log.i(TAG,"write width "+polyline.getWidth()+" to "+filename);
					// zindex
					out.writeFloat(polyline.getZIndex());
					if(DEBUG) Log.i(TAG,"write zindex "+polyline.getZIndex()+" to "+filename);
					// points:
					// size
					out.writeInt(polyline.getPoints().size());
					if(DEBUG) Log.i(TAG,"write size "+polyline.getPoints().size()+" to "+filename);
					// (lat,lng)...
					for (LatLng point : polyline.getPoints()) {
						out.writeDouble(point.latitude);
						out.writeDouble(point.longitude);
						if(DEBUG) Log.i(TAG,"write LatLng "+point.toString()+" to "+filename);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				try {
					if(fos!=null)
						fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					if(out!=null)
						out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				Log.i(TAG,"close write stream to "+filename);
			}
		}

		// restore from disk
		public ArrayList<PolylineOptions> restore(Context context, String filename) {
			ArrayList<PolylineOptions> polylines = null;
			File file;
			FileInputStream fis = null;
			ObjectInputStream in = null;
			try {
				file = new File(context.getFilesDir(), filename);
				if(!file.exists()) {
					return polylines;
				}
				fis = new FileInputStream(file);
				in = new ObjectInputStream(fis);
				Log.i(TAG,"open read stream to "+filename);

				int length = 0;
				try {
					// read:
					length = in.readInt();
				} catch (EOFException eof) {}
					
				if(length>0){
					polylines = new ArrayList<PolylineOptions>();
				}
				for(int i=0; i<length; i++) {					
					// color
					int color = in.readInt();
					if(DEBUG) Log.i(TAG,"read color "+color+" from "+filename);
					// width
					float width = in.readFloat();
					if(DEBUG) Log.i(TAG,"read width "+width+" from "+filename);
					// zindex
					float zindex = in.readFloat();
					if(DEBUG) Log.i(TAG,"read zindex "+zindex+" from "+filename);
					// points: 
					//size,
					int size = in.readInt();
					if(DEBUG) Log.i(TAG,"read size "+size+" from "+filename);
					//(lat,lng)...
					List<LatLng> points = new ArrayList<LatLng>();
					for (int j=0; j<size; j++) {
						double lat = in.readDouble();
						double lng = in.readDouble();
						LatLng latlng = new LatLng(lat, lng);
						points.add(latlng);
						if(DEBUG) Log.i(TAG,"read LatLng "+latlng.toString()+" from "+filename);
					}
					PolylineOptions polyline = new PolylineOptionsBuilder(color, width, zindex, points).build();
					Log.i(TAG,"read polyline "+polyline.toString()+" from "+filename);
					polylines.add(polyline);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				try {
					if(fis!=null)
						fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					if (in!=null)
						in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				Log.i(TAG,"close read stream from "+filename);
			}
			return polylines;
		}
}
